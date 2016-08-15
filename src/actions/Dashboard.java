package actions;

import java.awt.Component;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import simulation.Database;
import simulation.SimManager;

public class Dashboard implements MenuInterface{

	String role;
	
	public static List<Component> GetOverlay(List<Component> list)
	{
		list.add(Coex.btn("R", 880, 20, 20, 20, "dashboard_refresh"));	
		list.add(Coex.btn("D", 910, 20, 20, 20, "dashboard_return"));		
		list.add(Coex.btn("S", 940, 20, 20, 20, "dashboard_signout"));
		return list;
	}
	
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
		//String[] specific = new String[1];//{"Management"};
		List<String> specific = new ArrayList<String>();
			//Manager
			if(role.equals("manager"))
			{
				specific.add("Management");
				if(Database.hasNext("SELECT id FROM employment WHERE id_player = "+SimManager.player.GetID()+" AND position = 'CTO' AND offer = 'accepted'"))
				{
					specific.add("Project Bin");
					specific.add("Manage Workers");
				}
				specific.add("Job Offers");
			}
			if(role.equals("technologist"))
			{
				specific.add("Employment");
				specific.add("Job Offers");
			}
			if(role.equals("government"))
			{
				specific.add("Patent Office");
			}
			
		if(specific != null)
		for(int i = 0; i < specific.size(); i++)
			base_comp.add(Coex.btn(specific.get(i), 5,300 + (20 * i),200,20, null));

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
				if(!Database.hasNext("SELECT id FROM employment WHERE offer = 'accepted' AND id_player = " + SimManager.player.GetID()))
					SimManager.newMenu = new CreateCompany();//Management();
				if(Database.hasNext("SELECT id FROM employment WHERE offer = 'accepted' AND position = 'ceo' AND id_player = " + SimManager.player.GetID()))
					SimManager.newMenu = new CEO();
				if(Database.hasNext("SELECT id FROM employment WHERE offer = 'accepted' AND position = 'cto' AND id_player = " + SimManager.player.GetID()))
					SimManager.newMenu = new CTO();
				break;
			case "Project Bin":
				if(Database.hasNext("SELECT id FROM employment WHERE id_player = "+SimManager.player.GetID()+" AND position = 'CTO' AND offer = 'accepted'"))
					SimManager.newMenu = new ProjectBin();
				break;
			case "Manage Workers":
				if(Database.hasNext("SELECT id FROM employment WHERE id_player = "+SimManager.player.GetID()+" AND position = 'CTO' AND offer = 'accepted'"))
					SimManager.newMenu = new ManageWorkers();
				break;
			case "Job Offers":
				SimManager.newMenu = new JobOffers();
				break;
			case "Employment":
				SimManager.newMenu = new TechEmployment();
				//SimManager.newMenu = new Employment();
				break;
			case "Patent Office":
				if(Database.hasNext("SELECT id FROM profile WHERE id = "+SimManager.player.GetID()+" AND role = 'government'"))
					SimManager.newMenu = new PatentOffice();
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
