/**
* Class Machinecode
* @author Lara Luu
* 26 April 2017
* Generates the machine codes depending on instruction type and arguments
*/

import java.lang.String;
import java.lang.Integer;
import java.util.Hashtable;
import java.util.ArrayList;


public class Machinecode
{
	private String machinecode;
	private String[] instructions;
	private ArrayList<Label> labels;
	private int address;
	private Hashtable<String, String> instructionTable; //maps the instructions to their corresponding opcode
	private Hashtable<String, String> registerTable;	// maps the register names to their corresponding binary values


	public Machinecode(String[] instructions, ArrayList<Label> labels, int address)
	{
		this.instructions = instructions;
		this.instructionTable = new Hashtable<String, String>();
		this.registerTable = new Hashtable<String, String>();
		this.labels = labels;
		this.address = address;
		setInstructionTable();
		setRegisterTable();
		setMachinecode();
	}

	public String getMachinecode()
	{
		return machinecode;
	}

	/* Sets different machine codes based on instruction type. */
	/* Organize into instruction types to simplify for next project */
	private void setMachinecode()
	{
		String instruction = instructions[0];
		String offset;
		String reg;

		/* Switch generation of different codes based on instruction. Pretty lengthy, but straightforward. */
		switch(instruction)
		{
			case "and":
				this.machinecode = (instructionTable.get("and") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[3]) + " " + 
								registerTable.get(instructions[1]) + " " +
								"00000" + " " + "100100");

				break;

			case "or":
				this.machinecode = (instructionTable.get("or") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[3]) + " " + 
								registerTable.get(instructions[1]) + " " +
								"00000" + " " + "100101");
				break;

			case "add":
				this.machinecode = (instructionTable.get("add") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[3]) + " " + 
								registerTable.get(instructions[1]) + " " +
								"00000" + " " + "100000");
				break;

			case "addi":
				String immediate = getBinaryString(instructions[3], 16);

				this.machinecode = (instructionTable.get("addi") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[1]) + " " + 
								immediate);			
				break;

			case "sll":
				this.machinecode = (instructionTable.get("sll") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[1]) + " " + 
								getBinaryString(instructions[3], 5) + " " +
								"000000");
				break;

			case "sub":
				this.machinecode = (instructionTable.get("sub") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[3]) + " " + 
								registerTable.get(instructions[1]) + " " +
								"00000" + " " + "100010");
				break;

			case "slt":
				this.machinecode = (instructionTable.get("slt") + " " + 
								registerTable.get(instructions[2]) + " " +
								registerTable.get(instructions[3]) + " " + 
								registerTable.get(instructions[1]) + " " +
								"00000" + " " + "101010");			
				break;

			case "beq":
				offset = getBrnLabelAddress(instructions[3], 16);

				this.machinecode = (instructionTable.get("beq") + " " + 
								registerTable.get(instructions[1]) + " " +
								registerTable.get(instructions[2]) + " " + 
								offset);		
				break;

			case "bne":
				offset = getBrnLabelAddress(instructions[3], 16);

				this.machinecode = (instructionTable.get("bne") + " " + 
								registerTable.get(instructions[1]) + " " +
								registerTable.get(instructions[2]) + " " + 
								offset);
				break;

			case "lw":
				offset = getBinaryString(getOffset(instructions[2]), 16);
				reg = getRegister(instructions[3]);
				this.machinecode = (instructionTable.get("lw") + " " + 
								registerTable.get(reg) + " " +
								registerTable.get(instructions[1]) + " " + 
								offset);
				break;

			case "sw":
				offset = getBinaryString(getOffset(instructions[2]), 16);
				reg = getRegister(instructions[3]);
				this.machinecode = (instructionTable.get("sw") + " " + 
								registerTable.get(reg) + " " +
								registerTable.get(instructions[1]) + " " + 
								offset);
				break;

			case "j":
				offset = getLabelAddress(instructions[1], 26);
				this.machinecode = (instructionTable.get("j") + " " + offset);
				break;

			case "jr":
				this.machinecode = (instructionTable.get("jr") + " " + 
								registerTable.get(instructions[1]) + " " + 
								"000000000000000 001000");
				break;

			case "jal":
				offset = getLabelAddress(instructions[1], 26);
				this.machinecode = (instructionTable.get("jal") + " " + offset);
				break;

			default:
				this.machinecode = "error"; 
		}

	}

	/* Finds the offset of lw and sw functions. */ 
	/* Does this by removing the parentheses in the offset arguement(switch to trim later)*/
	public static String getOffset(String line)
	{
		int parenIdx = line.indexOf('(');
		return line.substring(0, parenIdx);
	}

	/* Finds the register stored in the parentheses of lw and sw functions. */ 
	/* Does this by removing the parentheses in the offset arguement(switch to trim later)*/
	public static String getRegister(String line)
	{
		int closeIdx = line.indexOf(')');
		return line.substring(0, closeIdx);
	}

	/* Finds the branching address of the code. */
	/* Formula: brn address = (current address - label address - 1) */  
	private String getBrnLabelAddress(String label, int length)
	{
		int i;
		String labelAddress = "0000000000000000";
		for(i = 0; i < labels.size(); ++i)
		{
			if(labels.get(i).getLabel().equals(label))
			{
				labelAddress = getBinaryString(Integer.toString(labels.get(i).getAddress() - address - 1), length);
			}
		}
		return labelAddress;
	}

	/* Finds and returns the label's address (unmodified) */
	private String getLabelAddress(String label, int length)
	{
		int i;
		String labelAddress = "0000000000000000";
		for(i = 0; i < labels.size(); ++i)
		{
			if(labels.get(i).getLabel().equals(label))
			{
				labelAddress = getBinaryString(Integer.toString(labels.get(i).getAddress()), length);
			}
		}
		return labelAddress;
	}

	/* Finds and returns the the binary string of an Integer argument. */
	/* If binary String does not have enough leading 0's to match the machine code length, appends 0s onto the code. */
	private String getBinaryString(String toBinary, int wordLength)
	{
		String tmp = Integer.toBinaryString(Integer.parseInt(toBinary));

		if(Integer.parseInt(toBinary) < 0)
		{
			tmp = tmp.substring(16);
		}
		while(tmp.length() < wordLength)
		{
			tmp = "0" + tmp;
		}
		return tmp;
	}

	/* Maps instructions to their various opcode values */
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

		instructionTable.put("lw", "100011");
		instructionTable.put("sw", "101011");

		instructionTable.put("j", "000010");
		instructionTable.put("jr", "000000");
		instructionTable.put("jal", "000011");

	}

	/* Maps registers to their binary values */
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
		registerTable.put("sp", "11101");
		registerTable.put("ra", "11111");
	}


}