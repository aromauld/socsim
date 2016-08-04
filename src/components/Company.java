package components;

import java.sql.ResultSet;
import java.sql.SQLException;

import simulation.Database;
import simulation.SimManager;

public class Company {

	int ID;
	String name;
	int ceo = 0;
	int cfo = 0;
	int cto = 0;
	int money = 0;
	
	
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
		ResultSet rs = Database.Query("select id, name, ceo, cfo, cto, money from company WHERE id = '" + ID + "';");
		try {
			if(rs.next())
			{
				this.name = rs.getString("name");
				this.ceo = rs.getInt("ceo");
				this.cfo = rs.getInt("cfo");
				this.cto = rs.getInt("cto");
				this.money = rs.getInt("money");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/*
		this.ID = ID;
		this.name = "temp name";
		this.ceo = 0;
		this.cfo = 0;
		this.cto = 0;
		*/
	}
	
	
	//Sets up a new company in the database, assigning it an ID, and storing all necessary information
	private void SetInfo(String name)
	{
		int maxID = 0;
		ResultSet rs = Database.Query("SELECT MAX(id) as maxid from company");
		try {
			if(rs.next())
			{
				maxID = rs.getInt("maxid") + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.name = name;
		this.ID = maxID;
		this.ceo = SimManager.player.GetID();
		this.cfo = -1;
		this.cto = -1;
		this.money = 0;
		
		
		Database.Update("INSERT INTO company " + 
						"VALUES (" + ID + ", '" + name + "', " + this.ceo + "," + this.cfo + "," + this.cto + "," + money + ");");
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
		if(ceo >= 0)
			return new Player(ceo);
		return null;
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
	public int GetMoney()
	{
		return money;
	}
	
	public void SetCEO(Player player)
	{
		if(player != null)
			ceo = player.GetID();
		else
			ceo = -1;
		Database.Update("UPDATE company SET ceo = '" + ceo + "' where id = '" + GetID() + "';");
	}
	public void SetCFO(Player player)
	{
		if(player != null)
			cfo = player.GetID();
		else
			cfo = -1;
		Database.Update("UPDATE company SET cfo = '" + cfo + "' where id = '" + GetID() + "';");
	}
	public void SetCTO(Player player)
	{
		if(player != null)
			cto = player.GetID();
		else
			cto = -1;
		Database.Update("UPDATE company SET cto = '" + cto + "' where id = '" + GetID() + "';");
	}
	public void SetMoney(int money)
	{
		Database.Update("UPDATE company SET money = '" + money + "' where id = '" + GetID() + "';");
		this.money = money;
	}
	public void Pay(int amount)
	{
		int money = GetMoney() + amount;
		SetMoney(money);
	}
}
