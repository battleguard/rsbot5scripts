package PotatoPicker.Util;

import java.awt.Rectangle;

import org.powerbot.game.api.wrappers.Entity;

public class Methods {
	
	
	private static final Rectangle SCREEN = new Rectangle(0, 50, 514, 260);
	
	public static boolean isOnScreen(Entity e) {        
        return SCREEN.contains(e.getCentralPoint());
    }
}
