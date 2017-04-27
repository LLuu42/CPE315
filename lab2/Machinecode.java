import java.lang.String;
import java.lang.Integer;
import java.util.Hashtable;
import java.util.ArrayList;


public class Machinecode
{
	private String machinecode;
	private String[] instructions;
	private ArrayList<Label> labels;
	private Hashtable<String, String> instructionTable;
	private Hashtable<String, String> registerTable;


	public Machinecode(String[] instructions, ArrayList<Label> labels)
	{
		this.instructions = instructions;
		this.instructionTable = new Hashtable<String, String>();
		this.registerTable = new Hashtable<String, String>();
		this.labels = labels;
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
		String offset;
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
				System.out.println("addi");
				String immediate = getBinaryString(instructions[3], 16);

				this.machinecode = (instructionTable.get("addi") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[1]) + " " + 
								immediate);			
				break;

			case "sll":
				System.out.println("sll");
				this.machinecode = (instructionTable.get("sll") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[1]) + " " + 
								getBinaryString(instructions[3], 5) + " " +
								"00000");
				break;

			case "sub":
				System.out.println("sub");
				this.machinecode = (instructionTable.get("sub") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[3]) + " " + 
								registerTable.get(instructions[1]) + " " +
								"00000" + " " + "100010");
				break;

			case "slt":
				System.out.println("slt");
				this.machinecode = (instructionTable.get("slt") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[3]) + " " + 
								registerTable.get(instructions[1]) + " " +
								"00000" + " " + "101010");			
				break;

			case "beq":
				System.out.println("beq");
				System.out.printf("Label: %s\n", instructions[3]);
				offset = getLabelAddress(instructions[3]);

				this.machinecode = (instructionTable.get("beq") + " " + 
								registerTable.get(instructions[1]) + " " +
								registerTable.get(instructions[2]) + " " + 
								offset);		
				break;

			case "bne":
				System.out.println("bne");
				System.out.printf("Label: %s\n", instructions[3]);
				offset = getLabelAddress(instructions[3]);

				this.machinecode = (instructionTable.get("bne") + " " + 
								registerTable.get(instructions[1]) + " " +
								registerTable.get(instructions[2]) + " " + 
								offset);
				break;

			case "lw":
				break;

			case "sw":
				break;

			case "j":
				System.out.println("j");
				offset = getLabelAddress(instructions[1]);
				this.machinecode = (instructionTable.get("jr") + " " + offset);
				break;

			case "jr":
				System.out.println("jr");
				this.machinecode = (instructionTable.get("jr") + " " + 
								registerTable.get(instructions[1]) + " " + 
								"000 000 000 001000");
				break;

			case "jal":
				System.out.println("jal");
				offset = getLabelAddress(instructions[1]);
				this.machinecode = (instructionTable.get("jr") + " " + offset);
				break;

			default:
				this.machinecode = "error"; 
		}

	}

	private String getLabelAddress(String label)
	{
		int i;
		String labelAddress = "0000000000000000";
		for(i = 0; i < labels.size(); ++i)
		{
			if(labels.get(i).getLabel().equals(label))
			{
				labelAddress = getBinaryString(Integer.toString(labels.get(i).getAddress()), 16);
			}
		}
		return labelAddress;
	}

	private String getBinaryString(String toBinary, int wordLength)
	{
		String tmp = Integer.toBinaryString(Integer.parseInt(toBinary));
		while(tmp.length() < wordLength)
		{
			tmp = "0" + tmp;
		}
		return tmp;
	}

	private void setInstructionTable()
	{
		instructionTable.put("and", "000000");
		instructionTable.put("or", "000000");
		instructionTable.put("addi", "001000");
		instructionTable.put("add", "000000");
		
		instructionTable.put("sll", "000000 00000");

		instructionTable.put("sub", "000000");
		instructionTable.put("slt", "000000");

		instructionTable.put("beq", "000100");
		instructionTable.put("bne", "000101");

		instructionTable.put("j", "000010");
		instructionTable.put("jr", "000000");
		instructionTable.put("jal", "000011");

	}

	private void setRegisterTable()
	{
		registerTable.put("0", "00000");
		registerTable.put("zero", "00000");
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