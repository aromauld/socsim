package actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import components.Company;
import simulation.Database;
import simulation.SimManager;
import java.awt.*;

public class CreateCompany implements MenuInterface{
	
	public CreateCompany()
	{
		SetBaseComp();
	}
	
	TextField companyName;
	
	private void SetBaseComp() //Sets up the base components
	{
		base_comp.add(Coex.btn("Dashboard", 5, 50, 200, 20, null));
		
		companyName = Coex.txtF("", 210, 70, 200, 20);
		base_comp.add(companyName);
		base_comp.add(Coex.btn("Create Company", 210, 90, 200, 20, "company_create"));
	}


	public void action_evt(ActionEvent e) //Handle Button Presses
	{
		String action = e.getActionCommand();
		switch (action)
		{
			case "Dashboard":
				SimManager.newMenu = new Dashboard();
				break;
				
				
			case "company_create":
				if(!Database.hasNext("SELECT id FROM employment WHERE offer = 'accepted' AND id_player = "  + SimManager.player.GetID()))
				{
						Company org = new Company(companyName.getText());
						SimManager.player.SetOrg(org);
						SimManager.player.SetStatus("CEO");
						Database.Employ(SimManager.player.GetID(), org.GetID(), "CEO", "accepted");
						SimManager.newMenu = new CEO();
				}
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
