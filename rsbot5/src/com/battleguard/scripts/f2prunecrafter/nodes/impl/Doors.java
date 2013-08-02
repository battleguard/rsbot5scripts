package com.battleguard.scripts.f2prunecrafter.nodes.impl;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;

import com.battleguard.scripts.f2prunecrafter.data.Alters;
import com.battleguard.scripts.f2prunecrafter.nodes.Node;

public class Doors extends MethodProvider implements Node {

	private final int doorId;
	private final int distance;
	
	protected Doors(MethodContext ctx, final int doorId, final int distance) {
		super(ctx);
		this.doorId = doorId;
		this.distance = distance;
	}
	
	public static Doors createEnterAlterInstance(MethodContext ctx, Alters alter) {
		return new Doors(ctx, alter.outsideDoorId(), 5);
	}
	
	public static Doors createExitAlterInstance(MethodContext ctx, Alters alter) {
		return new Doors(ctx, alter.insideDoorId(), Integer.MAX_VALUE);
	}
	
	
	@Override
	public boolean activate() {		
		return !ctx.objects.select().id(doorId).within(distance).isEmpty();
	}	

	@Override
	public void execute() {				
		final GameObject door = ctx.objects.iterator().next();
		final Timer wait = new Timer(3000);
		if(door.isOnScreen() && door.click(true)) {
			while(wait.isRunning() && door.isOnScreen()) {
				sleep(50);
			}
		} else if(ctx.movement.stepTowards(door)) {
			while(wait.isRunning() && ctx.players.local().isInMotion()) {
				sleep(50);
			}
		}
	}
}
