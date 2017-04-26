import java.lang.String;

public class MachineCode
{
	private String machineCode;
	private String[] instructions;

	public MachineCode(String[] instructions)
	{
		this.instructions = instructions;
		setMachinecode();
	}

	public String getMachineCode()
	{
		return machineCode;
	}


	private void setMachinecode()
	{
		switch(instructions[0])
		{
			case "and":
				System.out.println("Let's start with and.");


				break;
			case "or":
				break;
			case "add":
				break;
			case "addi":
				break;
			case "sll":
				break;
			case "sub":
				break;
			case "slt":
				break;
			case "beq":
				break;
			case "bne":
				break;
			case "lw":
				break;
			case "sw":
				break;
			case "jr":
				break;
			case "jal":
				break;
		}

	}

}