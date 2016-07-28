package actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

import simulation.SimManager;

public interface MenuInterface {
	List<Component> base_comp = new ArrayList<Component>();
	List<Component> add_comp = new ArrayList<Component>();
	
	public List<Component> GetCurrentUI();
	public void action_evt(ActionEvent e);
	public void item_evt(ItemEvent e);
}




//General Copy-Paste layout for a new sub-menu
/*

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import simulation.SimManager;

 implements MenuInterface

	private void SetBaseComp() //Sets up the base components
	{
		base_comp.add(Coex.btn("Dashboard", 5, 50, 200, 20, null));
	}


	public void action_evt(ActionEvent e) //Handle Button Presses
	{
		String action = e.getActionCommand();
		switch (action)
		{
			case "Dashboard":
				SimManager.newMenu = new Dashboard();
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
*/
