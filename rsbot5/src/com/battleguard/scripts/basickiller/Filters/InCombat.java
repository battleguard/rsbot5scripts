package com.battleguard.scripts.basickiller.Filters;

import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.wrappers.Npc;

public class InCombat extends MethodProvider implements Filter<Npc> {
	
	public InCombat(MethodContext arg0) {
		super(arg0);
	}

	@Override
	public boolean accept(Npc npc) {
		return npc.getInteracting().equals(ctx.players.local());
	}

}
