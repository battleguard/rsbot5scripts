package com.battleguard.scripts.f2prunecrafter.data;

import org.powerbot.script.wrappers.Tile;

import com.battleguard.scripts.f2prunecrafter.wrappers.Area;

public enum Areas {
	AIR(new Area(new Tile(3181,3432,0), new Tile(3190,3447,0)),
		new Area(new Tile(3124,3402,0), new Tile(3131,3410,0)),
		new Area(new Tile(2836,4826,0), new Tile(2851,4842,0))),
	MIND(new Area(new Tile(0,0,0), new Tile(0,0,0)),
		new Area(new Tile(0,0,0), new Tile(0,0,0)),
		new Area(new Tile(0,0,0), new Tile(0,0,0))),
	WATER(new Area(new Tile(0,0,0), new Tile(0,0,0)),
		new Area(new Tile(0,0,0), new Tile(0,0,0)),
		new Area(new Tile(0,0,0), new Tile(0,0,0))),
	EARTH(new Area(new Tile(0,0,0), new Tile(0,0,0)),
		new Area(new Tile(0,0,0), new Tile(0,0,0)),
		new Area(new Tile(0,0,0), new Tile(0,0,0))),
	FIRE(new Area(new Tile(0,0,0), new Tile(0,0,0)),
		new Area(new Tile(0,0,0), new Tile(0,0,0)),
		new Area(new Tile(0,0,0), new Tile(0,0,0))),
	BODY(new Area(new Tile(0,0,0), new Tile(0,0,0)),
		new Area(new Tile(0,0,0), new Tile(0,0,0)),
		new Area(new Tile(0,0,0), new Tile(0,0,0)));
	
	private final Area bank;
	private final Area outsideAlter;
	private final Area insideAlter;
	
	Areas(final Area bank, final Area outsideAlter, final Area insideAlter) {
		this.bank = bank;
		this.outsideAlter = outsideAlter;
		this.insideAlter = insideAlter;
	}
	
	public Area bank() {
		return bank;
	}
	
	public Area outsideAlter() {
		return outsideAlter;
	}
	
	public Area insiderAlter() {
		return insideAlter;
	}
}
