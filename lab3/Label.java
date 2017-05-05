/**
* Class Label
* @author Lara Luu
* 26 April 2017
* Stores the information for MIPS labels by name and address location 
*/
public class Label
{
	private String label;
	private int address;

	public Label(String lable, int address)
	{
		this.label = lable;
		this.address = address;
	}

	public String getLabel()
	{
		return this.label;
	}

	public int getAddress()
	{
		return this.address;
	}

}