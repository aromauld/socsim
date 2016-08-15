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

public class ManageWorkers implements MenuInterface {

	private Company org;
	private java.awt.List technologists_employed;
	private List<Integer> technologists_employed_id;
	
	private Choice projectList;
	private Button addProject;
	
	private Label name_label;
	
	public ManageWorkers()
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
		
		base_comp.add(Coex.lbl("Employees" , 50,50 ,80,20)); 
		technologists_employed_id = new ArrayList<Integer>();
		technologists_employed = new java.awt.List();
		technologists_employed.setBounds(50,75, 200, 350);
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
		
		name_label = new Label("");
		name_label.setBounds(275, 75, 350, 20);
		base_comp.add(name_label);
	}

	private boolean isCTO() //Returns true if the current player is still CTO
	{
		return Database.hasNext("SELECT id FROM employment WHERE offer = 'accepted' AND position = 'cto' AND id_player = " + SimManager.player.GetID() + " AND id_company = " + org.GetID());
	}
	
	private void UpdateEmployee(int item)
	{
		if(item >= 0)
		{
			int p = technologists_employed_id.get(technologists_employed.getSelectedIndex());
			name_label.setText(technologists_employed.getSelectedItem() + " is working on (/) projects: ");
			
			//Employee's Projects
			add_comp.clear();
			for(int i = 0; i < 5; i++)
			{
				add_comp.add(Coex.lbl("Project " + i, 275, 100 + (20 * i), 350, 20));
			}
			
			//Project List
			projectList = new Choice();
			projectList.setBounds(275, 405, 250, 20);

			ResultSet rs = Database.Query("SELECT id, name, id_company FROM project WHERE status = 'patent pending'");
			try {
				while(rs.next())
				{
					projectList.add(rs.getString("name"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			add_comp.add(projectList);
			
			//Add Project Button
			addProject = Coex.btn("Add To Jobs", 530, 405, 100, 20, "add_project");
			add_comp.add(addProject);
			
			SimManager._UpdateUI = true;
		}
		else
			name_label.setText("");
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
			case "add_project":
				if(isCTO())
					UpdateEmployee(technologists_employed.getSelectedIndex());
				break;
			default:
				break;
		}
	}
	
	public void item_evt(ItemEvent e) //Handle Item Events
	{
		if(e.getSource().equals(technologists_employed))
			UpdateEmployee(technologists_employed.getSelectedIndex());
		
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
