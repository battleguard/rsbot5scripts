package com.battleguard.scripts.basickiller.Nodes;



import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GroundItem;
import org.powerbot.script.wrappers.Npc;

import com.battleguard.scripts.basickiller.CockroachKiller;
import com.battleguard.scripts.basickiller.Filters.InCombat;
import com.battleguard.scripts.basickiller.Filters.LootFilter;

public class Looter extends Node {
	
	public Looter(MethodContext arg0) {
		super(arg0);
	}
	
	private final Filter<GroundItem> lootFilter = new LootFilter(ctx);
	private final Filter<Npc> inCombatFilter = new InCombat(ctx);	


	@Override
	public void execute() {			
		final GroundItem loot = ctx.groundItems.iterator().next();
		if(loot.interact("Take", loot.getName())) {
			final Timer t = new Timer(2000);
			while(t.isRunning() && loot.isValid()) {
				sleep(50);
			}		
		} else if(ctx.mouse.move(loot)) {
			sleep(1000);
		}		
	}

	@Override
	public boolean activate() {
		return ctx.npcs.select().select(inCombatFilter).isEmpty() && ctx.groundItems.select().select(lootFilter).iterator().hasNext();
	}

}
