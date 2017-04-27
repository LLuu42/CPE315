
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintStream;

public class lab2
{
	public static void main(String[] args)
	{
		int address = -1;
		int i;
		String line;

		PrintStream file = new PrintStream(System.out);
		Scanner scanner = new Scanner(System.in);

		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		ArrayList<Label> labels = new ArrayList<Label>();

		while(scanner.hasNextLine())
		{
			line = scanner.nextLine();

			if(line.equals("yolo"))
				break;

			if((line = line.trim()).length() > 0) //remove whitespace. do not process blank lines
			{
				/* Processes the line */
				file.println("Processing Line.");

				line = line.replace("$", " ");
				line = line.replace(",", " ");
				line = line.toLowerCase();
				++ address;

				if(hasLabel(line))
				{
					Label label = new Label(getLabel(line), address);
					labels.add(label);
					file.printf("Label: %s, address: %d\n", labels.get(0).getLabel(), labels.get(0).getAddress());

				}

				line = getInstruction(line);

				if(line.length() > 0)
				{
					Instruction instruction = new Instruction(line, address, labels);
					instructions.add(instruction);
					//file.println(instruction.getMachinecode());
					//file.printf("Instruction: %s, Address: %d\n", instruction.getInstruction(), instruction.getAddress());
					//instructions.add(instruction);
				}
				else
				{
					-- address;
				}
			}
		}
		printInstructionMachineCodes(file, instructions);
	}	

	private static void printInstructionMachineCodes(PrintStream file, ArrayList<Instruction> instructions)
	{
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

	private static boolean hasLabel(String line)
	{
		return (line.indexOf(':') > -1);
	}

	private static String getLabel(String line)
	{
		return line.substring(0, line.indexOf(':'));
	}

	private static String getInstruction(String line)
	{
		line = removeLabel(line);
		line =  removeComment(line);
		return line.trim();
	}

	private static String removeLabel(String line)
	{
		int labelIdx = line.indexOf(':');
		if(labelIdx > -1)
		{
			line = line.substring(labelIdx + 1, line.length());
		}
		return line;
	}

	private static String removeComment(String line)
	{
		int commentIdx = line.indexOf('#');
		if(commentIdx > -1)
		{
			line = line.substring(0, commentIdx);
		}

		return line;
	}
}