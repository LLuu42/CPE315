import java.lang.String;

public class Instruction
{
	private String line;
	private String[] instructions;
	private MachineCode machinecode;
	private int address;

	public Instruction(String line, int address)
	{
		this.line = line;
		this.instructions = line.split(" +");
		this.address = address;
		this.machinecode = new MachineCode(instructions);
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
		return this.machinecode.getMachineCode();
	}

	public void printAll()
	{
		System.out.println("Printing shit");
		for(String s : instructions)
		{
			System.out.println(s);
		}
	}
}