package components;

public class Project {

	String name;
	String desc;
	int ID;
	
	public Project()
	{
	}
	public Project(String name, String desc)
	{
		this.name = name;
		this.desc = desc;
	}
	
	public String GetName()
	{
		return name;
	}
	public String GetDesc()
	{
		return desc;
	}
	public int GetID()
	{
		return ID;
	}
	
}
