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



public class lab3
{
	private static PrintStream userDisplay = new PrintStream(System.out);

	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner in = new Scanner(System.in);
		String input;
		char command;
		AssemblyParser aCode = new AssemblyParser(args); //(Put this in once ready to test file parsing)

		/* Begin prompting and accepting user input */
		userDisplay.printf("mips> ");

		while(!(input = in.nextLine()).equals("q"))
		{
			userDisplay.println("Input: " + input);
			command = input.charAt(0);

			runCommand(command, input);

			userDisplay.printf("mips> ");
		}

		aCode.printInstructionMachineCodes(); // test to make sure lab 2 class still worked
	}


	private static void runCommand(char command, String input)
	{

		switch(command)
		{
			case('h'):
			/* Help screen */
				userDisplay.println();
				userDisplay.println("h = show help");
				userDisplay.println("d = dump register state");
				userDisplay.println("s = single step through the program (i.e. execute 1 instruction and stop)");
				userDisplay.println("s num = step through num instructions of the program");
				userDisplay.println("r = run until the program ends");
				userDisplay.println("m num1 num2 = display data memory from location num1 to num2");
				userDisplay.println("c = clear all registers, memory, and the program counter to 0");
				userDisplay.println("q = exit the program");
				break;

			case('d'):
				userDisplay.println("Dump Register State:");
				break;

			case('s'):
				userDisplay.println("Step through program:");
				break;

			case('r'):
				userDisplay.println("Run until the program ends");
				break;

			case('m'):
				userDisplay.println("userDisplay data memory");
				break;

			case('c'):
				userDisplay.println("Clear all registers and shit.");
				break;

			default:
				userDisplay.println("Invalid input.");
		}

	}


}