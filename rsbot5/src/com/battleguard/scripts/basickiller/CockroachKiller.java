package com.battleguard.scripts.basickiller;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GroundItem;
import org.powerbot.script.wrappers.Npc;
import org.powerbot.script.wrappers.Player;

import com.battleguard.scripts.basickiller.Enums.Path;
import com.battleguard.scripts.basickiller.Filters.InCombat;
import com.battleguard.scripts.basickiller.Nodes.Fighter;
import com.battleguard.scripts.basickiller.Nodes.Looter;
import com.battleguard.scripts.basickiller.Nodes.Node;

@Manifest(authors = { "Battleguard" }, description = "Warped Cockroach Killer", name = "Warped Cockroach Killer")
public class CockroachKiller extends PollingScript implements PaintListener {
	
	private final Node[] nodes = {new Fighter(ctx)};
	
	private static int NPC_ID = 7913;
	private String currentAction = "nothing";
	
	@Override
	public int poll() {
		for (Npc roach : ctx.npcs.select().select(fightFilter).nearest().first()) {
			currentAction = "attacking npc";
			if(roach.interact("Attack", roach.getName())) {
				System.out.println("clicking");
				final Timer t = new Timer(2000);
				while(t.isRunning() && roach.isValid() && roach.getAnimation() != 8788) {
					if(roach.getAnimation() != -1) t.reset();
					currentAction = "Killing npc " + roach.getAnimation();
					sleep(50);
				}
			}
		}
		currentAction = "going after next roach";
		return 50;
	}		
	
	
	Filter<Npc> fightFilter = new Filter<Npc>() {
		
		public boolean accept(Npc npc) {
			return npc.getId() == NPC_ID && npc.getInteracting() == null && npc.getAnimation() == -1;
		}
		
	};		
	
//	public class InCombat extends MethodProvider implements Filter<Npc> {
//		
//		public InCombat(MethodContext arg0) {
//			super(arg0);
//		}	
//		
//		private static int WARPED_COCK_ROACH_ID = 7913;
//		
//		@Override
//		public boolean accept(Npc npc) {
//			return npc.getId() == WARPED_COCK_ROACH_ID && npc.getInteracting() != null
//					&& npc.getInteracting().equals(ctx.players.local()) && npc.getModel() != null
//					&& npc.getHealthPercent() > 0;
//		}
//
//	}
	

	@Override
	public void repaint(Graphics g) {
//		g.setColor(Color.RED);
//		g.setFont(new Font("Arial", Font.BOLD, 20));
//		for (Npc npc : ctx.npcs.select()) {
//			if(npc.isOnScreen()) {
//				g.drawPolygon(npc.getLocation().getMatrix(ctx).getBounds());
//				g.drawString("" + npc.getAnimation(), npc.getCenterPoint().x, npc.getCenterPoint().y);
//			}			
//		}
		
//		for (GroundItem loot : ctx.groundItems) {			
//			g.drawPolygon(loot.getLocation().getMatrix(ctx).getBounds());
//		}
	}

}
