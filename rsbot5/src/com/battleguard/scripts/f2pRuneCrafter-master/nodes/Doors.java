package f2pRuneCrafter.nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.node.SceneObject;

import f2pRuneCrafter.data.Master;

public class Doors extends Node {

	private final int doorId;
	private final Area doorArea;
	
	private Doors(final int doorId, final Area doorArea) {
		this.doorId = doorId;
		this.doorArea = doorArea;
	}
	
	public static Doors enterAlterInstance(Master master) {
		return new Doors(master.alter().outsideDoorId(), master.area().outsideAlter());
	}
	
	public static Doors exitAlterInstance(Master master) {
		return new Doors(master.alter().insideDoorId(), master.area().insiderAlter());
	}
	
	@Override
	public boolean activate() {
		return doorArea.contains(Players.getLocal());
	}

	@Override
	public void execute() {
		final SceneObject door = SceneEntities.getNearest(doorId);
		if(door == null) return;
		final Timer t = new Timer(3000);
		if(door.isOnScreen() && door.click(true)) {
			while(t.isRunning() && doorArea.contains(Players.getLocal())) {
				sleep(50);
			}
		} else if(Walking.walk(door)) {
			while(t.isRunning() && Players.getLocal().isMoving()) {
				sleep(50);
			}
		}
	}

}
