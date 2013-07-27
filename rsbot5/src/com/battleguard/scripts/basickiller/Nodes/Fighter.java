package com.battleguard.scripts.basickiller.Nodes;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Npc;

public class Fighter extends Node {

	private static int WARPED_COCK_ROACH_ID = 7913;
	
	public Fighter(MethodContext arg0) {
		super(arg0);
	}

	@Override
	public void execute() {		
		final Npc roach = ctx.npcs.iterator().next();
		if(roach.interact("Attack", roach.getName())) {
			sleep(50);
		}
	}

	@Override
	public boolean activate() {
		return !ctx.players.local().isInCombat() && ctx.npcs.select().id(WARPED_COCK_ROACH_ID).nearest().iterator().hasNext();
	}

}
