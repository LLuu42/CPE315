/**
* Class Instruction
* @author Lara Luu
* 26 April 2017
* Stores the data of the instruction, the instruction's address, the machine code for the instruction, and the instruction split by argument.
*/

import java.lang.String;
import java.util.ArrayList;

public class Instruction
{
	private String line; //original instruction line, trimmed by whitespace
	private String[] instructions; //Instruction broken up by arguments
	private Machinecode machinecode; //machine code for the instruction
	private int address; //location of the instruction

	public Instruction(String line, int address, ArrayList<Label> labels)
	{
		this.line = line;
		this.instructions = line.split(" +");
		this.address = address;
		this.machinecode = new Machinecode(instructions, labels, address);
	}

	public String getInstruction()
	{
		return instructions[0];
	}

	public String getArguementAt(int i)
	{
		return instructions[i];
	}

	public int getAddress()
	{
		return this.address;
	}

	public String getMachinecode()
	{
		return this.machinecode.getMachinecode();
	}

	/* Prints out the contents of the instruction. Used for debugging purposes. */
	public void printAll()
	{
		for(String s : instructions)
		{
			System.out.println(s);
		}
	}
}