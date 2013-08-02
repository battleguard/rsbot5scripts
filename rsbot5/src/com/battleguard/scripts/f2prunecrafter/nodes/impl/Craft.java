package com.battleguard.scripts.f2prunecrafter.nodes.impl;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;

import com.battleguard.scripts.f2prunecrafter.data.Alters;

import com.battleguard.scripts.f2prunecrafter.data.Runes;
import com.battleguard.scripts.f2prunecrafter.nodes.Node;

public class Craft extends MethodProvider implements Node {

	private final int alterId;
	
	public Craft(MethodContext ctx, Alters alter) {
		super(ctx);
		this.alterId = alter.alterId();
	}	
	
	
	@Override
	public boolean activate() {
		return !ctx.objects.select().id(alterId).isEmpty();
	}

	@Override
	public void execute() {		
		final GameObject alter = ctx.objects.iterator().next();		
		final Timer wait = new Timer(3000);
		if(alter.isOnScreen() && alter.click(true)) {
			while(wait.isRunning() && !ctx.backpack.select().id(Runes.ESSENCE_ID).isEmpty()) {
				sleep(50);
			}
		} else if(ctx.movement.stepTowards(alter)) {
			while(wait.isRunning() && ctx.players.local().isInMotion()) {
				sleep(50);
			}
		}
	}
}
