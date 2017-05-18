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
		userDisplay.println("pushInstruction");

		Instruction dequeuedInstruction; // returns the instruction to be run in the processor


		dequeuedInstruction = instructionQueue[EXEMEM]; // 3rd instruction b/c run in exe stage

		// Shift all the instructions over in the processor
		instructionQueue[MEMWB] = instructionQueue[EXEMEM];
		//userDisplay.println("MEMWB " + instructionQueue[MEMWB].getInstruction());

		instructionQueue[EXEMEM] = instructionQueue[IDEXE];
		//userDisplay.println("EXEMEM " + instructionQueue[EXEMEM].getInstruction());

		if(instruction.getInstruction().equals("stall"))
		{
			//Entered in a stall, have to swap indexes 0 and 1
			instructionQueue[IDEXE] = instruction;
			//userDisplay.println("IDEXE " + instructionQueue[IDEXE].getInstruction());

		}
		else
		{
			instructionQueue[IDEXE] = instructionQueue[IFID];
			//userDisplay.println("IDEXE " + instructionQueue[IDEXE].getInstruction());

			instructionQueue[IFID] = instruction;
			//userDisplay.println("IFID " + instructionQueue[IFID].getInstruction());
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
			userDisplay.println("Checking for stall: ");

			String modifiedRegister = instructionQueue[IDEXE].getArguementAt(1);

			userDisplay.println("modified reg: " + modifiedRegister);
			userDisplay.println("check reg 1: " + checkInstruction.getArguementAt(2));
			userDisplay.println("check reg 2: " + checkInstruction.getArguementAt(3));

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
		//System.out.println(stall ? "true":"false");
		return stall;
	}
}