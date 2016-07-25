package actions;

import java.awt.Choice;
import java.awt.Component;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import simulation.SimManager;
import java.util.*;

public class Transactions implements MenuInterface{

	
	Choice ProjectList;
	Label Error;
	TextField a;
	private String selectedAccount = "personal";
	
	Calendar cal;
	Choice cal_day;
	Choice cal_month;
	Choice cal_year;
	
	public Transactions()
	{
		SetBaseComp();
		cal = Calendar.getInstance();
		//System.out.println(cal.);
	}
	
	private void SetBaseComp()
	{
		//base_comp.add(Coex.lbl("Transactions", 100,100,100,100));
		base_comp.add(Coex.btn("Dashboard", 5, 50, 200, 20, null));
		base_comp = Coex.LabeledTextField(base_comp, "Amount: $", 420, 70, 100);
		
		String role = SimManager.player.GetRole();
		
		List<String> transactionType = new ArrayList();  transactionType.add("Normal");
		if(role == "investor"  ||  role == "government")	transactionType.add("Loan");
		if(role == "government")	transactionType.add("Grant");
		
		base_comp = Coex.LabeledDropDown(base_comp, "Transaction Type: ", 210, 70, transactionType);
		
		List<String> fromAct = new ArrayList();  fromAct.add("Personal Account");
		if(role == "manager")	fromAct.add("Company Account");
		
		base_comp = Coex.LabeledDropDown(base_comp, "From: ", 600, 70, fromAct);
		base_comp = Coex.LabeledTextField(base_comp, "To: ", 800, 70, 100);
		
		base_comp.add(Coex.btn("Submit", 210, 120, 100, 20, "transaction_submit"));
		
		Error = new Label("");
		Error.setFont(Coex.font);
		Error.setBounds(210, 150,200,20);
		base_comp.add(Error);
		
		ProjectList = new Choice();
		ProjectList.setFont(Coex.font);
		
	}
	
	//Update the project list with the current list of projects from the DB
	private void UpdateProjectList()
	{
		ProjectList.removeAll();
		ProjectList.add("Project 1");
		ProjectList.add("Project 2");
		ProjectList.add("Project 3");
		ProjectList.setBounds(210, 90, 80, 20);
	}
	private void SubmitTransaction()
	{
		Error.setText("");
		if(ParseAmmount() <= 0)
			Error.setText("Invalid Ammount");
		if(selectedAccount == "personal" && (SimManager.player.GetMoney()) < ParseAmmount())
			Error.setText("Insufficient Funds");
		if(selectedAccount == "company")
		{
		}
		
		if(Error.getText().equals(""))
		{
			//Complete Transaction
			Error.setText("Transaction Sent");
		}
	}
	private int ParseAmmount()
	{
		int amnt = 0;
		try{
			amnt = Integer.parseInt(((TextField)base_comp.get(2)).getText());
		} catch(Exception e){}
		return amnt;
	}
	private void UpdateDaysInMonth()
	{
		int selected = 1;
		if(cal_day != null)
			selected = Integer.parseInt(cal_day.getSelectedItem());
		add_comp.remove(cal_day);
		cal_day = new Choice();
		cal_day.setBounds(250, 90, 50, 20);
		int m = Integer.parseInt(cal_month.getSelectedItem());
		
		int days_in_month = 28;
		
		if(m == 1  ||  m == 3 || m == 5 || m == 7 || m == 8  ||  m == 10 || m == 12)
			days_in_month = 31;
		if(m == 4  ||  m == 6 || m == 9 || m == 11)
			days_in_month = 30;
		
		
		boolean leap_yr = false;
		int y = Integer.parseInt(cal_year.getSelectedItem());
		if(y % 4 == 0)
			leap_yr = true;
		if(y % 100 == 0)
			leap_yr = false;
		if(y % 400 == 0)
			leap_yr = true;
		
		if(m == 2  &&  leap_yr)
			days_in_month = 29;
				

		for(int i = 1; i <= days_in_month; i++)
			cal_day.add("" + i);
		
		if(selected <= days_in_month)
			cal_day.select(selected - 1);
		else
			cal_day.select(cal_day.getItemCount() - 1);
		
		add_comp.add(cal_day);
	}
	public void action_evt(ActionEvent e) {
		String action = e.getActionCommand();
		switch (action)
		{
			case "Dashboard":
				SimManager.newMenu = new Dashboard();
				break;
			case "transaction_submit":
				SubmitTransaction();
				break;
		}
	}
	public void item_evt(ItemEvent e) {
		String item = e.getItem().toString();
		
		switch (item)
		{
			case "Normal":
				add_comp.clear();
				break;
			case "Loan":
				add_comp.clear();
				cal_month = new Choice();
				cal_month.setBounds(210, 90, 40,20);
				cal_month.add("1"); cal_month.add("2"); cal_month.add("3"); cal_month.add("4"); cal_month.add("5"); cal_month.add("6");
				cal_month.add("7"); cal_month.add("8"); cal_month.add("9");  cal_month.add("10"); cal_month.add("11"); cal_month.add("12");
				add_comp.add(cal_month);
				
				cal_year = new Choice();
				cal_year.setBounds(300, 90, 80, 20);
				for(int i = SimManager.cal.get(Calendar.YEAR); i <= SimManager.cal.get(Calendar.YEAR) + 200; i++)
					cal_year.add("" + i);
				add_comp.add(cal_year);
				UpdateDaysInMonth();
				break;
			case "Grant":
				add_comp.clear();
				UpdateProjectList();
				add_comp.add(ProjectList);
				break;
				
			case "Personal Account":
				selectedAccount = "personal";
				break;
			case "Company Account":
				selectedAccount = "company";
				break;
		}
		if(e.getSource() == cal_month  ||  e.getSource() ==  cal_year)
			UpdateDaysInMonth();
		SimManager._UpdateUI = true;
	}
	
	
	private List<Component> base_comp = new ArrayList<Component>();
	private List<Component> add_comp = new ArrayList<Component>();
	public List<Component> GetCurrentUI() {
		List<Component> list = new ArrayList<Component>(base_comp);
		list.addAll(add_comp);
		return list;
	}
	
}
