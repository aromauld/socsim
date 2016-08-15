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
import simulation.Database;
import simulation.SimManager;

public class JobOffers implements MenuInterface {

	java.awt.List offers;
	List<Integer> offers_id = new ArrayList<Integer>();
	Button acceptOffer;
	Button declineOffer;
	
	public JobOffers()
	{
		SetBaseComp();
	}
	
	private void SetBaseComp() //Sets up the base components
	{
		base_comp.clear();
		base_comp = Dashboard.GetOverlay(base_comp);
		
		offers = new java.awt.List();
		offers.setBounds(210,100,400,300);
		base_comp.add(offers);
		
		ResultSet rs = Database.Query("SELECT id, id_company, position FROM employment WHERE id_player = " + SimManager.player.GetID() + " AND offer = 'pending'");
		try {
			while(rs.next())
			{
				offers.add(new Company(rs.getInt("id_company")).GetName() + " - " + rs.getString("position"));
				offers_id.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		acceptOffer = Coex.btn("Accept", 620, 100, 200, 20, "accept_offer"); acceptOffer.setEnabled(false); base_comp.add(acceptOffer);
		declineOffer = Coex.btn("Decline", 620, 130, 200, 20, "decline_offer"); declineOffer.setEnabled(false); base_comp.add(declineOffer);
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
				
			case "accept_offer":
				if(SimManager.player.GetRole().equals("manager"))
					Database.Update("DELETE FROM employment WHERE id = " + SimManager.player.GetID() + " AND offer = 'accepted'");
				Database.Update("UPDATE employment SET offer = 'accepted' where id = " + offers_id.get(offers.getSelectedIndex()) + ";");
				break;
				
			case "decline_offer":
				Database.Update("DELETE FROM employment WHERE id = " + offers_id.get(offers.getSelectedIndex()));
				break;
				
			default:
				break;
		}
	}
	
	public void item_evt(ItemEvent e) //Handle Item Events
	{
		if(offers.getSelectedIndex() >= 0)
		{
			acceptOffer.setEnabled(true);
			declineOffer.setEnabled(true);
		}
		else
		{
			acceptOffer.setEnabled(false);
			declineOffer.setEnabled(false);
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
