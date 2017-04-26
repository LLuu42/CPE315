
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

			if((line = line.trim()).length() == 0) //remove whitespace
			{
				/* Do not process blank lines */
			}
			else
			{
				/* Processes the line */
				file.println("Processing Line.");
				++ address;

				switch (lineType(line))
				{
					case "comment":					
						file.println("Comment.");
						-- address;						
						break;

					case "label":
						file.println("Label.");

						file.printf("Label: %s\n", getLabel(line));
						labels.add(new Label(getLabel(line), address));
						-- address;

						break;

					case "label and other":
						file.println("Label and Instruction");

						file.printf("Label: %s\n", getLabel(line));
						labels.add(new Label(getLabel(line), address));

					case "no label":
						file.println("No Label.");					
						line = getInstruction(line);

						/* Check if line is only a label with a comment*/
						if(line.length() > 0) 
						{
							Instruction instruction = new Instruction(line, address);
							file.printf("Instruction: %s, Address: %d\n", instruction.getInstruction(), instruction.getAddress());
						}
						else
						{
							/* no instruction present */
							-- address;
						}

						break;
				}

			}
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

	private static String lineType(String line)
	{
		if (line.charAt(0) == '#')
		{
			return "comment";
		}
		else if (line.contains(":"))
		{
			if(line.indexOf(':') == line.length() - 1)
			{
				return "label";
			}
			else
			{
				return "label and other";
			}
		}
		else
		{
			return "no label";
		}
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