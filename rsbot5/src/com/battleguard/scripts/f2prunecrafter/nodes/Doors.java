package com.battleguard.scripts.f2prunecrafter.nodes;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;

import com.battleguard.scripts.f2prunecrafter.data.Master;
import com.battleguard.scripts.f2prunecrafter.wrappers.Area;

public class Doors extends MethodProvider implements Node {

	private final int doorId;
	private final Area doorArea;
	
	private Doors(MethodContext ctx, final int doorId, final Area doorArea) {
		super(ctx);
		this.doorId = doorId;
		this.doorArea = doorArea;
	}
	
	public static Doors enterAlterInstance(Master master, MethodContext ctx) {
		return new Doors(ctx, master.alter().outsideDoorId(), master.area().outsideAlter());
	}
	
	public static Doors exitAlterInstance(Master master, MethodContext ctx) {
		return new Doors(ctx, master.alter().insideDoorId(), master.area().insiderAlter());
	}
	
	@Override
	public boolean activate() {
		return doorArea.contains(ctx.players.local()) && !ctx.objects.select().id(doorId).nearest().first().isEmpty();
	}

	@Override
	public void execute() {				
		GameObject door = ctx.objects.iterator().next();
		final Timer t = new Timer(3000);
		if(door.isOnScreen() && door.click(true)) {
			while(t.isRunning() && doorArea.contains(ctx.players.local())) {
				sleep(50);
			}
		} else if(ctx.movement.stepTowards(door)) {
			while(t.isRunning() && ctx.players.local().isInMotion()) {
				sleep(50);
			}
		}
	}

}
