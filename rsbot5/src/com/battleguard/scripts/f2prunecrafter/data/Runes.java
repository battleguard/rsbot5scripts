package com.battleguard.scripts.f2prunecrafter.data;

public enum Runes {
	AIR(556,1436),
	MIND(558,1436),
	WATER(0,1436),
	EARTH(0,1436),
	FIRE(554,1436),
	BODY(0,1436);	
	
	private final int runeId;
	public static final int ESSENCE_ID = 1436;
	
	Runes(final int runeId, final int essenceId) {
		this.runeId = runeId;
	}
	
	public int runeId() {
		return runeId;
	}
}
