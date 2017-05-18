import java.lang.*;
import java.util.*;
import java.text.DecimalFormat;

/**
* lab 4 Assignment
* CPE 315
* Dr. John Seng
* @author Lara Luu
* 04 May 2017
* Java MIPS Simulator  
* Loads in MIPS assembly files and runs through them with user input. 
* @Input: MIPS assembly files with comments, labels, and whitespace (spaces and tabs)
* @Input: (Optional) script with given user inputs
* @Output: Various registers and actions based on user prompt
*/

import java.util.Scanner;
import java.lang.String;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintStream;
import java.util.ArrayList;



public class lab4
{
	private static int pc = 0;
	private static int cycles = 2; //playing a dangerous game here
	private static int instructionCount = 0;

	private static Processor processor = new Processor();

	private static PrintStream userDisplay = new PrintStream(System.out);
	private static RegisterFile registers = new RegisterFile();

	private static int[] memory = new int[8192];

	private static boolean jumpTaken = false;

	private static ArrayList<Instruction> instructions;
	private static ArrayList<Label> labels;


	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner in;
		if(args.length < 2)
		{
			in = new Scanner(System.in);
		}
		else
		{
			in = new Scanner(new File(args[1]));
		}
		String input;
		char command;
		AssemblyParser aCode = new AssemblyParser(args[0]); //(Put this in once ready to test file parsing)
		instructions = aCode.getInstructions();
		labels = aCode.getLabels();


		/* Begin prompting and accepting user input */
		userDisplay.printf("mips> ");

		while(!(input = in.nextLine()).equals("q"))
		{
			command = input.charAt(0);
			userDisplay.printf("%s\n", input);
			runCommand(command, input, instructions, labels);

			userDisplay.printf("mips> ");
		}
		userDisplay.println("q");

