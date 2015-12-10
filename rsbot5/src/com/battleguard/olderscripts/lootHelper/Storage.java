package LootHelper;

import java.awt.Font;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;



public class Storage {
	
	private static final String DROPBOX_URL = "http://dl.dropbox.com/u/10772544/";	
	private static final String FONT_FILE_NAME = "runescape_chat_bold.ttf";	
	
	
	
	public static final JButton getImage(String fileName) {
		return new JButton(fileName);
	}
	
	/*
	public static final JButton getImage(String fileName) {
		JButton button;
		try {
			System.out.println("Downloading File " + fileName);			
			button = new JButton(new ImageIcon(ImageIO.read(new URL(DROPBOX_URL + fileName + ".png").openStream())));	    					
		} catch (Exception e) {
			button = new JButton(fileName);
			e.printStackTrace();
		}	
		return button;
	}
	*/
	
	/*
	public static final Font getFont() {
		return new Font("serif", Font.PLAIN, 12);
	}
	*/
	
	public static final Font getFont() {		
		Font font;
		try {
			URL fontUrl = new URL(DROPBOX_URL + FONT_FILE_NAME);			
			font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream()).deriveFont(15f);
			System.out.println("Font has been loaded");
		} catch (Exception e) {
			System.out.println("Failed to load font");
			font = new Font("serif", Font.PLAIN, 12);
		}
		return font;
	}
	
	
	
}
