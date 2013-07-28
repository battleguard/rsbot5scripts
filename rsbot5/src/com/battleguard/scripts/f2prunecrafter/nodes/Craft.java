package com.battleguard.scripts.f2prunecrafter.nodes;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;

import com.battleguard.scripts.f2prunecrafter.data.Master;
import com.battleguard.scripts.f2prunecrafter.wrappers.Area;

public class Craft extends MethodProvider implements Node {

	private final int alterId;
	private final Area alterArea;
	private final int essenceId;
	
	public Craft(Master master, MethodContext ctx) {
		super(ctx);
		this.alterId = master.alter().alterId();
		this.alterArea = master.area().insiderAlter();
		this.essenceId = master.rune().essenceId();
	}	
	
	@Override
	public boolean activate() {
		return alterArea.contains(ctx.players.local()) && !ctx.objects.select().id(alterId).nearest().first().isEmpty();
	}

	@Override
	public void execute() {		
		GameObject alter = ctx.objects.iterator().next();		
		final Timer t = new Timer(3000);
		if(alter.isOnScreen() && alter.click(true)) {
			while(t.isRunning() && !ctx.backpack.select().id(essenceId).isEmpty()) {
				sleep(50);
			}
		} else if(ctx.movement.stepTowards(alter)) {
			while(t.isRunning() && ctx.players.local().isInMotion()) {
				sleep(50);
			}
		}
	}

}
