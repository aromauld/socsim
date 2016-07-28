package actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

import simulation.SimManager;


public class Projects  implements MenuInterface{
	
	private void SetBaseComp() //Sets up the base components
	{
		base_comp.add(Coex.btn("Management", 5, 50, 200, 20, null));
		base_comp.add(Coex.btn("Create New Project", 550, 100, 200, 30, "project_create"));
		
		base_comp.add(Coex.lbl("Projects", 210, 80, 80, 20));
		base_comp.add(Coex.lbl("Status", 380, 80, 200, 20));
	}
	
	public Projects()
	{
		SetBaseComp();
	}


	public void action_evt(ActionEvent e) //Handle Button Presses
	{
		String action = e.getActionCommand();
		switch (action)
		{
			case "Management":
				SimManager.newMenu = new Management();
				break;
			default:
				break;
		}
	}
	
	public void item_evt(ItemEvent e) //Handle Item Events
	{
		String item = e.getItem().toString();
		
		switch (item)
		{
			default :
				break;
		}
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
