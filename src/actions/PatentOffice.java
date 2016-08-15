package actions;

import java.awt.Button;
import java.awt.Component;
import java.awt.Label;
import java.awt.TextField;
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

public class PatentOffice implements MenuInterface {

	java.awt.List projects;
	List<Integer> projects_id;
	List<Integer> project_orgID;
	
	Button approve;
	Button deny;
	
	java.awt.List perfGoals;
	TextField relevance;
	List<Integer> goal_relevance;
	
	Label lbl_scale;
	Label lbl_goal;
	Label lbl_name;
	Label lbl_company;
	Label lbl_Desc;
	Label lbl_chlg;
	Label lbl_Funding;
	
	public PatentOffice()
	{
		SetBaseComp();
	}
	
	private void SetBaseComp() //Sets up the base components
	{
		base_comp.clear();
		base_comp = Dashboard.GetOverlay(base_comp);
		
		//Get Projects
		projects_id = new ArrayList<Integer>();
		project_orgID = new ArrayList<Integer>();
		projects = new java.awt.List();
		projects.setBounds(20, 100, 150, 400);
		ResultSet rs = Database.Query("SELECT id, name, id_company FROM project WHERE status = 'patent pending'");
		try {
			while(rs.next())
			{
				projects.add(rs.getString("name"));
				projects_id.add(rs.getInt("id"));
				project_orgID.add(rs.getInt("id_company"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		base_comp.add(projects);
		base_comp.add(Coex.lbl("Projects", 20, 75, 150, 20));
		
		approve = Coex.btn("Approve", 840, 440, 80, 25, "approve"); approve.setEnabled(false);	
		base_comp.add(approve);
		
		deny = Coex.btn("Deny", 840, 470, 80, 25, "deny"); deny.setEnabled(false);
		base_comp.add(deny);
		
		
		relevance = Coex.txtF("", 900, 350, 50, 25);
		lbl_scale = Coex.lbl("Relevance (0-10)", 800, 350, 100, 20);
		lbl_goal = Coex.lbl("Goals", 800, 105, 140, 20);
		lbl_name = Coex.lbl("Project: ", 200, 60, 250, 20);
		lbl_company = Coex.lbl("Company: ", 550, 60, 250, 20);
		
		lbl_Desc = Coex.lbl("Description", 200, 95, 150, 20);
		lbl_chlg = Coex.lbl("Challenge", 200, 240, 150, 20);
		lbl_Funding = Coex.lbl("Funding", 200, 370, 150, 20);

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
				
			case "approve":
				Database.Update("UPDATE project SET status = 'approved' where id = " + projects_id.get(projects.getSelectedIndex()) + ";");
				UploadRelevanceScores();
				break;
			case "deny":
				Database.Update("DELETE FROM project WHERE id = " + projects_id.get(projects.getSelectedIndex()));
				break;
				
			default:
				break;
		}
	}
	
	private void UploadRelevanceScores()
	{
		for(int i = 0; i < perfGoals.getItemCount(); i++)
		{
			String pushCLMN = "challenge_goal_" + perfGoals.getItem(i);
			Database.Update("UPDATE project SET "+pushCLMN+" = "+goal_relevance.get(i)+" where id = " + projects_id.get(selected_project) + ";");
		}
	}
	private void DownloadRelevanceScores()
	{
		goal_relevance = new ArrayList<Integer>();
		goal_relevance.clear();
	//	System.out.println(projects_id.get(selected_project));
		for(int i = 0; i < perfGoals.getItemCount(); i++)
		{
			String pullCLMN = "challenge_goal_" + perfGoals.getItem(i);
			
			ResultSet rs = Database.Query("SELECT "+pullCLMN+" FROM project WHERE id = " + projects_id.get(selected_project) + ";");
			try {
				while ( rs.next() ) {
					
			        rs.getMetaData();
			        float temp =  (float)rs.getObject(1);
			        goal_relevance.add((int)temp);

			    }
			} catch (SQLException er) {
				er.printStackTrace();
			}
		}
		
	}
	
	int prev_selected_goal = -1;
	int selected_project = -1;
	public void item_evt(ItemEvent e) //Handle Item Events
	{

		if(e.getSource().equals(perfGoals)  &&  perfGoals.getSelectedIndex() >= 0)
		{
			if(!add_comp.contains(relevance))
			{
				add_comp.add(relevance);
				add_comp.add(lbl_scale);
				SimManager._UpdateUI = true;
			}
			
			if(prev_selected_goal >= 0)
				goal_relevance.set(prev_selected_goal, ParseAmmount(relevance.getText()));
			
			relevance.setText("" + goal_relevance.get(perfGoals.getSelectedIndex()));
			prev_selected_goal = perfGoals.getSelectedIndex();
			
			
		}
		
		if(projects.getSelectedIndex() >= 0  &&  e.getSource().equals(projects)) 
		{
			if(prev_selected_goal >= 0)
				goal_relevance.set(prev_selected_goal, ParseAmmount(relevance.getText()));
			if(selected_project >= 0)
				UploadRelevanceScores();
			selected_project = projects.getSelectedIndex();
			prev_selected_goal = -1;
			
			add_comp.clear();
			approve.setEnabled(true);
			deny.setEnabled(true);
			

			//Challenges
			goal_relevance = new ArrayList<Integer>();
			perfGoals = new java.awt.List();
			perfGoals.setBounds(800, 130, 150, 200);
			ResultSet rs_c = Database.Query("SELECT name FROM challenges");
			try {
				while(rs_c.next())
				{
					perfGoals.add(rs_c.getString("name"));
				}
			} catch (SQLException er) {
				er.printStackTrace();
			}
			DownloadRelevanceScores();
			add_comp.add(perfGoals);
			add_comp.add(lbl_goal);
			lbl_name.setText("Project: " + projects.getSelectedItem());
			add_comp.add(lbl_name);
			lbl_company.setText("Company: " + new Company(project_orgID.get(projects.getSelectedIndex())).GetName() + " ("+project_orgID.get(projects.getSelectedIndex())+")");
			add_comp.add(lbl_company);
			
			add_comp.add(lbl_Desc);
			add_comp.add(lbl_chlg);
			add_comp.add(lbl_Funding);
			
			SimManager._UpdateUI = true;
		}
		else if(projects.getSelectedIndex() < 0)
		{
			add_comp.clear();
			approve.setEnabled(false);
			deny.setEnabled(false);
		}
		
		
		
		String item = e.getItem().toString();
		
		switch (item)
		{
			default :
				break;
		}
	}
	
	private int ParseAmmount(String s)//Returns the string as an int
	{
		int amnt = 0;
		try{
			amnt = Integer.parseInt(s);
		} catch(Exception e){}
		return amnt;
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
