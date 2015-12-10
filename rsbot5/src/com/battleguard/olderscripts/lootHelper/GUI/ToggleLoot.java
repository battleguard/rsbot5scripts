package LootHelper.GUI;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JTextField;
public class ToggleLoot extends Panel {
	private static final long serialVersionUID = 1L;	
	
	private static boolean displayLoot = true;
	private static int toggleLootKey = KeyEvent.VK_ALT;
	
	private static final JLabel TOGGLE_HDR = new JLabel("KeyStroke");
	private static final JLabel TOGGLE_LBL = new JLabel(
			"<html>Click on the Keystroke textField and type<br> a single key to change the assigned key<br>"
					+ "to toggle on and off loot display.</html>");
	private static final JTextField TOGGLE_KEY_TXT = new JTextField("Key: Alt");
	
	public ToggleLoot() {
		
		TOGGLE_KEY_TXT.addKeyListener(new KeyListener()
	    {				          
	          @Override	          
	          public void keyReleased(KeyEvent e) {
	        	  toggleLootKey = e.getKeyCode();
	        	  if(e.getKeyChar() == 65535) {
	        		  TOGGLE_KEY_TXT.setText("Key: " + KeyEvent.getKeyText(e.getKeyCode()));
	        	  }	else {
	        		  TOGGLE_KEY_TXT.setText("Key: " + e.getKeyChar());
	        	  }		
	          }
	          
	          @Override 
	          public void keyPressed(KeyEvent e) {}
	          
	          @Override
	          public void keyTyped(KeyEvent e) {}
	    });
		
		TOGGLE_HDR.setForeground(Color.WHITE);
		TOGGLE_LBL.setForeground(Color.WHITE);		
		c.insets = new Insets(0, 5, 0, 5);
		c.gridheight = 2;
		addToGrid(c, TOGGLE_LBL, 0, 5, 1, 1.0, this);
		c.gridheight = 1;
		addToGrid(c, TOGGLE_HDR, 1, 5, 1, 1.0, this);
		addToGrid(c, TOGGLE_KEY_TXT, 1, 6, 1, 1.0, this);		
	}
	
	public static final boolean displayLoot() {
		return displayLoot;
	}
		
	public static final void toggle(KeyEvent e) {
		if(e.getKeyCode() == toggleLootKey) {
			displayLoot = !displayLoot;			
		}
	}

}
