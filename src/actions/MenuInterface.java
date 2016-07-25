package actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public interface MenuInterface {
	List<Component> base_comp = new ArrayList<Component>();
	List<Component> add_comp = new ArrayList<Component>();
	
	public List<Component> GetCurrentUI();
	public void action_evt(ActionEvent e);
	public void item_evt(ItemEvent e);
}


