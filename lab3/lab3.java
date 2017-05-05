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
		Scanner in = new Scanner(System.in);
		String input;
		char command;
		AssemblyParser aCode = new AssemblyParser(args); //(Put this in once ready to test file parsing)
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
				userDisplay.println("Dump Register State:");
				userDisplay.printf("pc = %d\n", pc);
				registers.printRegisters();
				break;

			case('s'):
				userDisplay.println("Step through program:");
				stepThrough(instructions, labels, input);
				break;

			case('r'):
				userDisplay.println("Run until the program ends");
				System.out.printf("Number of instructions: %d\n", instructions.size());
				runProgram(instructions, labels, instructions.size());
				break;

			case('m'):
				userDisplay.println("userDisplay data memory");
				break;

			case('c'):
				userDisplay.println("Simulator Reset");
				pc = 0;
				registers.setRegistersToZero();
				memory = new int[8192];
				break;

			default:
				userDisplay.println("Invalid input.");
		}
	}

	private static void stepThrough(ArrayList<Instruction> instructions, ArrayList<Label> labels, String input)
	{
		int steps = 1;
		if(input.length() > 1)
		{
			//parse through input to find number
			String[] args = input.split(" ");
			steps = Integer.parseInt(args[1]);
		}
		userDisplay.printf("%d instruction(s) executed.\n", steps);
		runProgram(instructions, labels, steps);
	}

	private static void runProgram(ArrayList<Instruction> instructions, ArrayList<Label> labels, int steps)
	{
		int i, x, y, memIdx;
		String label;
		Instruction currentInstruction;
		for(i = 0; i < steps; ++i)
		{
			currentInstruction = instructions.get(pc);

			switch(currentInstruction.getInstruction())
			{
				case "and":
					rInstruction(currentInstruction, '&', false);				
					break;

				case "or":
					rInstruction(currentInstruction, '|', false);
					break;

				case "add":
					rInstruction(currentInstruction, '+', false);		
					break;

				case "addi":
					rInstruction(currentInstruction, '+', true);	
					break;

				case "sub":
					rInstruction(currentInstruction, '-', false);
					break;

				case "sll":

					break;

				case "slt":
		
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

					break;

				case "jr":

					break;

				case "jal":

					break;

				default:
					; 
			}	
		}
	}

	private static int getMemAddress(Instruction currentInstruction)
	{
		int offset, memIdx;
		memIdx = registers.getRegister(Machinecode.getRegister(currentInstruction.getArguementAt(3)));
		offset = Integer.parseInt(Machinecode.getOffset(currentInstruction.getArguementAt(2)));
		return(memIdx + offset);
	}

	private static void rInstruction(Instruction currentInstruction, char operator, boolean immediate)
	{
		int x, y;
		x = registers.getRegister(currentInstruction.getArguementAt(2));

		if(immediate) 
		{
			y = Integer.parseInt(currentInstruction.getArguementAt(3));
		}
		
		else 
		{
			y = registers.getRegister(currentInstruction.getArguementAt(3));
		}

		switch(operator)
		{
			case('+'):
				registers.setRegister(currentInstruction.getArguementAt(1), x + y);	
				break;

			case('-'):
				registers.setRegister(currentInstruction.getArguementAt(1), x - y);	
				break;

			case('&'):
				registers.setRegister(currentInstruction.getArguementAt(1), x & y);	
				break;

			case('|'):
				registers.setRegister(currentInstruction.getArguementAt(1), x | y);	
				break;
		}
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