import java.io.PrintStream;

public class Processor
{
	private final int IFID = 0;
	private final int IDEXE = 1;
	private final int EXEMEM = 2;;
	private final int MEMWB = 3;

	private final Instruction empty = new Instruction("empty", -1, null);
	private final Instruction squash = new Instruction("squash", -1, null);


	private int cycles;
	private Instruction [] instructionQueue;

	private static PrintStream userDisplay = new PrintStream(System.out);

	private static boolean decrementPC = false;

	public Processor()
	{
		this.instructionQueue = new Instruction[4];

		//initialize to empty instructions
		for(int i = 0; i < instructionQueue.length; ++i)
		{
			instructionQueue[i] = new Instruction("empty", -1, null);;
		}
	}	

	public Instruction pushInstruction(Instruction instruction)
	{

		Instruction dequeuedInstruction; // returns the instruction to be run in the processor

		if(hasJump(0))
		{

			lab4.runInstruction(instructionQueue[0]); //sets the PC counter to whatever jump is
			decrementPC = true;
		}
		if(hasJump(2))
		{
			dequeuedInstruction = new Instruction("yolo", -1, null);
			lab4.incrementPC();
		}
		else
		{
			dequeuedInstruction = instructionQueue[EXEMEM]; // 3rd instruction b/c run in exe stage
		}

		// Shift all the instructions over in the processor
		instructionQueue[MEMWB] = instructionQueue[EXEMEM];
		instructionQueue[EXEMEM] = instructionQueue[IDEXE];

		if(instruction.getInstruction().equals("stall"))
		{
			//Entered in a stall, have to swap indexes 0 and 1
			instructionQueue[IDEXE] = instruction;
		}
		else
		{
			instructionQueue[IDEXE] = instructionQueue[IFID];
			instructionQueue[IFID] = instruction;
		}

		if(decrementPC)
		{
			lab4.decrementPC();
			decrementPC = false;
		}

		++cycles;
		return dequeuedInstruction;
	}

	public void branchTaken()
	{
		//Squashes the next 3 instructions
		instructionQueue[EXEMEM] = squash;
		instructionQueue[IDEXE] = squash;
		instructionQueue[IFID] = squash;

	}

	public void printCycles()
	{
		int i;

		for(i = 0; i < instructionQueue.length; ++i)
		{
			//Print the number of instructions
			userDisplay.print(instructionQueue[i].getInstruction() + "\t");
		}
		userDisplay.println();
		userDisplay.println();
	}

	public boolean hasJump(int i)
	{
		return instructionQueue[i].getInstruction().equals("j") || 
			instructionQueue[i].getInstruction().equals("jr") || 
			instructionQueue[i].getInstruction().equals("jal");
	}

	public int getCycles()
	{
		return cycles;
	}

	public boolean checkForStall()
	{
		boolean stall = false;

		Instruction checkInstruction = instructionQueue[IFID];

		if(instructionQueue[IDEXE].getInstruction().equals("lw"))
		{
			String modifiedRegister = instructionQueue[IDEXE].getArguementAt(1);

			//Stall possible
			if(checkInstruction.getArguementAt(1) != null && checkInstruction.getArguementAt(2).equals(modifiedRegister))
			{
				stall = true;
			}
			else if(checkInstruction.getArguementAt(2) != null && checkInstruction.getArguementAt(3).equals(modifiedRegister))
			{
				stall = true;
			}
		}
		return stall;
	}
}