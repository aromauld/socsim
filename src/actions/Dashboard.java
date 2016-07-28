package actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;


import simulation.SimManager;

public class Dashboard implements MenuInterface{

	String role;
	
	private void SetBaseComp() //Sets up the base components
	{
		this.role = SimManager.player.GetRole();
		
		base_comp.add(Coex.btn("Logout", 5, 50, 200, 20, "logout"));
		base_comp.add(Coex.lbl(SimManager.player.GetName() + " (" + SimManager.player.GetID() + ") " + SimManager.player.GetRole(), 800,50,200,20));
		base_comp.add(Coex.lbl("$" + SimManager.player.GetMoney(), 800,70,200,20));
		
		//General Buttons (All Roles)
		String[] gerneral = {"Transactions", "Voting", "Policies", "Stock"};
		for(int i = 0; i < gerneral.length; i++)
			base_comp.add(Coex.btn(gerneral[i], 5,150 + (20 * i),200,20, null));
		
		//(Role Specific)
		String[] specific = new String[1];//{"Management"};
			//Manager
			if(role == "manager")
				specific[0] = "Management";
			if(role == "technologist")
				specific[0] = "Employment";
			
		if(specific != null)
		for(int i = 0; i < specific.length; i++)
			base_comp.add(Coex.btn(specific[i], 5,300 + (20 * i),200,20, null));

	}
	public Dashboard()
	{
		SetBaseComp();
	}
	
	
	

	//Handle button presses
	public void action_evt(ActionEvent e) {
		String action = e.getActionCommand();
		switch (action)
		{
			case "logout":
				SimManager.player = null;
				SimManager.newMenu = new Login();
				break;
			case "Transactions":
				SimManager.newMenu = new Transactions();
				break;
			case "Management":
				SimManager.newMenu = new Management();
				break;
			case "Employment":
				SimManager.newMenu = new Employment();
				break;
		}

	}
	//Handle Item Events
	public void item_evt(ItemEvent e) {
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
