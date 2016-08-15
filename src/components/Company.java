package components;

import java.sql.ResultSet;
import java.sql.SQLException;

import simulation.Database;
import simulation.SimManager;

public class Company {

	int ID;
	String name;
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
		ResultSet rs = Database.Query("select id, name, money from company WHERE id = '" + ID + "';");
		try {
			if(rs.next())
			{
				this.name = rs.getString("name");
				this.money = rs.getInt("money");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
		this.money = 0;
		
		
		Database.Update("INSERT INTO company " + 
						"VALUES (" + ID + ", '" + name + "," + money + ");");
	}
	
	
	public String GetName()
	{
		return name;
	}
	public int GetID()
	{
		return ID;
	}
	

	public int GetMoney()
	{
		return money;
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
