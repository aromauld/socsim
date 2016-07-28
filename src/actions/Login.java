package actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import components.Player;
import simulation.SimManager;


public class Login implements MenuInterface{
	
	
	
	private TextField username;
	private TextField password;
	private Label ErrorMsg;
	
	
	private void SetBaseComp()//Sets up the base components
	{
		username = Coex.txtF("username" ,5,30,200,20);
		password = Coex.txtF("password" ,5,55,200,20);
		ErrorMsg = Coex.lbl("", 5, 110, 200, 50);
		base_comp.add(username);
		base_comp.add(password);
		base_comp.add(Coex.btn("Login", 5,80,200,20, "login"));
		base_comp.add(ErrorMsg);
	}
	
	public Login()
	{
		SetBaseComp();	
	}
	
	
	
	//Looks up the username and password in the database and returns the matching player
	private Player SignIn(String username, String password)
	{
		if(username.equals("username")  &&  password.equals("password"))
			return new Player(0);
		ErrorMsg.setText("Error Msg");
		return null;
	}
	
	
		

	//Handle Button Presses
	public void action_evt(ActionEvent e) {
		String action = e.getActionCommand();
		if(action == "login")
		{
			SimManager.player = SignIn(username.getText(), password.getText());
			if(SimManager.player != null)
			{
				SimManager.newMenu = new Dashboard();
				SimManager.GetCurDate();
			}
		}
	}
	//Handle Item Events
	public void item_evt(ItemEvent e)
	{
		
	}
	
	
	
	
	//Return the current components
	private List<Component> base_comp = new ArrayList<Component>();
	private List<Component> add_comp = new ArrayList<Component>();
	public List<Component> GetCurrentUI() {
		List<Component> list = new ArrayList<Component>(base_comp);
		list.addAll(add_comp);
		return list;
	}

	
	
	
}
