package components;

import simulation.SimManager;

public class Company {

	int ID;
	String name;
	int ceo = 0;
	int cfo = 0;
	int cto = 0;
	
	
	public Company(int ID)//Existing company
	{
		GetInfo(ID);
	}
	public Company(String name)//New Company
	{
		SetInfo(name);
	}
	
	//Looks up the company ID in the database and sets values
	public void GetInfo(int ID)
	{
		this.ID = ID;
		this.name = "temp name";
		this.ceo = 0;
		this.cfo = 0;
		this.cto = 0;
	}
	
	
	//Sets up a new company in the database, assigning it an ID, and storing all necessary information
	private void SetInfo(String name)
	{
		this.name = name;
		this.ID = 0;
		this.ceo = SimManager.player.GetID();
		this.cfo = -1;
		this.cto = -1;
	}
	
	
	public String GetName()
	{
		return name;
	}
	public int GetID()
	{
		return ID;
	}
	
	public Player GetCEO()
	{
		return new Player(ceo);
	}
	public Player GetCFO()
	{
		if(cfo >= 0)
			return new Player(cfo);
		return null;
	}
	public Player GetCTO()
	{
		if(cto >= 0)
			return new Player(cto);
		return null;
	}
	
	public void SetCEO(Player player)
	{
		ceo = player.GetID();
	}
	public void SetCFO(Player player)
	{
		cfo = player.GetID();
	}
	public void SetCTO(Player player)
	{
		cto = player.GetID();
	}
}
