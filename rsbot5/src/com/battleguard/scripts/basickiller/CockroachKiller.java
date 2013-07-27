package com.battleguard.scripts.basickiller;


import java.awt.Graphics;
import java.util.Iterator;

import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.wrappers.Npc;

@Manifest(authors = { "Battleguard" }, description = "Warped Cockroach Killer", name = "Warped Cockroach Killer")
public class CockroachKiller extends PollingScript implements PaintListener {

	private static int WARPED_COCK_ROACH_ID = 7913;		
	
	@Override
	public int poll() {		
		if(!ctx.players.local().isInCombat()) {
			Iterator<Npc> iterator = ctx.npcs.select().id(WARPED_COCK_ROACH_ID).nearest().iterator();
			if(iterator.hasNext()) {
				final Npc roach = iterator.next();
				if(roach.interact("Attack", roach.getName())) {
					sleep(1000);
				}				
			}
		}
		return 50;
	}

	@Override
	public void repaint(Graphics g) {
	}

}
