package com.battleguard.scripts.f2prunecrafter.data;

import org.powerbot.script.wrappers.Tile;

import com.battleguard.scripts.f2prunecrafter.wrappers.Area;

public enum Musicians {
	AIR(8699, new Area(new Tile(3150, 3416, 0), new Tile(3160, 3425, 0))),
	MIND(0,null),
	WATER(0,null),
	EARTH(0,null),
	FIRE(0,null),
	BODY(0,null);	
	
	private final int id;
	private final Area area;
	
	public static int Animation = 11768;
	
	Musicians(final int id, final Area area) {
		this.id = id;
		this.area = area;
	}
	
	public int id() { 
		return id;
	}
	
	public Area area() {
		return area;
	}
	
	
	
}
