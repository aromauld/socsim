package simulation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import actions.Dashboard;
import components.Company;
import components.Player;

public class Database {

	public Database()
	{
		
	}
	
	private static Connection con;
	public void Connect(String DB_URL, String USER, String PASS) //Connects to given database
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting...");
			this.con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected");		
		} 
		catch (SQLException err){
			System.out.println(err.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ResultSet Query(String query) //Returns result set of query
	{
		if(con == null)
			return null;
		
		try
		{
			java.sql.Statement stmt  = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		}
		catch (Exception e)
		{
			return null;
		}
		
	}
	
	public static void Update(String u) //Updates the database
	{
		if(con != null)
		{
			try {
				java.sql.Statement stmt = con.createStatement();
				stmt.executeUpdate(u);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	
	
	public static boolean hasNext(String q) //Returns true if the given row exists
	{
		ResultSet rs = Query(q);
		try {
			if(rs.next())
			{
				return true;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		return false;
	}
	
	private static int GetMaxID(String table) //returns the max (id) of the given table
	{
		int maxID = -1;
		ResultSet rs = Database.Query("SELECT MAX(id) as maxid from " + table);
		try {
			if(rs.next())
			{
				maxID = rs.getInt("maxid") + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxID;
	}
	
	public static void Employ(int playerID, int companyID, String position, String offer) //Adds a row to the 'employment' table
	{
		/*
		String q = String.format("SELECT id FROM employment WHERE id_player = %2d AND id_company = %2d AND position = '%s' and offer = 'pending');",
				playerID, companyID, position);
		ResultSet rs = Query(q);
		try {
			if(!rs.next())
			{
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		GetMaxID("employment");
		String u = String.format("INSERT INTO employment VALUES( %2d, %2d, %2d, '%s', '%s' );", 
				GetMaxID("employment"), playerID, companyID, position, offer);
		Update(u);
	}
	
	
	
	public static void SendJobOffer(int playerID, int orgID, String position)
	{
		int maxID = 0;
		ResultSet rs = Database.Query("SELECT MAX(id) as maxid from job_offers");
		try {
			if(rs.next())
			{
				maxID = rs.getInt("maxid") + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.Update("INSERT INTO job_offers " + 
				"VALUES (" + maxID + ", '" + playerID + "', " + orgID + ",'" + position + "');");
	}
	
	public static List<Player> GetPlayers(String query)
	{
		List<Player> players = new ArrayList<Player>();
		try {
			ResultSet rs = Database.Query(query);
			if(rs != null)
			while ( rs.next() ) {
				players.add(new Player((int)rs.getObject("id")));
			}
			return players;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
		java.sql.Statement stmt  = con.createStatement();
		//String query = "select id from profile ;";
		String query = "select name, password from profile WHERE name = 'Mark Suchman' and password = 'admin';";
		ResultSet rs = stmt.executeQuery(query);
		//System.out.println(rs.getObject(2));//.getString(0));
		
		
		while ( rs.next() ) {
	        int numColumns = rs.getMetaData().getColumnCount();
	        for ( int i = 1 ; i <= numColumns ; i++ ) {
	           // Column numbers start at 1.
	           // Also there are many methods on the result set to return
	           //  the column as a particular type. Refer to the Sun documentation
	           //  for the list of valid conversions.
	           System.out.println( "COLUMN " + i + " = " + rs.getObject(i) );
	        }
	    }
    */
}
