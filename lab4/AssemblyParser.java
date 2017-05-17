/**
* lab 3 Assignment
* CPE 315
* Dr. John Seng
* @author Lara Luu
* 26 April 2017
* Java 2-pass MIPS assembler  
* Loads in MIPS assembly files and output the corresponding machine code (to the screen). 
* @Input: MIPS assembly files with comments, labels, and whitespace (spaces and tabs)
* @Output: Machine code for the MIPS assembly code to the terminal.
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintStream;

public class AssemblyParser
{

	private static ArrayList<Instruction> instructions;
	private static ArrayList<Label> labels;

	public AssemblyParser(String filename) throws FileNotFoundException
	{
		this.instructions = new ArrayList<Instruction>(); //Array containing all the instructions
		this.labels = new ArrayList<Label>(); //Array containing all the labels

		parseFile(filename);
	}	

	public ArrayList<Instruction> getInstructions()
	{
		return instructions;
	}

	public ArrayList<Label> getLabels()
	{
		return labels;
	}

	/** 
	* Iterates through the array of Instructions and prints out the machine code for each one of them
	* Where it prints is specified in the given PrintStream argument
	*/
	public void printInstructionMachineCodes()
	{
		PrintStream file = new PrintStream(System.out);
		int i;
		for(i = 0; i < instructions.size(); ++i)
		{
			if(instructions.get(i).getMachinecode().equals("error"))
			{
				file.printf("invalid instruction: %s\n", instructions.get(i).getInstruction());
				return;
			}
			file.println(instructions.get(i).getMachinecode());
		}
	}

	private static void parseFile(String filename) throws FileNotFoundException
	{
		int address = -1;
		String line;

		File readFile = new File(filename);

		PrintStream file = new PrintStream(System.out);
		Scanner scanner = new Scanner(readFile);

		while(scanner.hasNextLine())
		{
			line = scanner.nextLine();

			if((line = line.trim()).length() > 0) //remove whitespace. do not process blank lines
			{

				line = line.replace("$", " ");
				line = line.replace(",", " ");
				line = line.replace("\t", " ");
				line = line.toLowerCase();
				++ address;

				if(hasLabel(line))
				{
					/* Create new lable object with lable name and address location */
					Label label = new Label(getLabel(line), address);
					labels.add(label);

				}

				line = getInstruction(line); //removes all comments and labels from the line.

				if(line.length() > 0)
				{
					/* Line is an address */
					Instruction instruction = new Instruction(line, address, labels);
					instructions.add(instruction);
				}
				else
				{ 	
					/* Line is either a comment or label only, do not count towards address length. */
					-- address;
				}
			}
		}

	}

	/** 
	* @return true if line has lable, false if not.
	*/
	private static boolean hasLabel(String line)
	{
		return (line.indexOf(':') > -1);
	}

	/** 
	* Finds and returns Assembly lable
	* @return String w /label
	*/
	private static String getLabel(String line)
	{
		return line.substring(0, line.indexOf(':'));
	}

	/** 
	* Removes all labels and comments from instruction comment.
	* @return truncated string if label found, same string if lable not found
	*/
	private static String getInstruction(String line)
	{
		line = removeLabel(line);
		line =  removeComment(line);
		return line.trim();
	}

	/** 
	* Removes labels from instruction comment.
	* @return truncated string if label found, same string if lable not found
	*/
	private static String removeLabel(String line)
	{
		int labelIdx = line.indexOf(':'); //finds location of comment, assumes no other non-commented ':' characters in line
		if(labelIdx > -1)
		{
			line = line.substring(labelIdx + 1, line.length());
		}
		return line;
	}

	/** 
	* Removes comments from instruction comment.
	* @return truncated string if label found, same string if lable not found
	*/
	private static String removeComment(String line)
	{
		int commentIdx = line.indexOf('#'); //finds location of comment, ignores all '#' characters thereafter
		if(commentIdx > -1)
		{
			line = line.substring(0, commentIdx);
		}

		return line;
	}
}