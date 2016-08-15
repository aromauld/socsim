package actions;

import java.awt.Button;
import java.awt.Component;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import components.Company;
import simulation.Database;
import simulation.SimManager;

public class CreateProject  implements MenuInterface {

	private Company org;
	Button submit;
	
	//Project Info
	TextField name;
	
	public CreateProject()
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
		
		submit = Coex.btn("Submit For Approval", 250, 450, 250, 25, "submit");
		base_comp.add(submit);
		
		name = Coex.txtF("", 130, 35, 200, 25);
		base_comp.add(name);
	}
	
	private boolean isCTO() //Returns true if the current player is still CTO
	{
		return Database.hasNext("SELECT id FROM employment WHERE offer = 'accepted' AND position = 'cto' AND id_player = " + SimManager.player.GetID() + " AND id_company = " + org.GetID());
	}

	
	
	public void action_evt(ActionEvent e) //Handle Button Presses
	{
		String action = e.getActionCommand();
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
				
			case "submit":
				Database.CreateProject(name.getText(), org.GetID());
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
	
	
	//Refresh the current page
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
