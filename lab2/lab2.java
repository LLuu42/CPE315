
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

				switch (lineType(line))
				{
					case "comment":
						file.println("Comment.");
						++ address;
						break;

					case "label":
						file.println("Label.");

						file.printf("Label: %s\n", getLabel(line));
						labels.add(new Label(getLabel(line), address));

						break;

					case "label and instruction":
						file.println("Lable and Instruction");

						file.printf("Label: %s\n", getLabel(line));
						labels.add(new Label(getLabel(line), address));

						file.printf("Instruction: %s\n", getInstruction(line));

						break;

					case "instruction":
						file.println("Instruction.");

						file.printf("Instruction: %s\n", getInstruction(line));						

						break;
				}

				

			}
		}


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
				return "label";
			else
				return "label and instruction";
		}
		else
		{
			return "instruction";
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