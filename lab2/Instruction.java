import java.lang.String;

class Instruction
{
	private String line;
	private String[] instructions;
	private String machinecode;
	private int address;

	public Instruction(String line, int address)
	{
		this.line = line;
		this.instructions = line.split(" ");
		this.address = address;
		//splitLine(line);

	}

	public String getInstruction()
	{
		return instructions[0];
	}

	public int getAddress()
	{
		return this.address;
	}

	public void getMachinecode()
	{
		/* Returns machine code */
	}
}