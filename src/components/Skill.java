package components;

public class Skill {

	private String name;
	private float learnRate;
	
	
	public Skill()
	{
		FetchInfo();
	}
	
	public Skill(String name, float learnRate)
	{
		this.name = name;
		this.learnRate = learnRate;
	}
	
	private void FetchInfo()
	{
		name = "temp name";
		learnRate = 1f;
	}
	
	
	public String GetName()
	{
		return name;
	}
	public float GetLearnRate()
	{
		return learnRate;
	}
}
