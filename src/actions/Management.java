package actions;

import java.awt.Button;
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

public class Management implements MenuInterface{

	Company org;
	private Label[] companyInfo = new Label[6];
	private Button quitButton;
	
	//Unemployed
	TextField companyName;
	
	//CEO
	java.awt.List available_managers;
	List<Player> available_managers_player;
	Button hire_cfo;
	Button hire_cto;
	
	//CTO
	java.awt.List technologists;
	Button sendJobOffer;
	java.awt.List ProjectList;
	Button createProject;
	java.awt.List Employees;
	Button fireEmployee;
	Button AssignToProject;
	java.awt.List WorkingOnProject;
	Button RemoveFromProject;
	
	
	
	public Management()
	{
		GetPertainedCompany();
		SetBaseComp();
	}
	
	private void GetPertainedCompany() //Looks up player in database, sets 'org' to the company that they are a part of
	{
		this.org = SimManager.player.GetOrg();
	}
	
	private void GetAvailableManagers() //Updates the list of all unemployed managers
	{
		if(available_managers == null)
		{
			available_managers_player = new ArrayList<Player>();
			available_managers = new java.awt.List();
		}
		else
		{
			available_managers_player.clear();
			available_managers.removeAll();
			available_managers.select(-1);
			if(hire_cfo != null) hire_cfo.setEnabled(false);
			if(hire_cto != null) hire_cto.setEnabled(false);
		}
		
		try {
			
			ResultSet rs = Database.Query("select id from profile WHERE role = 'manager' and status = '';");
			if(rs != null)
			while ( rs.next() ) {
				available_managers_player.add(new Player((int)rs.getObject("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < available_managers_player.size(); i++)
			available_managers.add(available_managers_player.get(i).GetName());
		
		available_managers.setBounds(450, 120, 200,300);		
	}
	
	private void GetCompanyInfo() //Updates the displayed company info
	{
		if(org != null)
		{
			companyInfo[0].setText(org.GetName() + " (" + org.GetID() + ")");
			if(org.GetCEO() != null) companyInfo[1].setText("CEO: " + org.GetCEO().GetName()); else companyInfo[1].setText("CEO: -");
			if(org.GetCFO() != null) companyInfo[2].setText("CFO: " + org.GetCFO().GetName()); else companyInfo[2].setText("CFO: -");
			if(org.GetCTO() != null) companyInfo[3].setText("CTO: " + org.GetCTO().GetName()); else companyInfo[3].setText("CTO: -");
		}
	}
	
	private void GetTechnologists() //Updates the list of all technologists in the game (not working for company)
	{
		technologists = new java.awt.List();

		List<Player> technologists_player = Database.GetPlayers("select id from profile WHERE role = 'technologist' and org_id != '" + org.GetID() + "';");
		for(int i = 0; i < technologists_player.size(); i++)
			technologists.add(technologists_player.get(i).GetName());		
		
		technologists.setBounds(210, 250, 150,180);
		sendJobOffer = Coex.btn("Send Job Offer", 210, 430, 150, 20, "send_job_offer"); sendJobOffer.setEnabled(false);
	}
	
	private void GetProjectList() //Updates the list of projects that the company has
	{
		ProjectList = new java.awt.List();
		ProjectList.add("Project 1");
		ProjectList.add("Project 2");
		ProjectList.setBounds(450, 120, 150, 270);
		createProject = Coex.btn("Create Project", 450, 430, 150, 20, "create_project");
	}
	
	private void GetEmployees() //Updates the list of players that are working for the company
	{
		Employees = new java.awt.List();

		for(int i = 0; i < Database.GetPlayers("select id from profile WHERE role = 'technologist' and org_id = '" + org.GetID() + "';").size(); i++)
			Employees.add(Database.GetPlayers("select id from profile WHERE role = 'technologist' and org_id = '" + org.GetID() + "';").get(i).GetName());
				
		Employees.setBounds(610, 120, 150, 270);
		fireEmployee = Coex.btn("Fire", 610, 430, 150, 20, "fire_employee"); fireEmployee.setEnabled(false);
	}
	
	private void GetWorkingOnProject() //Returns the list of people that are working on the selected project
	{
		WorkingOnProject = new java.awt.List();
		WorkingOnProject.add("Player 6");
		WorkingOnProject.add("Player 7");
		WorkingOnProject.setBounds(800, 150, 150, 260);
		RemoveFromProject = Coex.btn("Remove From Project", 800, 120, 150, 20, "remove_from_project"); RemoveFromProject.setEnabled(false);
	}
	
	private void SetBaseComp() //Sets up the base components
	{
		base_comp.clear();
		base_comp.add(Coex.btn("Dashboard", 5, 50, 200, 20, null));
		
		if(org == null)
		{
			//Create Company
			companyName = Coex.txtF("", 210, 70, 200, 20);
			base_comp.add(companyName);
			base_comp.add(Coex.btn("Create Company", 210, 90, 200, 20, "company_create"));
		}
		
		if(org != null)
		{	
			
			//Company Info
			for(int i = 0; i < companyInfo.length; i++) { companyInfo[i] = Coex.lbl("", 210,100 + (20 * i),200,20); base_comp.add(companyInfo[i]); }
			GetCompanyInfo();

			if(SimManager.player.GetStatus().equals("CEO"))
			{
				//Manager List
				base_comp.add(Coex.lbl("Available Managers", 450, 100, 200, 20));
				GetAvailableManagers();
				base_comp.add(available_managers);
				
				//Hire Buttons
				hire_cfo = Coex.btn("Hire as CFO", 700, 120, 150, 20, "change_cfo"); hire_cfo.setEnabled(false);
				hire_cto = Coex.btn("Hire as CTO", 700, 140, 150, 20, "change_cto"); hire_cto.setEnabled(false);
				base_comp.add(hire_cfo);
				base_comp.add(hire_cto);
				
				//Quit Button
				quitButton = Coex.btn("Resign as CEO", 700, 400, 150, 20, "resignation");
				base_comp.add(quitButton);
				//base_comp.add(Coex.btn("Resign as CEO", 700, 400, 150, 20, "resignation"));
				
				//Projects, Products
				base_comp.add(Coex.btn("Projects", 210, 220, 200, 20, "company_projects"));
				base_comp.add(Coex.btn("Products", 210, 240, 200, 20, "company_products"));
			}
			else if(SimManager.player.GetStatus().equals("CTO"))
			{
				//Technologists / Job Offers
				GetTechnologists();
				base_comp.add(technologists);
				base_comp.add(sendJobOffer);
				
				//Projects List
				base_comp.add(Coex.lbl("Projects", 450, 100, 150, 20));
				GetProjectList();
				base_comp.add(ProjectList);
				base_comp.add(createProject);
				
				//Employees
				base_comp.add(Coex.lbl("Employees", 610, 100, 150, 20));
				GetEmployees();
				base_comp.add(Employees);
				base_comp.add(fireEmployee);
				
				//Assign To Project
				AssignToProject = Coex.btn("Assign To Project", 450, 400, 310, 20, "assign_project"); AssignToProject.setEnabled(false);
				base_comp.add(AssignToProject);
				
				//Working on project
				base_comp.add(Coex.lbl("Working on Project", 800, 100, 150, 20));
				GetWorkingOnProject();
				base_comp.add(WorkingOnProject);
				base_comp.add(RemoveFromProject);
				
				//Quit Button
				quitButton = Coex.btn("Resign as CTO", 800, 430, 150, 20, "resignation");
				base_comp.add(quitButton);
				//base_comp.add(Coex.btn("Resign as CTO", 800, 430, 150, 20, "resignation"));
			}
		}
	}

	public void action_evt(ActionEvent e) //Handle Button Presses
	{
		String action = e.getActionCommand();
		switch (action)
		{
			case "Dashboard":
				SimManager.newMenu = new Dashboard();
				break;
			case "company_projects":
				SimManager.newMenu = new Projects();
				break;
			case "company_products":
				SimManager.newMenu = new Products();
				break;
			case "company_create":
				org = new Company(companyName.getText());
				SimManager.player.SetOrg(org);
				SimManager.player.SetStatus("CEO");
				SetBaseComp();
				SimManager._UpdateUI = true;
				break;
				
			case "change_cfo":			
				if(org.GetCFO() != null) //Fire the old CFO
				{
					org.GetCFO().SetOrg(null);
					org.GetCFO().SetStatus("");
				}
				org.SetCFO(available_managers_player.get(available_managers.getSelectedIndex()));
				available_managers_player.get(available_managers.getSelectedIndex()).SetOrg(org);
				available_managers_player.get(available_managers.getSelectedIndex()).SetStatus("CFO");
				GetAvailableManagers();
				GetCompanyInfo();
				quitButton.transferFocus();
				break;
				
			case "change_cto":
				if(org.GetCTO() != null)
				{
					org.GetCTO().SetOrg(null);
					org.GetCTO().SetStatus("");
				}
				org.SetCTO(available_managers_player.get(available_managers.getSelectedIndex()));
				available_managers_player.get(available_managers.getSelectedIndex()).SetOrg(org);
				available_managers_player.get(available_managers.getSelectedIndex()).SetStatus("CTO");
				GetAvailableManagers();
				GetCompanyInfo();
				quitButton.transferFocus();
				break;
				
			case "send_job_offer":
				//technologists_player.get(technologists.getSelectedIndex()).SetStatus("");
				Database.SendJobOffer(Database.GetPlayers("select id from profile WHERE role = 'technologist' and org_id != '" + org.GetID() + "';").get(technologists.getSelectedIndex()).GetID(), org.GetID(), "tech");
				//technologists_player.get(technologists.getSelectedIndex()).SetOrg();
				sendJobOffer.setEnabled(false);
				GetTechnologists();
				break;
				
			case "fire_employee":
				Database.GetPlayers("select id from profile WHERE role = 'technologist' and org_id = '" + org.GetID() + "';").get(Employees.getSelectedIndex()).SetOrg(null);
				break;
				
			case "resignation":
				SimManager.player.SetOrg(null);
				
				if(SimManager.player.GetStatus().equals("CEO"))
					this.org.SetCEO(null);
				if(SimManager.player.GetStatus().equals("CFO"))
					this.org.SetCFO(null);
				if(SimManager.player.GetStatus().equals("CTO"))
					this.org.SetCTO(null);
				
				SimManager.player.SetStatus("");
				this.org = null;
				GetPertainedCompany();
				SetBaseComp();
				SimManager._UpdateUI = true;
				break;
				
			default:
				break;
		}
	}
	
	public void item_evt(ItemEvent e) //Handle Item Events
	{
		//CEO
		if(SimManager.player.GetStatus().equals("CEO") && available_managers.getSelectedIndex() >= 0) { hire_cfo.setEnabled(true); hire_cto.setEnabled(true); }
		
		if(SimManager.player.GetStatus().equals("CTO") && technologists.getSelectedIndex() >= 0) sendJobOffer.setEnabled(true);
		if(SimManager.player.GetStatus().equals("CTO") && Employees.getSelectedIndex() >= 0) fireEmployee.setEnabled(true);

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
