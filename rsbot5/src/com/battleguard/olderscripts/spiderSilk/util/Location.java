package SpiderSilk.util;

import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.Tile;

public enum Location {
	EDGEVILLE(new  Tile(3064, 3487, 0), new Tile(3100, 3508, 0)),
	SPIDER_DEN(new Tile(3978, 5549, 0), new Tile(3995, 5557, 0)),
	LUMBRIDGE(new Tile(3227, 3193, 0), new Tile(3251, 3227, 0));	
	
	private final Area area;
	
	public Area getArea() {
		return area;
	}
	
	public Tile getCentraltile() {
		return area.getCentralTile();
	}
	
	public boolean contains(Locatable loc) {
		return area.contains(loc);
	}
	
	Location(Tile tile1, Tile tile2) {		
		area = new Area(tile1, tile2);	
	}	
}
