package Miner;
import java.awt.Color;
import java.awt.Graphics;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;

public class SkillData {	
	
	public static final Timer RUN_TIME = new Timer(0);
	
	public static final String[] SKILL_NAMES = { "attack", "defence", "strength", "constitution", "range", "prayer", 
		  "magic", "cooking", "woodcutting", "fletching", "fishing", "firemaking", "crafting", "smithing", "mining", 
		  "herblore", "agility", "thieving", "slayer", "farming", "runecrafting", "hunter", "construction", 
		  "summoning", "dungeoneering"};
	  
	  public static final Color[] COLORS = {Color.red, Color.green, Color.blue, Color.yellow, Color.cyan, Color.magenta, Color.gray};
	
	public static Skill[] SKILL_LIST = new Skill[SKILL_NAMES.length];
	
	public SkillData() {
		
		for(int i = 0; i < SKILL_NAMES.length; i++) {
			SKILL_LIST[i] = new Skill(i);
		}
	}
	
	public class Skill {
		public final int START_XP;
		public final int START_LVL;
		public final int IDX;
		public final String NAME;
		
		public Skill(final int idx) {
			this.IDX = idx;
			this.NAME = SKILL_NAMES[idx];
			this.START_LVL = Skills.getRealLevel(idx);
			this.START_XP = Skills.getExperience(idx);
		}
		
		public final boolean isValid() {
			return (Skills.getExperience(IDX) - START_XP) != 0;
		}
		
		public final double getPercent() {
			final int CurXp = Skills.getExperience(IDX);
			final int CurLvl = Skills.getRealLevel(IDX);
			if(CurLvl == 99) {
				return 100;
			}
			return (double)((double)(CurXp - Skills.XP_TABLE[CurLvl]) / (double)(Skills.XP_TABLE[CurLvl + 1] - Skills.XP_TABLE[CurLvl]));
		}
		
		public final String getPaintLineInfo() throws Exception {
			final int CurXp = Skills.getExperience(IDX);
			final int CurXpGain = CurXp - START_XP;
			final int CurLvl = Skills.getRealLevel(IDX);
			final int LvlsGained = CurLvl - START_LVL;
			final double XpPerHour = (((double) CurXpGain / (double) RUN_TIME.getElapsed()) * 3600000d);
			long TTL = 0;
			if(CurLvl != 99) {							
				TTL = (long) ((RUN_TIME.getElapsed() * (Skills.XP_TABLE[CurLvl+1] - CurXp)) / CurXpGain);
			}						
			return NAME + " (" + CurLvl + ")" +  (LvlsGained == 0 ? "" : "+" + LvlsGained) 
					+ " " + formatter(CurXpGain) + " - " + formatter((int)XpPerHour) + "/H - TTL: " + Time.format(TTL);			
		}
		
		public final String formatter(final int num) {
			return num / 1000 + "." + (num % 1000) / 100 + "K"; 
		}
	}
	
	private static final Color OPAQUE_BLACK = new Color(0, 0, 0, 160);
	private static final Color OPAQUE_BLACK2 = new Color(0, 0, 0, 180);
	
	public final void drawSkillPaint(Graphics g, final String topRightText) {
		try {
			int found = 0;
			if(SKILL_LIST[Skills.MINING].isValid()) drawSkillInfo(g, Skills.MINING, found++);		
			found = 370 - (found * 22);
			g.setColor(OPAQUE_BLACK);		
			g.fillRect(0, found, 290, 22);
			shadowText(g, "v1.8  Runtime: " + RUN_TIME.toElapsedString() + "    " + topRightText, 20, found+18);
		} catch (Exception ignored) {}
	}
	
	private final void drawSkillInfo(Graphics g, final int idx, final int found) throws Exception {
		final int yComp = 370 - (found * 22);
		final int xComp = 20;
		final Color CurColor = new Color(COLORS[found].getRed(), COLORS[found].getGreen(), COLORS[found].getBlue(), 180);
				
		g.setColor(OPAQUE_BLACK);
		g.fillRect(0, yComp, 290, 22);				
		g.setColor(Color.BLACK);
		g.draw3DRect(xComp-5, yComp+3, 270, 17, true);
		g.setColor(OPAQUE_BLACK2);
		g.fill3DRect(xComp-5, yComp+3, 270, 17, true);
		g.setColor(CurColor);
		g.fillRect(xComp-4, yComp+4, (int) (268 * SKILL_LIST[idx].getPercent()), 15);
		shadowText(g, SKILL_LIST[idx].getPaintLineInfo(), xComp, yComp+16);
	}
	
	
	public static final void shadowText(Graphics g, final String line, final int x, final int y) {
		g.setColor(Color.BLACK);
		g.drawString(line, x+1, y+1);
		g.setColor(Color.WHITE);
		g.drawString(line, x, y);		
	}
}
