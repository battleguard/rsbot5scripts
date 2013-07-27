package com.battleguard.scripts.basickiller;


import java.awt.Color;
import java.awt.Graphics;

import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.lang.Filter;
import org.powerbot.script.wrappers.GroundItem;
import org.powerbot.script.wrappers.Npc;
import org.powerbot.script.wrappers.Player;

import com.battleguard.scripts.basickiller.Filters.InCombat;
import com.battleguard.scripts.basickiller.Nodes.Fighter;
import com.battleguard.scripts.basickiller.Nodes.Looter;
import com.battleguard.scripts.basickiller.Nodes.Node;

@Manifest(authors = { "Battleguard" }, description = "Warped Cockroach Killer", name = "Warped Cockroach Killer")
public class CockroachKiller extends PollingScript implements PaintListener {
	
	private final Node[] nodes = {new Fighter(ctx)};
	
	@Override
	public int poll() {			
		for (Node node : nodes) {
			if(node.activate()) {
				node.execute();
				return 50;
			}
		}		
		return 50;
	}		
	
	

	@Override
	public void repaint(Graphics g) {
		
		
//		for (GroundItem loot : ctx.groundItems) {			
//			g.drawPolygon(loot.getLocation().getMatrix(ctx).getBounds());
//		}
	}

}
