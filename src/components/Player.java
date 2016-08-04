package components;

import java.sql.ResultSet;
import java.sql.SQLException;

import simulation.Database;

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
		this.ID = ID;
		ResultSet rs = Database.Query("select name, role, status, money, org_id from profile WHERE id = '" + ID + "';");
		try {
			if(rs.next())
			{
				this.name = rs.getString("name");
				this.role = rs.getString("role");
				this.status = rs.getString("status");
				this.money = rs.getInt("money");
				this.org = rs.getInt("org_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		/*
		this.name = "test account";
		this.ID = ID;
		this.role = "manager"; // technologist government manager
		this.status = ""; // CEO CFO CTO
		org = -1;

		
		this.money = 100;
		*/
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
		Database.Update("UPDATE profile SET org_id = '" + this.org + "' where id = '" + GetID() + "';");
	}
	public void SetStatus(String status)//Sets the players status
	{
		this.status = status;
		Database.Update("UPDATE profile SET status = '" + status + "' where id = '" + GetID() + "';");
	}
	public void SetMoney(int money)
	{
		Database.Update("UPDATE profile SET money = '" + money + "' where id = '" + GetID() + "';");
		this.money = money;
	}
	public void Pay(int amount)
	{
		int money = GetMoney() + amount;
		SetMoney(money);
	}
	
}
