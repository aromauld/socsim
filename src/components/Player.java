package components;

public class Player {

	int ID;
	String name;
	String role;
	String status;
	
	//Employment and Finances
	int org;
	int money;
	//Stock[] stock;
		
	
	
	public Player(int ID)
	{
		FetchInfo(ID);		
	}
	
	private void FetchInfo(int ID) //Looks up the player ID in the database and assigns values to identifiers
	{
		this.name = "test account";
		this.ID = ID;
		this.role = "manager"; // technologist government manager
		this.status = ""; // CEO CFO CTO
		org = -1;

		
		this.money = 100;
	}
	
	public String GetName() //Returns name
	{
		return name;
	}
	public int GetID() //Returns ID
	{
		return ID;
	}
	public String GetRole() //Returns role
	{
		return role;
	}
	public int GetMoney() //Returns money
	{
		return money;
	}	
	public Company GetOrg() //Returns player's company
	{
		if(org >= 0)
			return new Company(org);
		return null;
	}
	public String GetStatus() //Returns status
	{
		return this.status;
	}
	
	public void SetOrg(Company org) //Sets the player's company
	{
		if(org != null)
			this.org = org.GetID();
		else
			this.org = -1;
	}
	public void SetStatus(String status)//Sets the players status
	{
		this.status = status;
	}
	
	
}
