package components;

public class Player {

	int ID;
	String name;
	public String role;
	
	//Employment and Finances
	//Company org;
	int money;
	//Stock[] stock;
		
	
	
	public Player(int ID)
	{
		FetchInfo(ID);		
	}
	
	
	public String GetName()
	{
		return name;
	}
	public int GetID()
	{
		return ID;
	}
	public String GetRole()
	{
		return role;
	}
	public int GetMoney()
	{
		return money;
	}
	
	
	//Looks up the player ID in the database and assigns values to identifiers
	private void FetchInfo(int ID)
	{
		this.name = "test account";
		this.ID = ID;
		this.role = "government";
		
		this.money = 100;
	}
}