		//aCode.printInstructionMachineCodes(); // test to make sure lab 2 class still worked
	}


	private static void runCommand(char command, String input, ArrayList<Instruction> instructions, ArrayList<Label> labels)
	{

		switch(command)
		{
			case('h'):
				/* Help screen */
				printHelp();
				break;

			case('d'):
				userDisplay.printf("\npc = %d\n", pc);
				registers.printRegisters();
				break;

			case('s'):
				stepThrough(instructions, labels, input);
				break;

			case('r'):
				runUntilEnd(instructions, labels);
				break;

			case('m'):
				displayMemory(input);

				break;

			case('c'):
				userDisplay.println("\tSimulator reset");
				pc = 0;
				registers.setRegistersToZero();
				memory = new int[8192];
				break;

			default:
				userDisplay.println("Invalid input.");
		}
	}

	private static void displayMemory(String args)
	{
		String[] split = args.split(" ");
		int i;
		int start = Integer.parseInt(split[1]);
		int end = Integer.parseInt(split[2]);

		for(i = start; i <= end; ++i)
		{
			userDisplay.printf("[%d] = %d\n", i, memory[i]);
		}
	}

	private static void stepThrough(ArrayList<Instruction> instructions, ArrayList<Label> labels, String input)
	{
		int i;
		int steps = 1;
		if(input.length() > 1)
		{
			//parse through input to find number
			String[] args = input.split(" ");
			steps = Integer.parseInt(args[1]);
		}

		for(i = 0; i < steps; ++i)
		{
			runProgram(instructions, labels);
			userDisplay.printf("\npc\t\tif/id \tid/exe\texe/mem\tmem/wb \n", steps);
			userDisplay.printf("%d\t\t", pc);
			processor.printCycles();
		}
	}

	private static void runUntilEnd(ArrayList<Instruction> instructions, ArrayList<Label> labels)
	{
		while(pc < instructions.size())
		{
			runProgram(instructions, labels);
		}

		pc = -10;
		for(int i = 0; i < 3; ++i) //empty out processor
		{
			runInstruction(processor.pushInstruction(new Instruction("finish", -1, null)));
		}
		//DecimalFormat dtf = new DecimalFormat("0.###");

		userDisplay.printf("Program complete\n");
		userDisplay.printf("CPI = %.03f\t Cycles = %d\t Instructions = %d\n", 
				(Math.floor(1000 * (cycles / (double)(instructionCount)) + 0.5) / 1000), cycles, instructionCount);
		//userDisplay.println();
	}

	private static void runProgram(ArrayList<Instruction> instructions, ArrayList<Label> labels)
	{

		Instruction pushInstruction, currentInstruction;


		if(processor.hasJump(0))
		{
			pushInstruction = new Instruction("squash", -1, null);
			
		}
		else if(processor.checkForStall())
		{
			pushInstruction = new Instruction("stall", -1, null);
			--pc;

		}
		else
		{
			if(pc < 0)
			{
				pushInstruction = new Instruction("done", -1, null);
			}
			else
			{
				pushInstruction = instructions.get(pc);
			}
		}

		currentInstruction = processor.pushInstruction(pushInstruction);

		runInstruction(currentInstruction);	
	}

	public static void runInstruction(Instruction currentInstruction)
	{
		int x, y;
		String label;
		++instructionCount;
		++cycles;

	
		switch(currentInstruction.getInstruction())
		{
			case "and":
				rInstruction(currentInstruction, "&", false);				
				break;

			case "or":
				rInstruction(currentInstruction, "|", false);
				break;

			case "add":
				rInstruction(currentInstruction, "+", false);		
				break;

			case "addi":
				rInstruction(currentInstruction, "+", true);	
				break;

			case "sub":
				rInstruction(currentInstruction, "-", false);
				break;

			case "sll":
				rInstruction(currentInstruction, "<<", true);
				break;

			case "slt":
				rInstruction(currentInstruction, "slt", false);
				break;

			case "beq":
				x = registers.getRegister(currentInstruction.getArguementAt(1));
				y = registers.getRegister(currentInstruction.getArguementAt(2));
				label = currentInstruction.getArguementAt(3);
				if(x == y)
				{
					pc = getLabelAddress(labels, label);
					processor.branchTaken();
				}
				else
				{
					pc ++;
				}
				-- cycles;
				break;

			case "bne":
				x = registers.getRegister(currentInstruction.getArguementAt(1));
				y = registers.getRegister(currentInstruction.getArguementAt(2));
				label = currentInstruction.getArguementAt(3);
				if(x != y)
				{
					pc = getLabelAddress(labels, label);
					processor.branchTaken();
				}
				else
				{
					pc ++;
				}
				-- cycles;
				break;

			case "lw":
				// lw $t,C($s)     # $t = Memory[$s + C]
				registers.setRegister(currentInstruction.getArguementAt(1), memory[getMemAddress(currentInstruction)]);

				++pc;
				break;

			case "sw":
				// sw $t,C($s)     # Memory[$s + C] = $t
				memory[getMemAddress(currentInstruction)] = registers.getRegister(currentInstruction.getArguementAt(1));
				++pc;
				break;

			case "j":
				label = currentInstruction.getArguementAt(1);
				pc = getLabelAddress(labels, label);

				break;

			case "jr":
				pc = registers.getRegister(currentInstruction.getArguementAt(1));
				break;

			case "jal":
				registers.setRegister("ra", pc + 1);
				label = currentInstruction.getArguementAt(1);
				pc = getLabelAddress(labels, label);
				break;

			case "empty":
			case "stall":
			case "squash":
				++pc;
				--instructionCount;
				break;

			default:
				--instructionCount
				; 
		}
	}

	public void setPC(int i)
	{
		pc = i;
	}

	public static void incrementPC()
	{
		pc += 1;
	}

	public static void decrementPC()
	{
		pc -= 1;
	}

	private static int getMemAddress(Instruction currentInstruction)
	{
		int offset, memIdx;
		//userDisplay.println(currentInstruction.getArguementAt(3));
		memIdx = registers.getRegister(Machinecode.getRegister(currentInstruction.getArguementAt(3)));
		offset = Integer.parseInt(Machinecode.getOffset(currentInstruction.getArguementAt(2)));
		return(memIdx + offset);
	}

	private static void rInstruction(Instruction currentInstruction, String operation, boolean immediate)
	{
		int x, y, setValue = -1;
		x = registers.getRegister(currentInstruction.getArguementAt(2));

		if(immediate) 
		{
			y = Integer.parseInt(currentInstruction.getArguementAt(3));
		}
		
		else 
		{
			y = registers.getRegister(currentInstruction.getArguementAt(3));
		}

		switch(operation)
		{
			case("+"):
				setValue = x + y;
				break;

			case("-"):
				setValue = x - y;
				break;

			case("&"):
				setValue = x & y;
				break;

			case("|"):
				setValue = x | y;
				break;

			case("<<"):
				setValue = x << y;
				break;

			case("slt"):	
				if(x < y)
				{
					setValue = 1;
				}
				else
				{
					setValue = 0;
				}
				break;
		}
		registers.setRegister(currentInstruction.getArguementAt(1), setValue);
		++pc;				
	}

	private static int getLabelAddress(ArrayList<Label> labels, String label)
	{
		int address = -1;
		for (Label l : labels)
		{
			if(l.getLabel().equals(label))
			{
				address = l.getAddress();
			}
		}
		return address;
	}

	private static void printHelp()
	{
		userDisplay.println();
		userDisplay.println("h = show help");
		userDisplay.println("d = dump register state");
		userDisplay.println("s = single step through the program (i.e. execute 1 instruction and stop)");
		userDisplay.println("s num = step through num instructions of the program");
		userDisplay.println("r = run until the program ends");
		userDisplay.println("m num1 num2 = display data memory from location num1 to num2");
		userDisplay.println("c = clear all registers, memory, and the program counter to 0");
		userDisplay.println("q = exit the program");
		userDisplay.println();
	}



}