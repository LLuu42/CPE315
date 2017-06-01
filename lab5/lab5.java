import java.lang.*;
import java.util.*;

/**
* lab 3 Assignment
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



public class lab5 
{
	private static int pc = 0;
	private static PrintStream userDisplay = new PrintStream(System.out);

	private static PrintStream printCsv;

	private static RegisterFile registers = new RegisterFile();
	private static int[] memory = new int[8192];
	private static BranchPredictor bPredictor;

	public static void main(String[] args) throws FileNotFoundException
	{
		int ghrSize = 0;
		Scanner in;
		if(args.length < 2)
		{
			in = new Scanner(System.in);
		}
		else
		{
			in = new Scanner(new File(args[1]));
		}

		if(args.length == 3)
		{
			ghrSize = Integer.parseInt(args[2]);
		}
		else
		{
			ghrSize = 2;
		}

		String input;
		char command;
		AssemblyParser aCode = new AssemblyParser(args[0]); //(Put this in once ready to test file parsing)
		ArrayList<Instruction> instructions = aCode.getInstructions();
		ArrayList<Label> labels = aCode.getLabels();


		bPredictor = new BranchPredictor(ghrSize);

		File csvFile = new File("coordinates.csv");
		printCsv = new PrintStream(csvFile);


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

			case('b'):
				bPredictor.printResults();
				break;

			case('x'):
				runUntilBranch(instructions, labels);

			case('o'):
				printToCsv();
				break;

			default:
				userDisplay.println("Invalid input.");
		}
	}

	private static void printToCsv()
	{
		for(int i = 0; i < memory.length; i+=2)
		{
			printCsv.printf("%d, %d\n", memory[i], memory[i+1]);
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
		userDisplay.printf("\t%d instruction(s) executed\n", steps);
		for(i = 0; i < steps; ++i)
		{
			runProgram(instructions, labels);
		}
	}

	private static void runUntilEnd(ArrayList<Instruction> instructions, ArrayList<Label> labels)
	{
		while(pc < instructions.size())
		{
			runProgram(instructions, labels);
		}
	}

	private static void runUntilBranch(ArrayList<Instruction> instructions, ArrayList<Label> labels)
	{
		while(pc < instructions.size() && (!instructions.get(pc).equals("beq") && !instructions.get(pc).equals("bne")))
		{
			runProgram(instructions, labels);
		}
	}

	private static void runProgram(ArrayList<Instruction> instructions, ArrayList<Label> labels)
	{
		int x, y;
		String label;
		boolean prediction = true;
		boolean result = true;
		Instruction currentInstruction;
		currentInstruction = instructions.get(pc);

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
				prediction = bPredictor.makePrediction();

				x = registers.getRegister(currentInstruction.getArguementAt(1));
				y = registers.getRegister(currentInstruction.getArguementAt(2));
				label = currentInstruction.getArguementAt(3);
				if(x == y)
				{
					result = true;
					pc = getLabelAddress(labels, label);
				}
				else
				{
					result = false;
					pc ++;
				}
				bPredictor.updatePrediction(prediction, result);
				break;

			case "bne":
				prediction = bPredictor.makePrediction();

				x = registers.getRegister(currentInstruction.getArguementAt(1));
				y = registers.getRegister(currentInstruction.getArguementAt(2));
				label = currentInstruction.getArguementAt(3);
				if(x != y)
				{
					result = true;
					pc = getLabelAddress(labels, label);
				}
				else
				{
					result = false;
					pc ++;
				}

				bPredictor.updatePrediction(prediction, result);
				break;

			case "lw":
				// lw $t,C($s)     # $t = Memory[$s + C]
				registers.setRegister(currentInstruction.getArguementAt(1), memory[getMemAddress(currentInstruction)]);
				++pc;	
				break;

			case "sw":
				// sw $t,C($s)     # Memory[$s + C] = $t
				//userDisplay.println("pc: " + pc);
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

			default:
				; 
		}	
	}

	private static int getMemAddress(Instruction currentInstruction)
	{
		int offset, memIdx;
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
		userDisplay.println("o = output a comma separated listing of the x,y coordinates to a file called coordinates.csv ");
		userDisplay.println("b = output the branch predictor accuracy");
		userDisplay.println("q = exit the program");
		userDisplay.println();
	}



}