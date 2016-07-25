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
	}
	
	public void paint(Graphics g)
	{
		//This is here in order to delay initialization and solve UI bug
		if(sim == null)
			sim = new SimManager(this);	  
		
		//sim.Draw(g);

	}
}
