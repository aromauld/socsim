package actions;

import java.awt.Button;
import java.awt.Component;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

import components.Company;
import simulation.SimManager;

public class Employment implements MenuInterface {

	private List<Label> skill_label = new ArrayList<Label>();
	
	private Label[] companyInfo = new Label[6];
	
	private java.awt.List projects;
	
	//Job Offers
	private Button acceptJob;
	private Button quitJob;
	private java.awt.List jobOffers;
	private List<Company> job_offers = new ArrayList<Company>();
	private int selectedOffer = -1;
	
	
	
	public Employment()
	{
		SetBaseComp();
	}
	
	private void SetBaseComp() //Sets up the base components
	{
		base_comp.clear();
		base_comp.add(Coex.btn("Dashboard", 5, 50, 200, 20, null));
		
		
		//Employment Info
		UpdateEmploymentStatus();
		
		
		//Accept Job Button
		acceptJob = new Button("Accept");
		acceptJob.setBounds(880, 125, 80, 20);
		acceptJob.setActionCommand("job_offer_accept");
		acceptJob.setEnabled(false);
		base_comp.add(acceptJob);
		
		
		//Quit Job Button
		quitJob = new Button("Quit Job");
		quitJob.setBounds(840, 450, 80, 20);
		quitJob.setActionCommand("job_quit");
		if(SimManager.player.GetOrg() == null)
			quitJob.setEnabled(false);
		base_comp.add(quitJob);
		
		
		//Skill List
		base_comp.add(Coex.lbl("Skills", 450, 125, 80,20));
		for(int i = 0; i < SimManager.skill_list.size(); i++)
			skill_label.add(Coex.lbl(SimManager.skill_list.get(i).GetName() + "	- " + SimManager.skill_list.get(i).GetLearnRate(), 450, 150 + (20 * i), 100, 20));
		for(int i = 0; i < skill_label.size(); i++)
			base_comp.add(skill_label.get(i));
		
		
		//Project List
		base_comp.add(Coex.lbl("Projects", 600, 125, 80,20));
		GetProjects();
		base_comp.add(projects);
		
		
		//Job Offers
		base_comp.add(Coex.lbl("Job Offers", 800, 125, 80,20));
		GetJobOffers();
		base_comp.add(jobOffers);
		
	}
	
	private void UpdateEmploymentStatus() //Updates the employment status label
	{
		for(int i = 0; i < companyInfo.length; i++) base_comp.remove(companyInfo[i]);
		
		companyInfo[0] = Coex.lbl("Employment", 230,125,170,20);

		if(SimManager.player.GetOrg() != null)
		{
			Company c = SimManager.player.GetOrg();
			companyInfo[1] = Coex.lbl(c.GetName() + " (" + c.GetID() + ")", 230,150,170,20);
			if(c.GetCEO() != null) companyInfo[2] = Coex.lbl("CEO: " + c.GetCEO().GetName(), 230,170,170,20);
			if(c.GetCFO() != null) companyInfo[3] = Coex.lbl("CFO: " + c.GetCFO().GetName(), 230,190,170,20);
			if(c.GetCTO() != null) companyInfo[4] = Coex.lbl("CTO: " + c.GetCTO().GetName(), 230,210,170,20);
			
			companyInfo[5] = Coex.lbl("Salary: ", 230,250,170,20);
		}
		else	
		{
			for(int i = 1; i < companyInfo.length; i++) companyInfo[i] = null;
			companyInfo[1] = Coex.lbl("Unemployed", 230,150,170,20);
		}
		for(int i = 0; i < companyInfo.length; i++) if(companyInfo[i] != null) base_comp.add(companyInfo[i]);
		SimManager._UpdateUI = true;
	}
	
	private void GetProjects() //Looks up the player's assigned projects and stores them in a list
	{
		projects = new java.awt.List();
		projects.add("Project 1");
		projects.add("Project 2");
		projects.add("Project 3");
		projects.add("Project 4");
		projects.add("Project 5");
		projects.setBounds(600, 150, 150, 150);
	}
	
	private void GetJobOffers() //Fetch job offers from database
	{
		job_offers.add(new Company("Company 1"));
		job_offers.add(new Company("Company 2"));
		
		jobOffers = new java.awt.List(job_offers.size());
		jobOffers.setBounds(800, 150, 160, 200);
		jobOffers.removeAll();
		for(int i = 0; i < job_offers.size(); i++)
			jobOffers.add(job_offers.get(i).GetName());
	}

	public void action_evt(ActionEvent e) //Handle Button Presses
	{
		String action = e.getActionCommand();
		switch (action)
		{
			case "Dashboard":
				SimManager.newMenu = new Dashboard();
				break;
			case "job_offer_accept":
				if(selectedOffer >= 0 &&  selectedOffer < job_offers.size())
				{
					
					SimManager.player.SetOrg(job_offers.get(selectedOffer));

					quitJob.setEnabled(true);
					UpdateEmploymentStatus();
					
					//Remove the job update from the database and update job Offers 'GetJobOffers()'
					jobOffers.remove(selectedOffer);
					job_offers.remove(selectedOffer);
					acceptJob.setEnabled(false);
				}
				
				break;
			case "job_quit":
				SimManager.player.SetOrg(null);
				quitJob.setEnabled(false);
				UpdateEmploymentStatus();
				break;
			default:
				break;
		}
	}
	
	public void item_evt(ItemEvent e) //Handle Item Events
	{

		selectedOffer = jobOffers.getSelectedIndex();
		if(selectedOffer >= 0) acceptJob.setEnabled(true);

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
