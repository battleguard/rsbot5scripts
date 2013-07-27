package com.battleguard.scripts.basickiller.Nodes;

import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Npc;
import org.powerbot.script.wrappers.Player;

import com.battleguard.scripts.basickiller.Filters.InCombat;

public class Fighter extends Node {

	private static int WARPED_COCK_ROACH_ID = 7913;
	
	private final Filter<Npc> inCombatFilter = new InCombat(ctx);	
	
	public Fighter(MethodContext ctx) {
		super(ctx);
		
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
		final Player local = ctx.players.local();		
		return local.getInteracting() == null && !local.isInMotion() && ctx.npcs.select().id(WARPED_COCK_ROACH_ID).nearest().iterator().hasNext();
	}

}
