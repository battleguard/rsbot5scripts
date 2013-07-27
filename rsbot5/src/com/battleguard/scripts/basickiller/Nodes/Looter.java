package com.battleguard.scripts.basickiller.Nodes;



import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.GroundItem;

import com.battleguard.scripts.basickiller.CockroachKiller;
import com.battleguard.scripts.basickiller.Filters.LootFilter;

public class Looter extends Node {
	
	public Looter(MethodContext arg0) {
		super(arg0);
	}
	
	private final Filter<GroundItem> lootFilter = new LootFilter(ctx);	


	@Override
	public void execute() {			
		final GroundItem loot = ctx.groundItems.iterator().next();
		if(loot.isOnScreen() && loot.interact("Take", loot.getName())) {
			sleep(100);
		} else if(ctx.mouse.move(loot)) {
			sleep(1000);
		}		
	}

	@Override
	public boolean activate() {		
		return !ctx.players.local().isInCombat() && ctx.groundItems.select().select(lootFilter).iterator().hasNext();
	}

}
