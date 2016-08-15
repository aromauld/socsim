package actions;

import java.awt.Choice;
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

public class TechEmployment implements MenuInterface{

	Company selectedOrg; 
	
	private Choice company;
	private List<Integer> company_id;
	private java.awt.List comp_info;
	private Label salary;
	
	private Label remainingEffort;
	
	private Label assignedProjects;
	private java.awt.List project_list;
	private Label label_realeffort;
	private Label label_apparenteffort;
	private TextField real_effort;
	private TextField app_effort;

	
	public TechEmployment()
	{
		SetBaseComp();
	}
	
	private void SetBaseComp() //Sets up the base components
	{
		base_comp.clear();
		base_comp = Dashboard.GetOverlay(base_comp);
		
		UpdateSkillSet();
		
		company_id = new ArrayList<Integer>();
		company = new Choice();
		company.setBounds(350, 50, 200, 20);
		ResultSet rs = Database.Query("SELECT id_company FROM employment WHERE id_player = " + SimManager.player.GetID() + " AND position = 'tech' AND offer = 'accepted'");
		try {
			while(rs.next())
			{
				company.add(new Company(rs.getInt("id_company")).GetName() + " (" + rs.getInt("id_company") + ")");
				company_id.add(rs.getInt("id_company"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(company.getItemCount() > 0)
		{
			base_comp.add(company);
			comp_info = new java.awt.List();
			UpdateCompanyInfo();
			base_comp.add(comp_info);
			base_comp.add(salary);
			
			base_comp.add(assignedProjects);
			base_comp.add(project_list);
			base_comp.add(label_realeffort);
			base_comp.add(label_apparenteffort);
			base_comp.add(real_effort);
			base_comp.add(app_effort);
			
			base_comp.add(Coex.btn("Quit", 880, 450, 80, 20, "quit_job"));
		}
		
		remainingEffort = Coex.lbl("Remaining Effort: 100", 20, 430, 150, 20);
		base_comp.add(remainingEffort);
		base_comp.add(Coex.btn("Reset Effort", 20, 450, 150, 20, "reset_effort"));
	}
	
	private void UpdateCompanyInfo()
	{
		
		selectedOrg = new Company(company_id.get(company.getSelectedIndex()));
		comp_info.removeAll();
		comp_info.setBounds(350, 70, 200, 350);
		ResultSet rs = Database.Query("SELECT id_player, position FROM employment WHERE id_company = " + selectedOrg.GetID() + " and offer = 'accepted'");
		try {
			while (rs.next()) 
			{				
				comp_info.add(new Player(rs.getInt("id_player")).GetName() + " - " + rs.getString("position"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		salary = Coex.lbl("", 350, 425, 200, 20);
		salary.setText("Salary: ");
		UpdateProjectList();
	}
	
	private void UpdateProjectList()
	{
		assignedProjects = Coex.lbl("Assigned Projects", 600, 100, 200, 20);
		
		project_list = new java.awt.List();
		project_list.setBounds(600, 120, 200, 250);
		for(int i = 0; i < 5; i++)
		{
			project_list.add("Project " + i);
		}
		
		label_realeffort  = Coex.lbl("Real Effort", 600, 380, 100, 20);
		label_apparenteffort  = Coex.lbl("Apparent Effort", 600, 400, 100, 20);

		real_effort = Coex.txtF("0", 700, 380, 75, 20);
		app_effort = Coex.txtF("0", 700, 400, 75, 20);
	
	}
	
	private boolean StillEmployed()
	{
		if(selectedOrg == null)
			return false;
		return Database.hasNext("SELECT id FROM employment WHERE offer = 'accepted' AND position = 'tech' AND id_player = " + SimManager.player.GetID() + " AND id_company = " + selectedOrg.GetID());
	}

	private void UpdateSkillSet()
	{
		base_comp.add(Coex.lbl("Skills", 20, 50, 50, 20));
		for(int i = 0; i < SimManager.skill_list.size(); i++)
		{
			base_comp.add(Coex.lbl(SimManager.skill_list.get(i).GetName() + " - " + SimManager.skill_list.get(i).GetPlayerSkill(SimManager.player.GetID()), 20, 75 + (20 * i), 150, 20));
		}
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
			
			case "quit_job":
				if(StillEmployed())
					Database.Update("DELETE FROM employment WHERE id_player = " + SimManager.player.GetID() + " AND id_company = "+ selectedOrg.GetID() +" AND position = 'tech' AND offer = 'accepted';");
				break;
			default:
				break;
		}
	}
	
	public void item_evt(ItemEvent e) //Handle Item Events
	{
		
		if(!company.getSelectedItem().equals(selectedOrg.GetName()))
			UpdateCompanyInfo();
		
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
