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



public class lab3
{
	private static int pc = 0;
	private static PrintStream userDisplay = new PrintStream(System.out);
	private static RegisterFile registers = new RegisterFile();
	private static int[] memory = new int[8192];

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
		ArrayList<Instruction> instructions = aCode.getInstructions();
		ArrayList<Label> labels = aCode.getLabels();


		/* Begin prompting and accepting user input */
		userDisplay.printf("mips> ");

		while(!(input = in.nextLine()).equals("q"))
		{
			userDisplay.println("Input: " + input);
			command = input.charAt(0);

			runCommand(command, input, instructions, labels);

			userDisplay.printf("mips> ");
		}

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
				userDisplay.printf("pc = %d\n", pc);
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
				userDisplay.println("\tSimulator Reset");
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
		userDisplay.printf("\t%d instruction(s) executed.\n", steps);
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

	private static void runProgram(ArrayList<Instruction> instructions, ArrayList<Label> labels)
	{
		int x, y;
		String label;
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
				userDisplay.println("add");
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
				rInstruction(currentInstruction, "slt", true);
				break;

			case "beq":
				System.out.println("beq");
				x = registers.getRegister(currentInstruction.getArguementAt(1));
				y = registers.getRegister(currentInstruction.getArguementAt(2));
				label = currentInstruction.getArguementAt(3);
				if(x == y)
				{
					pc = getLabelAddress(labels, label);
				}
				else
				{
					pc ++;
				}
				break;

			case "bne":
				System.out.println("bne");
				x = registers.getRegister(currentInstruction.getArguementAt(1));
				y = registers.getRegister(currentInstruction.getArguementAt(2));
				label = currentInstruction.getArguementAt(3);
				if(x != y)
				{
					pc = getLabelAddress(labels, label);
				}
				else
				{
					pc ++;
				}
				break;

			case "lw":
				// lw $t,C($s)     # $t = Memory[$s + C]
				System.out.println("lw");
				registers.setRegister(currentInstruction.getArguementAt(1), getMemAddress(currentInstruction));
				++pc;	
				break;

			case "sw":
				// sw $t,C($s)     # Memory[$s + C] = $t
				System.out.println("sw");
				memory[getMemAddress(currentInstruction)] = registers.getRegister(currentInstruction.getArguementAt(1));
				++pc;
				break;

			case "j":
				label = currentInstruction.getArguementAt(3);
				pc = getLabelAddress(labels, label);
				break;

			case "jr":
				pc = registers.getRegister(currentInstruction.getArguementAt(1));
				break;

			case "jal":
				registers.setRegister(currentInstruction.getArguementAt(1), pc+1);
				label = currentInstruction.getArguementAt(2);
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
		userDisplay.println("q = exit the program");
		userDisplay.println();
	}



}