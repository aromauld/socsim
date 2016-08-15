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
import simulation.Database;
import simulation.SimManager;

public class ProjectBin implements MenuInterface {

	Company org;
	Choice projectList;
	List<Integer> projectList_ID;
	
	int selectedProject = -1;
	
	//Project General Stats
	
	//Project Goals
	java.awt.List perfGoals;
	Label performance_progress;
	Label performance_goal;
	
	public ProjectBin()
	{
		SetBaseComp();
	}
	
	private boolean isCTO() //Returns true if the current player is still CTO
	{
		return Database.hasNext("SELECT id FROM employment WHERE offer = 'accepted' AND position = 'cto' AND id_player = " + SimManager.player.GetID() + " AND id_company = " + org.GetID());
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
				
		base_comp.add(Coex.btn("Create New Project", 275, 40, 150, 25, "create_project"));
		
		//Project List
		projectList_ID = new ArrayList<Integer>();
		projectList = new Choice();
		projectList.setBounds(30, 40, 200, 25);
		ResultSet rs = Database.Query("SELECT id, name FROM project WHERE status = 'approved' AND id_company = " + org.GetID());
		try {
			while(rs.next())
			{
				projectList.add(rs.getString("name"));
				projectList_ID.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		base_comp.add(projectList);
		
		
		
		
		
		perfGoals = new java.awt.List();
		perfGoals.setBounds(450, 130, 150, 250);
		ResultSet rs_c = Database.Query("SELECT name FROM challenges");
		try {
			while(rs_c.next())
			{
				perfGoals.add(rs_c.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		base_comp.add(perfGoals);
		
		performance_progress = Coex.lbl("", 625, 130, 200, 20);
		base_comp.add(performance_progress);
		performance_goal = Coex.lbl("", 625, 150, 200, 20);
		base_comp.add(performance_goal);
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
				
			case "create_project":
				SimManager.newMenu = new CreateProject();
				break;
			
			default:
				break;
		}
	}
	
	public void item_evt(ItemEvent e) //Handle Item Events
	{
		if(projectList.getSelectedIndex() >= 0)
			selectedProject = projectList_ID.get(projectList.getSelectedIndex());
		else
			selectedProject = -1;
		
		if(perfGoals.getSelectedIndex() >= 0  &&  selectedProject >= 0)
		{
			float cur = -5f;;
			float goal = -5f;
			String pull_progress = "challenge_current_" + perfGoals.getSelectedItem();
			String pull_goal = "challenge_goal_" + perfGoals.getSelectedItem();
			ResultSet rs = Database.Query("SELECT "+pull_progress+", "+pull_goal+" FROM project WHERE id = " + selectedProject);
			try {
				while ( rs.next() ) {
					
			        rs.getMetaData();
			        cur = (float) rs.getObject(1);
					goal = (float) rs.getObject(2);
			    }
			} catch (SQLException er) {
				er.printStackTrace();
			}
			performance_progress.setText("Current Progress: " + cur);
			performance_goal.setText("Goal: " + goal);
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
