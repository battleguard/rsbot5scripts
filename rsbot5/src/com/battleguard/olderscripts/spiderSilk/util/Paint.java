package SpiderSilk.util;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;

public final class Paint {		
	
	public static final DecimalFormat numFormat  = new DecimalFormat("###,###,###");
	
	public static final String formatter(int num) {
		if(num < 1000) return "" + num;
		return num / 1000 + "." + (num % 1000) / 100 + "K"; 
	}
	
	public static final void shadowText(Graphics g, String line, int x, int y) {
		g.setColor(Color.BLACK);
		g.drawString(line, x+1, y+1);
		g.setColor(Color.WHITE);
		g.drawString(line, x, y);		
	}
	
	


}
