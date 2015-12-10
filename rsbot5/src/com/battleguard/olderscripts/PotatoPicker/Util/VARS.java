package PotatoPicker.Util;

import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.TilePath;

public class VARS {

	
	
	public static final Area POTATO_AREA = new Area(new Tile(3139, 3267, 0), new Tile(3158, 3292, 0));
	
	
	public static final Tile[] TO_POTATOES_TILES = { new Tile(3092, 3245, 0), new Tile(3094, 3248, 0), new Tile(3100, 3249, 0), 
		new Tile(3106, 3253, 0), new Tile(3113, 3257, 0), new Tile(3119, 3260, 0), 
		new Tile(3127, 3262, 0), new Tile(3132, 3266, 0), new Tile(3134, 3272, 0), 
		new Tile(3134, 3277, 0), new Tile(3134, 3284, 0), new Tile(3134, 3289, 0), 
		new Tile(3139, 3293, 0), new Tile(3144, 3293, 0), new Tile(3145, 3293, 0) };	
	
	
	public static final TilePath TO_POTATOES_PATH = new TilePath(TO_POTATOES_TILES);
	public static final TilePath TO_BANK_PATH = new TilePath(TO_POTATOES_TILES).reverse();
	
	
	public static final int GATE_CLOSED_ID = 45208;
	public static final int GATE_OPEN_ID = 45209;
	public static final int POTATO_PLANT_ID = 312;
	public static final int POTATO_ID = 1942;
	
}
