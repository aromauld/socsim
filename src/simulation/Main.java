package simulation;

import java.applet.Applet;
//import java.sql.*;
import java.awt.*;


public class Main extends Applet{
	
	int WIDTH = 1000;
	int HEIGHT = 500;
	
	SimManager sim;
	
	public void start()
	{
	}
	
	public void init()
	{
		setSize(WIDTH, HEIGHT);
		setLayout(null);
		sim = new SimManager(this);	  
	}
	
	public void paint(Graphics g)
	{		
		//sim.Draw(g);

	}
}
