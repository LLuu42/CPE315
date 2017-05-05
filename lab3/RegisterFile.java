import java.lang.String;
import java.lang.Integer;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Set;
import java.io.PrintStream;

public class RegisterFile
{
	private PrintStream userDisplay = new PrintStream(System.out);
	private Hashtable<String,Integer> registers;

	public RegisterFile()
	{
		this.registers = new Hashtable<String,Integer>();
		setRegistersToZero();
	}

	public void setRegister(String register, int value)
	{
		registers.put(register, value);
	}

	public int getRegister(String register)
	{
		return registers.get(register);
	}

	public void printRegisters()
	{
		userDisplay.printf("$0 = 0 \t$v0 = %d \t$v1 = %d \t$a0 = %d\n", 
							registers.get("v0"), registers.get("v1"), registers.get("a0"));

		userDisplay.printf("$a1 = %d \t$a2 = %d \t$a3 = %d \t$t0 = %d\n", 
							registers.get("a1"), registers.get("a2"), registers.get("a3"), registers.get("t0"));
	
		userDisplay.printf("$t1 = %d \t$t2 = %d \t$t3 = %d \t$t4 = %d\n", 
							registers.get("t1"), registers.get("t2"), registers.get("t3"), registers.get("t4"));

		userDisplay.printf("$t5 = %d \t$t6 = %d \t$t7 = %d \t$s0 = %d\n", 
							registers.get("t5"), registers.get("t6"), registers.get("t7"), registers.get("s0"));

		userDisplay.printf("$s1 = %d \t$s2 = %d \t$s3 = %d \t$s4 = %d\n", 
							registers.get("s1"), registers.get("s2"), registers.get("s3"), registers.get("s4"));

		userDisplay.printf("$s5 = %d \t$s6 = %d \t$s7 = %d \t$t8 = %d\n", 
							registers.get("s5"), registers.get("s6"), registers.get("s7"), registers.get("t8"));

		userDisplay.printf("$t9 = %d \t$sp = %d \t$ra = %d\n", 
							registers.get("t9"), registers.get("sp"), registers.get("ra"));
	}

	public void setRegistersToZero()
	{
		registers.put("0", 0);
		registers.put("v0", 0);
		registers.put("v1", 0);
		registers.put("a0", 0);
		registers.put("a1", 0);
		registers.put("a2", 0);
		registers.put("a3", 0);
		registers.put("t0", 0);
		registers.put("t1", 0);
		registers.put("t2", 0);
		registers.put("t3", 0);
		registers.put("t4", 0);
		registers.put("t5", 0);
		registers.put("t6", 0);
		registers.put("t7", 0);
		registers.put("s0", 0);
		registers.put("s1", 0);
		registers.put("s2", 0);
		registers.put("s3", 0);
		registers.put("s4", 0);
		registers.put("s5", 0);
		registers.put("s6", 0);
		registers.put("s7", 0);
		registers.put("t8", 0);
		registers.put("t9", 0);
		registers.put("sp", 0);
		registers.put("ra", 0);
	}
}