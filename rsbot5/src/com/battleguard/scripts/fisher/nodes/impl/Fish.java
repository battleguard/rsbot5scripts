package com.battleguard.scripts.fisher.nodes.impl;

import java.awt.Graphics;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Npc;

import com.battleguard.scripts.fisher.debug.DebugMethodProvider;

public class Fish extends DebugMethodProvider {

	private static final int CRAY_FISH_POOL = 14907;	
	private Npc fishingPool;	
	
	public Fish(MethodContext ctx) {
		super(ctx);
		fishingPool = ctx.npcs.getNil();
	}

	@Override
	public void execute() {
		final Timer wait = new Timer(2000);
		if(ctx.npcs.iterator().hasNext()) {
			fishingPool = ctx.npcs.iterator().next();
			if(fishingPool.isOnScreen() && fishingPool.interact("Cage", "Fishing spot")) {
				logMessage("interacting with fishspot");
				while(wait.isRunning() && !isPlayerFishing()) {
					sleep(50);
				}
			} else if(ctx.movement.stepTowards(fishingPool)) {
				logMessage("walking to fishspot");
				while(wait.isRunning() && ctx.players.local().isInMotion()) {
					sleep(50);
				}
			}
		}
	}

	@Override
	public boolean activate() {
		return !isPlayerFishing() && fishingSpotFound() || !fishingPool.isValid(); 
	}
	
	private boolean isPlayerFishing() {
		return ctx.players.local().getAnimation() != -1;
	}
	
	private boolean fishingSpotFound() {
		return !ctx.npcs.select().id(CRAY_FISH_POOL).nearest().isEmpty();
	}

	@Override
	protected void writeDebugLines() {
		writeDebugLine("isPlayerFishing:  " + isPlayerFishing());
		writeDebugLine("fishingSpotFound:  " + fishingSpotFound());
		writeDebugLine("fishingPool.isValid:  " + fishingPool.isValid());
	}
	
	@Override
	protected void draw(Graphics g) {
		if(fishingPool.isValid()) {
			fishingPool.getLocation().getMatrix(ctx).draw(g);
		}
	}
}

