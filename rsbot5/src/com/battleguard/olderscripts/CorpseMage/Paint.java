package CorpseMage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.powerbot.game.api.methods.tab.Skills;

public class Paint {
	
	private static final NumberFormat K = new DecimalFormat("###,###,###");
		
	private static final Color GREEN = new Color(85, 110, 36, 180);
	private static final Color RED = new Color(255, 0, 0, 180);
	private static final Color OPAQUE_BLACK = new Color(0, 0, 0, 160);
	private static final Color OPAQUE_BLACK2 = new Color(0, 0, 0, 180);
	private static final Color OPAQUE_BLACK3 = new Color(0, 0, 0, 195);	
	
	private static final Color[] skillColors = {Color.RED, Color.YELLOW, Color.ORANGE.darker(), GREEN, GREEN, Color.BLUE};
	public static final int [] idx = {Skills.CONSTITUTION, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENSE, Skills.RANGE, Skills.MAGIC};
	
	public static final SkillData skilldata = new SkillData();
	
	public static String action = "";
	
	
	public static final void paint(Graphics g) throws Exception {		
		g.setFont(new Font("Gayatri", 0, 12));		
		g.setColor(OPAQUE_BLACK);	
		g.fill3DRect(0, 330, 510 + (AllLoot.HIGH_ALCH ? 160 : 0), 20, true);		
		g.fill3DRect(0, 320, 290, 10, true);
		
		drawLootTable(g);
		drawSkillPaint(g);
	}
	
	
	public static final void drawLootTable(Graphics g) throws Exception {
		drawbox(g, 0, 330, "Name:", "Collected:","Worth:");
		drawbox(g, 255, 330, "Name:", "Collected:","Worth:");
						
		for(int i = 0; i < AllLoot.LOOT_LIST.length; i++) {				
			if(i < 5) {
				drawbox(g, 0 ,330 + (i+1)*20, AllLoot.LOOT_LIST[i].name, K.format(AllLoot.LOOT_LIST[i].collected), K.format(AllLoot.LOOT_LIST[i].worth), i);
			} else {
				drawbox(g, 255,330 + (i-4)*20, AllLoot.LOOT_LIST[i].name, K.format(AllLoot.LOOT_LIST[i].collected), K.format(AllLoot.LOOT_LIST[i].worth), i);
			}				
		}
		
		final int ProfitPerHour = (int)(((double) AllLoot.Total / (double) AllLoot.runtime.getElapsed()) * 3600000d);
		drawbox(g, 0, 330+120, "Total Money: ", K.format(AllLoot.Total) + "gp", K.format(ProfitPerHour) + "gp/h");
		if(AllLoot.HIGH_ALCH) drawAlchList(g);
	}
	
	
	public static final void drawAlchList(Graphics g) throws Exception {
		drawAlchItemBox(g, 0, "Items Alched", "" + "Amount");
		shadowText(g, "Items Alched", 515, 348);
		for(int i = 0; i < AllLoot.HIGH_ALCH_LIST.length; i++) {
			drawAlchItemBox(g, i+1, AllLoot.HIGH_ALCH_LIST[i].name, "" + AllLoot.HIGH_ALCH_LIST[i].collected);
		}
	}
	
	
	public static final void drawSkillPaint(Graphics g) throws Exception {
		int found = 0;
		for(int idx = 0; idx < 6; idx++) {
			if(skilldata.SKILL_LIST[idx].isValid()) drawSkillInfo(g, idx, 300 - (found++ * 20));			
		}
		found = 300 - (found * 20);
		g.setColor(OPAQUE_BLACK);		
		g.fill3DRect(0, found, 290, 20, true);
		shadowText(g, "Runtime: " + AllLoot.runtime.toElapsedString() , 20, found+18);
		shadowText(g, "Action: " + action, 135, found+18);
	}
	
	
	public static final void drawSkillInfo(Graphics g, final int idx, final int yComp) throws Exception {		
		final Color CurColor = new Color(skillColors[idx].getRed(), skillColors[idx].getGreen(), skillColors[idx].getBlue(), 180);
		
		int xComp = 20;
		g.setColor(OPAQUE_BLACK);
		g.fill3DRect(0, yComp, 290, 20, true);				
		g.setColor(Color.BLACK);
		g.draw3DRect(xComp-5, yComp+5, 270, 17, true);
		g.setColor(OPAQUE_BLACK2);
		g.fill3DRect(xComp-5, yComp+5, 270, 17, true);
		g.setColor(CurColor);
		g.fillRect(xComp-4, yComp+6, (int) (268 * skilldata.SKILL_LIST[idx].getPercent()), 15);
		shadowText(g, skilldata.SKILL_LIST[idx].getPaintLineInfo(), xComp, yComp+18);
	}
	
	
	public static final String formatter(final int num) throws Exception {
		return num / 1000 + "." + (num % 1000) / 100 + "K"; 
	}
	
	
	public static final void drawAlchItemBox(Graphics g, final int idx, final String left, final String right) throws Exception {
		g.setColor(OPAQUE_BLACK);
		g.fillRect(510, 330 + (idx)*20, 160, 20);
		g.setColor(OPAQUE_BLACK2);
		g.drawRect(510, 330 + (idx)*20, 160, 20);
		g.drawLine(510+110, 330 + idx*20, 510+110, 350 + idx*20);
		shadowText(g, left, 515, 330 + (idx)*20 + 18);
		shadowText(g, right, 510+115, 330 + (idx)*20 + 18);
	}
	
	
	public static final void drawProfitPercent(Graphics g, final int i, final int x, final int y) throws Exception {		
		if(AllLoot.LOOT_LIST[i].worth == 0 || AllLoot.Total == 0 ) return;
		g.setColor(AllLoot.LOOT_LIST[i].worth > 0 ? GREEN : RED);
		g.fillRect(x ,y, 255 * Math.abs(AllLoot.LOOT_LIST[i].worth) / (AllLoot.Gain - AllLoot.Loss), 20);
	}
	
	
	public static final void drawbox(Graphics g, final int x, final int y, final String left, final String middle, final String right, int i) throws Exception {
		g.setColor(OPAQUE_BLACK3);
		g.fillRect(x, y, 255, 20);
		drawProfitPercent(g, i, x, y);
		g.setColor(OPAQUE_BLACK3);
		g.drawLine(x + 89, y, x + 89, y + 20);
		g.drawLine(x + 170, y, x + 170, y + 20);		
		g.drawRect(x, y, 255, 20);		
		shadowText(g, left, x + 5, y + 18);
		shadowText(g, middle, x + 100, y + 18);
		shadowText(g, right, x + 175, y + 18);	
	}
	
	
	public static final void drawbox(Graphics g, final int x, final int y, final String left, final String middle, final String right) throws Exception {
		g.setColor(OPAQUE_BLACK3);
		g.fillRect(x, y, 255, 20);		
		g.drawLine(x + 89, y, x + 89, y + 20);
		g.drawLine(x + 170, y, x + 170, y + 20);		
		g.drawRect(x, y, 255, 20);
		shadowText(g, left, x + 5, y + 18);
		shadowText(g, middle, x + 100, y + 18);
		shadowText(g, right, x + 175, y + 18);	
	}
	
	
	public static final void shadowText(Graphics g, final String line, final int x, final int y) throws Exception {
		g.setColor(Color.BLACK);
		g.drawString(line, x+1, y+1);
		g.setColor(Color.WHITE);
		g.drawString(line, x, y);		
	}
	
}
