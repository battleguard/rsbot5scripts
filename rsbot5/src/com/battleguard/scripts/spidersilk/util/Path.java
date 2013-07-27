package SpiderSilk.util;

import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.TilePath;

public enum Path {
	OUTSIDE_DUNGEON(new Tile(3233, 3221, 0), new Tile(3236, 3212, 0), 
			new Tile(3236, 3208, 0), new Tile(3240, 3291, 0), new Tile(3246, 3198, 0)),
	INSIDE_DUNGEON(new Tile(3972,5564, 0), new Tile(3972, 5553, 0), 
			new Tile(3984, 5553, 0)),
	EDGVILLE_BANK(new Tile(3067, 3505, 0), new Tile(3073, 3503, 0), 
			new Tile(3081, 3501, 0), new Tile(3089, 3499, 0), new Tile(3094, 3496, 0));	
	
	private final TilePath path;
	
	public boolean traverse() {
		return path.traverse();
	}
	
	Path(Tile... tiles) {		
		path = new TilePath(tiles);		
	}		
}
