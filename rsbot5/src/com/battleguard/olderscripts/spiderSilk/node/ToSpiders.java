package SpiderSilk.node;


import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.node.SceneObject;

import SpiderSilk.util.LoadStone;
import SpiderSilk.util.Location;
import SpiderSilk.util.Path;


public final class ToSpiders extends Node {

	private static final int[]  DOOR_IDS = {48797, 48682, 48688};
	
	@Override
	public void execute() {				
		if(Location.EDGEVILLE.contains(Players.getLocal())) {			
			LoadStone.LUMBRIDGE.teleport();
			return;
		}
		
		final SceneObject door = SceneEntities.getNearest(DOOR_IDS);
		
		if(door != null && Calculations.distanceTo(door) < 5) {
			if(!door.isOnScreen() || !door.click(true)) {
				Camera.turnTo(door);
			}
			sleep(1000);
		} else {
			if(Location.LUMBRIDGE.contains(Players.getLocal())) {				
				Path.OUTSIDE_DUNGEON.traverse();
			} else {
				Walking.walk(Location.SPIDER_DEN.getCentraltile());	
			}
		}							
	}
	
	
	@Override
	public boolean activate() {		
		return !Location.SPIDER_DEN.contains(Players.getLocal());
	}
	
	
}
