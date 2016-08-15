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
	public float GetPlayerSkill(int ID)
	{
		return 1f;
	}
	
	public void SetLearnRate(float learnRate)
	{
		this.learnRate = learnRate;
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
