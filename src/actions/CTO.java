package actions;

import java.awt.Button;
import java.awt.Component;
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

public class CTO implements MenuInterface{

	public CTO()
	{
		SetBaseComp();
	}
	
	private Company org;
	private java.awt.List managers;
	private java.awt.List technologists_employable;
	private List<Integer> technologists_employable_id;
	
	private java.awt.List technologists_employed;
	private List<Integer> technologists_employed_id;
	
	private Button sendJobOffer;
	private Button fire;
	private Button reisgn;
	
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
		base_comp.add(Coex.lbl(org.GetName() + " (" + org.GetID() + ")" , 20,130 ,150,20)); 
		base_comp.add(Coex.lbl("Salary: " , 20,160 ,150,20)); 
		reisgn = Coex.btn("Resign as CTO", 50, 450, 100, 20, "resign");
		base_comp.add(reisgn);
		
		//Managers Working For Company
		base_comp.add(Coex.lbl("Managers" , 210,130 ,200,20)); 
		managers = new java.awt.List();
		managers.setBounds(210, 150, 200, 300);
		base_comp.add(managers);

		ResultSet rs = Database.Query("SELECT id_player, position FROM employment WHERE id_company = " + org.GetID() + " and offer = 'accepted' AND position != 'tech'");
		try {
			while (rs.next()) 
			{				
				managers.add(new Player(rs.getInt("id_player")).GetName() + " - " + rs.getString("position"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		//Employed Technologists
		base_comp.add(Coex.lbl("Employees" , 450,130 ,200,20)); 
		technologists_employed_id = new ArrayList<Integer>();
		technologists_employed = new java.awt.List();
		technologists_employed.setBounds(450,150, 200, 260);
		base_comp.add(technologists_employed);
		ResultSet rs_t = Database.Query("SELECT id_player FROM employment WHERE position = 'tech' AND offer = 'accepted' AND id_company = " + org.GetID());
		try {
			while(rs_t.next())
			{
				technologists_employed.add(new Player(rs_t.getInt("id_player")).GetName());
				technologists_employed_id.add(rs_t.getInt("id_player"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		base_comp.add(Coex.txtF("0", 450, 420, 200, 20));
		
		fire = Coex.btn("Fire", 450, 450, 200, 20, "fire");
		fire.setEnabled(false);
		base_comp.add(fire);
		
		//Job Offers
		base_comp.add(Coex.lbl("Technologists" , 690,130 ,200,20)); 
		technologists_employable_id = new ArrayList<Integer>();
		technologists_employable = new java.awt.List();
		technologists_employable.setBounds(690,150, 200, 260);
		base_comp.add(technologists_employable);
		ResultSet rs_te = Database.Query("SELECT id FROM profile WHERE role = 'technologist'");
		try {
			while(rs_te.next())
			{
				if(!Database.hasNext("SELECT id FROM employment WHERE id_player = " + rs_te.getInt("id") + " AND id_company = " + org.GetID()))
				{
					technologists_employable.add(new Player(rs_te.getInt("id")).GetName());
					technologists_employable_id.add(rs_te.getInt("id"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		base_comp.add(Coex.txtF("0", 690, 420, 200, 20));
		
		sendJobOffer = Coex.btn("Send Job Offer", 690, 450, 200, 20, "offer_job");
		sendJobOffer.setEnabled(false);
		base_comp.add(sendJobOffer);
	}

	private boolean isCTO() //Returns true if the current player is still CTO
	{
		return Database.hasNext("SELECT id FROM employment WHERE offer = 'accepted' AND position = 'cto' AND id_player = " + SimManager.player.GetID() + " AND id_company = " + org.GetID());
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
				
			case "offer_job":
				selected_id = technologists_employable_id.get(technologists_employable.getSelectedIndex());
				if(!Database.hasNext("SELECT id FROM employment WHERE id_player = " + selected_id + " AND id_company = " + org.GetID()) &&
						isCTO() &&
						Database.hasNext("SELECT id FROM profile WHERE role = 'technologist' and id = " + selected_id))
					Database.Employ(selected_id, org.GetID(), "tech", "pending");
				break;
			case "fire":
				selected_id = technologists_employed_id.get(technologists_employed.getSelectedIndex());
				if(Database.hasNext("SELECT id FROM employment WHERE id_player = " + selected_id + " AND id_company = " + org.GetID()) &&
						isCTO() &&
						Database.hasNext("SELECT id FROM profile WHERE role = 'technologist' and id = " + selected_id))
					Database.Update("DELETE FROM employment WHERE id_player = " + selected_id + " AND id_company = "+org.GetID()+";");
				break;
			default:
				break;
		}
	}
	
	public void item_evt(ItemEvent e) //Handle Item Events
	{
		String item = e.getItem().toString();
		if(technologists_employed.getSelectedIndex() >= 0) fire.setEnabled(true); else fire.setEnabled(false);
		if(technologists_employable.getSelectedIndex() >= 0) sendJobOffer.setEnabled(true); else sendJobOffer.setEnabled(false);
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
