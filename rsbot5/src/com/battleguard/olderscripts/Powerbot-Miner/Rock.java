package Miner;

import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class Rock {
	public final Tile RockTile;
	private final int RockId;
	private long rockTimer = System.currentTimeMillis();
	public long priorityTime = 0;
	public boolean rockUp = true;
	private long spawnLength  = 180 * 1000;
	public final String rockName;
	
	public Rock(Tile tile, int id, String name) {		
		RockTile = tile;
		RockId = id;
		rockName = name;
		
		int idx = 0;
		for(int i = MiningGUI.ORE_TYPES.length - 1; i >= 0; i--) {
			if(MiningGUI.ORE_TYPES[i].equals(rockName)) {
				priorityTime = idx * 3000;
				if(MiningGUI.ORE_TYPES[i].equals("Copper") ) {
					priorityTime -= 3000;
				} else if(MiningGUI.ORE_TYPES[i].equals("Adamantite")) {
					priorityTime -= 1500;
				}
			}
			idx++;
		}
		rockTimer = System.currentTimeMillis();
	}
	
	public final void update(){
		SceneObject obj = SceneEntities.getAt(RockTile);
		boolean oldState = rockUp;
		rockUp = (obj != null && obj.getId() == RockId);
		if(rockUp && !oldState) {
			spawnLength = System.currentTimeMillis() - rockTimer;
		}			
		if(rockUp) {
			rockTimer = System.currentTimeMillis();
		}
	}
	
	public final long getPriorityTimer() {
		final long walkTime = (long) (Calculations.distanceTo(RockTile) * 900);
		if(rockUp) return priorityTime + walkTime;
		return priorityTime + walkTime + Math.abs(spawnLength - (System.currentTimeMillis() - rockTimer));
	}
	
	public final long getRemaining() {
		if(rockUp) return 0;
		return Math.abs(spawnLength - (System.currentTimeMillis() - rockTimer));
	}
}