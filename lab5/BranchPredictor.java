import java.util.*;
import java.lang.*;

class BranchPredictor
{
	int correctPredictions;
	int numPredictions;
	int[] ghr; //global history register
	int[] lbp; //local branch predictor

	public BranchPredictor(int arrSize)
	{
		this.ghr = new int[arrSize];
		this.lbp = new int[(int) Math.pow(2, arrSize)];
		correctPredictions = 0;
		numPredictions = 0;

		// Initialize both arrays to zero
		for(int i = 0; i < ghr.length; ++i)
		{
			ghr[i] = 0;
		}

		for(int i = 0; i < lbp.length; ++i)
		{
			lbp[i] = 0;
		}
	}

	public boolean makePrediction()
	{
		++ numPredictions;
		return (lbp[ghrToInt()] >=2);
	}

	public void updatePrediction(boolean taken, boolean res)
	{


		if(taken == res)
		{
			++ correctPredictions;
		}

		//Update the lbp
		updateLbp(res);

		// Update the ghr
		if(res)
		{
			enqueue(1);
		}

		else
		{
			enqueue(0);
		}
		
	}

	public void printResults()
	{
		System.out.println();
		float accuracy = getAccuracy();
		System.out.printf("accuracy %.2f%% (%d correct predictions, %d predictions)\n\n", accuracy, correctPredictions, numPredictions);
	}


	private float getAccuracy()
	{
		return (correctPredictions / (float)numPredictions * 100);
	}

	private void updateLbp(boolean taken)
	{
		int idx = ghrToInt();
		if(taken)
		{
			if(lbp[idx] <= 2)
			{
				lbp[idx] ++;
			}
		}
		else
		{
			if(lbp[idx] > 0)
			{
				lbp[idx] --;
			}
		}
	}

	/* True if taken, false if not */
	private boolean isCorrect(boolean taken, boolean res)
	{
		return (taken == res);
	}

	private void enqueue(int insert)
	{
		for(int i = ghr.length - 1; i > 0; --i)
		{
			ghr[i] = ghr[i-1];
		}
		ghr[0] = insert;
	}

	private int ghrToInt()
	{
		int ans = 0;

		for(int i = 0; i < ghr.length; ++i)
		{
			//System.out.println("GHR[i]: " + ghr[i]);
			if(ghr[i] == 1)
			{
				ans = ans + (int) Math.pow(2, i);
			}
		}
		return ans;
	}

	private void printghr()
	{
		System.out.print("GHR: ");
		for(int i = ghr.length - 1; i >= 0; --i)
		{
			System.out.print(ghr[i]);
		}
		System.out.println();
	}

		private void printlbp()
	{
		System.out.print("LBP: [");
		for(int i = lbp.length - 1; i >= 0; --i)
		{
			System.out.print(lbp[i] + ", ");
		}
		System.out.print("]");
		System.out.println();
	}


}