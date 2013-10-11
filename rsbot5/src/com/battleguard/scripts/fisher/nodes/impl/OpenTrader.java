package com.battleguard.scripts.fisher.nodes.impl;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Npc;

import com.battleguard.scripts.fisher.debug.DebugMethodProvider;

public class OpenTrader extends DebugMethodProvider {

	private static final int TRADER = 14879;
	private Npc trader;
	
	public OpenTrader(MethodContext ctx) {
		super(ctx);
		trader = ctx.npcs.getNil();
	}	
	
	@Override
	public void execute() {			
		if(ctx.npcs.iterator().hasNext()) {
			trader = ctx.npcs.iterator().next();
			if(trader.isOnScreen()) {
				logMessage("interacting with trader");
				trader.interact("Trade", trader.getName());
			} else {
				logMessage("Walking to trader");
				ctx.movement.stepTowards(trader);
			}
			sleep(2000);
		}		
	}

	@Override
	public boolean activate() {
		return !isPlayerMoving() && isInventoryFull() && !ctx.npcs.select().id(TRADER).isEmpty();
	}
	
	public boolean isInventoryFull() {
		return ctx.backpack.select().count() == 28;
	}
	
	public boolean isPlayerMoving() {
		return ctx.players.local().isInMotion();
	}
	
	@Override
	protected void writeDebugLines() {
		writeDebugLine("backPathSize:  " + ctx.backpack.select().count());
		writeDebugLine("isPlayerMoving:  " + isPlayerMoving());
	}
}
