package simulation;

import java.sql.*;
import actions.*;
import actions.MenuInterface;
import components.*;

import java.applet.Applet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SimManager implements ActionListener, ItemListener {
	
	public static MenuInterface newMenu;
	public static boolean _UpdateUI = false;
	Applet app;
	private MenuInterface cur_menu;
	private List<Component> app_comp = new ArrayList<Component>();

	public static Player player;
	public static Calendar cal;
	
	String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	String DB_URL = "db.college.brown.edu";
	String USER = "socsim";
	String PASS = "$0cs!mUs3r";
	Connection conn = null;
	Statement stmt = null;
	
	
	
	public SimManager(Applet app) 
	{
		Coex.font = new Font("Tahoma", Font.PLAIN, 12);
		this.app = app;
		
		UpdateMenu(new Login());
		newMenu = cur_menu;
		
		/*
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			conn.close();
		}catch(SQLException se)
		{
			se.printStackTrace();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		*/
	}
	
	
	//Fetches the current in game date from the database
	public static void GetCurDate()
	{
		cal = GregorianCalendar.getInstance();
		int y = 2016;
		int m = 7;
		int d = 21;
		cal.set(y,m - 1,d);
	}
	
	


	public void itemStateChanged(ItemEvent e) {
		cur_menu.item_evt(e);
		if(_UpdateUI)
			UpdateUI();
		
	}


	public void actionPerformed(ActionEvent e) {
		cur_menu.action_evt(e);
		if(newMenu != cur_menu)
			UpdateMenu(newMenu);

	}
	
	
	
	
	private void UpdateUI()
	{
		if(app_comp != null)
			for (Component c : app_comp)
			{
				if(c.getClass().equals(Button.class))
					((Button)c).removeActionListener(this);	
				if(c.getClass().equals(Choice.class))
					((Choice)c).removeItemListener(this);
			}
		app.removeAll();
		app_comp = cur_menu.GetCurrentUI();
		if(app_comp != null)
		for (Component c : app_comp)
		{
			app.add(c);
			if(c.getClass().equals(Button.class))
				((Button)c).addActionListener(this);	
			if(c.getClass().equals(Choice.class))
				((Choice)c).addItemListener(this);
		}
		_UpdateUI = false;
	}
	
	
	
	//Switch to the new menu and update the UI
	private void UpdateMenu(MenuInterface menu)
	{
		cur_menu = menu;
		if(app_comp != null)
			for (Component c : app_comp)
			{
				if(c.getClass().equals(Button.class))
					((Button)c).removeActionListener(this);	
				if(c.getClass().equals(Choice.class))
					((Choice)c).removeItemListener(this);
			}
		app.removeAll();
		app_comp = cur_menu.GetCurrentUI();
		if(app_comp != null)
		for (Component c : app_comp)
		{
			app.add(c);
			if(c.getClass().equals(Button.class))
				((Button)c).addActionListener(this);	
			if(c.getClass().equals(Choice.class))
				((Choice)c).addItemListener(this);
		}
	}
}
