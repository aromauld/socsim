package actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;


import simulation.SimManager;

public class Dashboard implements MenuInterface{

	
	
	private void SetBaseComp()
	{
		base_comp.add(Coex.btn("Logout", 5, 50, 200, 20, "logout"));
		base_comp.add(Coex.lbl(SimManager.player.GetName() + " (" + SimManager.player.GetID() + ") " + SimManager.player.GetRole(), 800,50,200,20));
		
		//General Buttons (All Roles)
		String[] gerneral = {"Transactions", "Voting", "Policies", "Stock"};
		for(int i = 0; i < gerneral.length; i++)
			base_comp.add(Coex.btn(gerneral[i], 5,150 + (20 * i),200,20, null));

	}
	public Dashboard()
	{
		SetBaseComp();
	}
	
	

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
		}

	}

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
