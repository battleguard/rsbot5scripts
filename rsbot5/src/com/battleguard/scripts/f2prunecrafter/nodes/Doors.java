package com.battleguard.scripts.f2prunecrafter.nodes;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;

import com.battleguard.scripts.f2prunecrafter.data.Master;

public class Doors extends MethodProvider implements Node {

	private final int doorId;
	private final int distance;
	
	private Doors(MethodContext ctx, final int doorId, final int distance) {
		super(ctx);
		this.doorId = doorId;
		this.distance = distance;
	}
	
	public static Doors enterAlterInstance(Master master, MethodContext ctx) {
		return new Doors(ctx, master.alter().outsideDoorId(), 5);
	}
	
	public static Doors exitAlterInstance(Master master, MethodContext ctx) {
		return new Doors(ctx, master.alter().insideDoorId(), Integer.MAX_VALUE);
	}
	
	@Override
	public boolean activate() {		
		return !ctx.objects.select().id(doorId).within(distance).isEmpty();
	}
	


	@Override
	public void execute() {				
		GameObject door = ctx.objects.iterator().next();
		final Timer t = new Timer(3000);
		if(door.isOnScreen() && door.click(true)) {
			while(t.isRunning() && door.isOnScreen()) {
				sleep(50);
			}
		} else if(ctx.movement.stepTowards(door)) {
			while(t.isRunning() && ctx.players.local().isInMotion()) {
				sleep(50);
			}
		}
	}

}
