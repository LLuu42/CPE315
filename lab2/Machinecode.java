import java.lang.String;
import java.util.Hashtable;


public class Machinecode
{
	private String machinecode;
	private String[] instructions;
	private Hashtable<String, String> instructionTable;
	private Hashtable<String, String> registerTable;


	public Machinecode(String[] instructions)
	{
		this.instructions = instructions;
		this.instructionTable = new Hashtable<String, String>();
		this.registerTable = new Hashtable<String, String>();
		setInstructionTable();
		setRegisterTable();
		setMachinecode();
	}

	public String getMachinecode()
	{
		return machinecode;
	}

	private void setMachinecode()
	{
		String instruction = instructions[0];
		switch(instruction)
		{
			case "and":
				System.out.println("And");
				this.machinecode = (instructionTable.get("and") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[3]) + " " + 
								registerTable.get(instructions[1]) + " " +
								"00000" + " " + "100100");
				//System.out.println(machinecode);

				break;

			case "or":
				System.out.println("Or");
				this.machinecode = (instructionTable.get("or") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[3]) + " " + 
								registerTable.get(instructions[1]) + " " +
								"00000" + " " + "100101");
				//System.out.println(machinecode);
				break;

			case "add":
				System.out.println("add");
				this.machinecode = (instructionTable.get("add") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[3]) + " " + 
								registerTable.get(instructions[1]) + " " +
								"00000" + " " + "100000");
				//System.out.println(machinecode);
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

			default:
				this.machinecode = "error"; 
		}

	}
	private void setInstructionTable()
	{
		instructionTable.put("and", "000000");
		instructionTable.put("or", "000000");
		instructionTable.put("add", "000000");
	}

	private void setRegisterTable()
	{
		registerTable.put("0", "00000");
		registerTable.put("v0", "00010");
		registerTable.put("v1", "00011");
		registerTable.put("a0", "00100");
		registerTable.put("a1", "00101");
		registerTable.put("a2", "00110");
		registerTable.put("a3", "00111");
		registerTable.put("t0", "01000");
		registerTable.put("t1", "01001");
		registerTable.put("t2", "01010");
		registerTable.put("t3", "01011");
		registerTable.put("t4", "01100");
		registerTable.put("t5", "01101");
		registerTable.put("t6", "01110");
		registerTable.put("t7", "01111");
		registerTable.put("s0", "10000");
		registerTable.put("s1", "10001");
		registerTable.put("s2", "10010");
		registerTable.put("s3", "10011");
		registerTable.put("s4", "10100");
		registerTable.put("s5", "10101");
		registerTable.put("s6", "10110");
		registerTable.put("s7", "10111");
		registerTable.put("t8", "11000");
		registerTable.put("t9", "11001");
		registerTable.put("sp", "00000");
	}


}