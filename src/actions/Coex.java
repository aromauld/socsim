//Component Extension
//Easy to use static methods that return components or modified lists based on the given values

package actions;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.List;

public class Coex {
	public static Font font;
	
	public static TextField txtF(String name, int x, int y, int width, int height)
	{
		TextField temp = new TextField(name);
		if(font != null)
			temp.setFont(font);
		temp.setBounds(x,y,width,height);
		return temp;
	}
	
	public static Label lbl(String name, int x, int y, int width, int height)
	{
		Label temp = new Label(name);
		temp.setBounds(x,y,width,height);
		return temp;
	}
	
	public static Button btn(String Label, int x, int y, int width, int height, String actCmd)
	{
		Button temp = new Button(Label);
		temp.setBounds(x,y,width,height);
		temp.setActionCommand(actCmd);
		return temp;
	}
	
	
	
	
	
	
	
	
	public static List<Component> LabeledTextField(List<Component> list, String lbl, int x, int y, int txtF_width)
	{
		
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		
		int textwidth = (int)(font.getStringBounds(lbl, frc).getWidth()) + 2;
		int textheight = 20;//(int)(font.getStringBounds(lbl, frc).getHeight() + 2);
		
		Label temp = new Label(lbl);
		temp.setFont(font);
		temp.setBounds(x, y, textwidth, textheight);
		
		
		TextField tempTF = new TextField();
		tempTF.setBounds(x + textwidth + 5, y, txtF_width, textheight);
		
		list.add(temp);
		list.add(tempTF);
		
		
		return list;
	}
	
	
	
	
	
	
	public static List<Component> LabeledDropDown(List<Component> list, String lbl, int x, int y, List<String> dd_list)
	{
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		
		int textwidth = (int)(font.getStringBounds(lbl, frc).getWidth()) + 2;
		int textheight = 20;
		
		Label temp = new Label(lbl);
		temp.setFont(font);
		temp.setBounds(x, y, textwidth, textheight);
		
		int dd_width = 0;
		
		Choice c = new Choice();
		c.setFont(font);
		for(int i = 0; i < dd_list.size(); i++)
		{
			if(dd_list.get(i) != "")
			{
			c.add(dd_list.get(i));
			if((int)(font.getStringBounds(dd_list.get(i), frc).getWidth()) > dd_width)
				dd_width = (int)(font.getStringBounds(dd_list.get(i), frc).getWidth());
			}
		}
		
		c.setBounds(x + textwidth + 5, y, dd_width + 40, textheight);
		
		
		list.add(temp);
		list.add(c);
		
		return list;
	}
}
