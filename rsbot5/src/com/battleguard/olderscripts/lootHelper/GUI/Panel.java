package LootHelper.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel {
	
	public final GridBagConstraints c = new GridBagConstraints();
	
	public Panel() {
		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());		
		c.fill = GridBagConstraints.BOTH;
	}
	
	public static final void addToGrid(GridBagConstraints c, Component comp, int gridx, int gridy, int gridwidth, double weightx,
			Container pane) {
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		c.weightx = weightx;
		pane.add(comp, c);
	}
	
}
