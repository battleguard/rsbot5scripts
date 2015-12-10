package CorpseMage;

import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Time;

public class SkillData {
	public static final int [] idx = {Skills.CONSTITUTION, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENSE, Skills.RANGE, Skills.MAGIC};
	
	public final Skill[] SKILL_LIST = {new Skill(idx[0]), new Skill(idx[1]), new Skill(idx[2]), new Skill(idx[3]), new Skill(idx[4]), new Skill(idx[5])};
	
	public static final String[] SKILL_NAMES = { "attack", "defence", "strength", "constitution", "range", "prayer", 
		  "magic", "cooking", "woodcutting", "fletching", "fishing", "firemaking", "crafting", "smithing", "mining", 
		  "herblore", "agility", "thieving", "slayer", "farming", "runecrafting", "hunter", "construction", 
		  "summoning", "dungeoneering", "-unused-" };
	
	public SkillData() {}
	
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
			final int CUR_XP = Skills.getExperience(IDX);
			final int CUR_LVL = Skills.getRealLevel(IDX);
			return (double)((double)(CUR_XP - Skills.XP_TABLE[CUR_LVL]) / (double)(Skills.XP_TABLE[CUR_LVL + 1] - Skills.XP_TABLE[CUR_LVL]));
		}
		
		public final String getPaintLineInfo() throws Exception {
			final int CUR_XP = Skills.getExperience(IDX);
			final int CUR_XP_GAIN = CUR_XP - START_XP;
			final int CUR_LVL = Skills.getRealLevel(IDX);
			final int LVLS_GAINED = CUR_LVL - START_LVL;
			final double XP_PER_HOUR = (((double) CUR_XP_GAIN / (double) VAR.RUN_TIME.getElapsed()) * 3600000d);
			final long TTL = (long) ((VAR.RUN_TIME.getElapsed() * (Skills.XP_TABLE[CUR_LVL+1] - CUR_XP)) / CUR_XP_GAIN);
			
			return NAME + " (" + CUR_LVL + ")" +  (LVLS_GAINED == 0 ? "" : "+" + LVLS_GAINED) 
					+ " " + Paint.formatter(CUR_XP_GAIN) + " - " + Paint.formatter((int)XP_PER_HOUR) + "/H - TTL: " + Time.format(TTL);			
		}
	}
	
}
