package actions;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import components.Company;
import components.Player;
import simulation.Database;
import simulation.SimManager;

public class CEO implements MenuInterface{

	private Company org;
	private java.awt.List managers;
	private List<Integer> employmentID = new ArrayList<Integer>();
	private Button fire;
	
	private java.awt.List managers_for_hire;
	private List<Integer> managers_for_hire_id = new ArrayList<Integer>();
	private Button offer_cfo;
	private Button offer_cto;
	private Button offer_manager;
	
	public CEO()
	{
		SetBaseComp();
	}
	
	private void SetBaseComp() //Sets up the base components
	{
		base_comp.clear();
		base_comp = Dashboard.GetOverlay(base_comp);
		
		//Get company
		ResultSet rs_org = Database.Query("SELECT id_company FROM employment WHERE id_player = " + SimManager.player.GetID() + " and offer = 'accepted'");
		try {
			if(rs_org.next())
				org = new Company(rs_org.getInt("id_company"));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		
		//Company Name and ID
		base_comp.add(Coex.lbl(org.GetName() + " (" + org.GetID() + ")" , 210,100 ,200,20)); 
		
		//Managers Working For Company
		managers = new java.awt.List();
		managers.setBounds(210, 130, 200, 300);
		base_comp.add(managers);
		ResultSet rs = Database.Query("SELECT id, id_player, position FROM employment WHERE id_company = " + org.GetID() + " and offer = 'accepted'");
		try {
			while (rs.next()) 
			{				
				employmentID.add(rs.getInt("id"));
				managers.add(new Player(rs.getInt("id_player")).GetName() + " - " + rs.getString("position"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		fire = Coex.btn("Fire", 210, 430, 200, 20, "fire_manager");
		fire.setEnabled(false);
		base_comp.add(fire);
		
		
		//Managers for hire
		base_comp.add(Coex.lbl("Available Managers" , 450,100 ,200,20)); 
		managers_for_hire = new java.awt.List();
		managers_for_hire.setBounds(450, 130, 200, 320);
		base_comp.add(managers_for_hire);
		rs = Database.Query("SELECT id, name FROM profile WHERE role = 'manager'");
		try {
			while(rs.next())
			{
				if(!Database.hasNext("SELECT id FROM employment WHERE id_player = " + rs.getInt("id") + " and id_company = " + org.GetID()))
				{
					managers_for_hire.add(rs.getString("name"));
					managers_for_hire_id.add(rs.getInt("id"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		base_comp.add(Coex.lbl("Offer Job As" , 660,100 ,200,20)); 
		offer_cfo = Coex.btn("CFO", 660, 130, 200, 20, "offer_cfo"); offer_cfo.setEnabled(false); base_comp.add(offer_cfo);
		offer_cto = Coex.btn("CTO", 660, 150, 200, 20, "offer_cto"); offer_cto.setEnabled(false); base_comp.add(offer_cto);
		offer_manager = Coex.btn("Manager", 660, 170, 200, 20, "offer_manager"); offer_manager.setEnabled(false); base_comp.add(offer_manager);
	}
	
	private boolean isCEO() //Returns true if the current player is still CEO
	{
		return Database.hasNext("SELECT id FROM employment WHERE offer = 'accepted' AND position = 'ceo' AND id_player = " + SimManager.player.GetID() + " AND id_company = " + org.GetID());
	}
	
	public void action_evt(ActionEvent e) //Handle Button Presses
	{
		String action = e.getActionCommand();
		int selected_id;
		switch (action)
		{
			
			case "dashboard_return":
				SimManager.newMenu = new Dashboard();
				break;
			case "dashboard_refresh":
				Refresh();
				break;
			case "dashboard_signout":
				SimManager.player = null;
				SimManager.newMenu = new Login();
				break;
				
			case "fire_manager":
				selected_id = employmentID.get(managers.getSelectedIndex());
				if(Database.hasNext("SELECT id FROM employment WHERE id_company = " + org.GetID() + " and offer = 'accepted' AND id = " + selected_id) &&
						isCEO())
					Database.Update("DELETE FROM employment WHERE id = '" + selected_id + "';");
				break;
				
			case "offer_cfo":
				selected_id = managers_for_hire_id.get(managers_for_hire.getSelectedIndex());
				if(!Database.hasNext("SELECT id FROM employment WHERE id_company = " + org.GetID() + " AND id_player = " + selected_id) &&
						isCEO() &&
						Database.hasNext("SELECT id FROM profile WHERE role = 'manager' and id = " + selected_id))
					Database.Employ(selected_id, org.GetID(), "CFO", "pending");
				break;
			case "offer_cto":
				selected_id = managers_for_hire_id.get(managers_for_hire.getSelectedIndex());
				if(!Database.hasNext("SELECT id FROM employment WHERE id_company = " + org.GetID() + " AND id_player = " + selected_id) &&
						isCEO() &&
						Database.hasNext("SELECT id FROM profile WHERE role = 'manager' and id = " + selected_id))
					Database.Employ(selected_id, org.GetID(), "CTO", "pending");
				break;
			case "offer_manager":
				selected_id = managers_for_hire_id.get(managers_for_hire.getSelectedIndex());
				if(!Database.hasNext("SELECT id FROM employment WHERE id_company = " + org.GetID() + " AND id_player = " + selected_id) &&
						isCEO() &&
						Database.hasNext("SELECT id FROM profile WHERE role = 'manager' and id = " + selected_id))
					Database.Employ(selected_id, org.GetID(), "manager", "pending");
				break;
				
			default:
				break;
		}
	}
	
	public void item_evt(ItemEvent e) //Handle Item Events
	{
		if(managers.getSelectedIndex() >= 0) fire.setEnabled(true); else fire.setEnabled(false);
		if(managers_for_hire.getSelectedIndex() >= 0)
		{
			offer_cfo.setEnabled(true);
			offer_cto.setEnabled(true);
			offer_manager.setEnabled(true);
		}
		else
		{
			offer_cfo.setEnabled(false);
			offer_cto.setEnabled(false);
			offer_manager.setEnabled(false);
		}
		
		String item = e.getItem().toString();
		
		switch (item)
		{
			default :
				break;
		}
	}
	
	
	private void Refresh()
	{
		SetBaseComp();
		SimManager._UpdateUI =  true;
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
