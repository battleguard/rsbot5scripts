package com.battleguard.scripts.f2prunecrafter.graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.battleguard.scripts.f2prunecrafter.data.Master;

public class Gui {
	
	private final JFrame frame;	
	private final JComboBox<Master> runeTypes = new JComboBox<Master>(Master.values());
	private final JLabel description = new JLabel("Select Rune Type");
	private final JButton start = new JButton("Start");	
	private Master selectedEnum;
	
	public Gui() {
		frame = new JFrame("F2P Runecrafter");
		frame.setLayout(new FlowLayout());		
		start.addActionListener(startEvent);
		addComponents();
		initializeFrame();
	}
	
	private ActionListener startEvent = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			selectedEnum = (Master) runeTypes.getSelectedItem();
			System.out.println(selectedEnum);
			frame.dispose();
		}
	};
	
	private void addComponents() {
		frame.add(description);
		frame.add(runeTypes);
		frame.add(start);
	}
	
	private void initializeFrame() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}	
	
	public boolean isDone() {
		return frame.isValid();
	}
	
	public Master getSelectedEnum() {
		return selectedEnum;
	}
}

