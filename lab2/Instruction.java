import java.lang.String;
import java.util.ArrayList;

public class Instruction
{
	private String line;
	private String[] instructions;
	private Machinecode machinecode;
	private int address;

	public Instruction(String line, int address, ArrayList<Label> labels)
	{
		this.line = line;
		this.instructions = line.split(" +");
		this.address = address;
		this.machinecode = new Machinecode(instructions, labels);
	}

	public String getInstruction()
	{
		return instructions[0];
	}

	public int getAddress()
	{
		return this.address;
	}

	public String getMachinecode()
	{
		return this.machinecode.getMachinecode();
	}

	public void printAll()
	{
		//System.out.println("Printing shit");
		for(String s : instructions)
		{
			System.out.println(s);
		}
	}
}