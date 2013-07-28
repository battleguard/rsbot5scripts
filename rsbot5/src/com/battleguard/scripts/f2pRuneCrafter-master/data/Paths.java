package f2pRuneCrafter.data;

import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.TilePath;

public enum Paths {
	AIR(new Tile(3184, 3435, 0), new Tile(3185, 3432, 0), new Tile(3181, 3430, 0), 
			new Tile(3177, 3429, 0), new Tile(3172, 3429, 0), new Tile(3169, 3428, 0), 
			new Tile(3166, 3426, 0), new Tile(3160, 3420, 0), new Tile(3157, 3419, 0), 
			new Tile(3153, 3417, 0), new Tile(3148, 3417, 0), new Tile(3144, 3413, 0), 
			new Tile(3141, 3410, 0), new Tile(3138, 3408, 0), new Tile(3134, 3406, 0), 
			new Tile(3131, 3403, 0), new Tile(3129, 3404, 0)),
	MIND(new Tile(0,0,0)),
	WATER(new Tile(0,0,0)),
	EARTH(new Tile(0,0,0)),
	FIRE(new Tile(0,0,0)),
	BODY(new Tile(0,0,0));		
		
	
	private final TilePath toBank;
	private final TilePath toAlter;
	
	Paths(Tile... tiles) {				
		toAlter = new TilePath(tiles);
		toBank = new TilePath(tiles).reverse();
	}

	public TilePath toBank() {
		return toBank;
	}
	
	public TilePath toAlter() {
		return toAlter;
	}
	
	public static Paths valueOf(Names name) {
		return valueOf(name.toString());
	}
	
	
}
