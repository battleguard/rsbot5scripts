package com.battleguard.scripts.f2prunecrafter.data;

public enum Master {
	AIR(Alters.AIR, Paths.AIR, Runes.AIR, Musicians.AIR),
	MIND(Alters.MIND, Paths.MIND, Runes.MIND, Musicians.MIND),
	WATER(Alters.WATER, Paths.WATER, Runes.WATER, Musicians.WATER),
	EARTH(Alters.EARTH, Paths.EARTH, Runes.EARTH, Musicians.EARTH),
	FIRE(Alters.FIRE, Paths.FIRE, Runes.FIRE, Musicians.FIRE),
	BODY(Alters.BODY, Paths.BODY, Runes.BODY, Musicians.BODY);	
	
	private final Alters alter;
	private final Paths path;
	private final Runes rune;
	private final Musicians musician;
	
	private Master(final Alters alter, final Paths path, final Runes rune, final Musicians musician) {
		this.alter = alter;
		this.path = path;
		this.rune = rune;
		this.musician = musician;
	}
	
	public Alters alter() {
		return alter;
	}
	
	public Paths path() {
		return path;
	}
	
	public Runes rune() {
		return rune;
	}
	
	public Musicians musician() {
		return musician;
	}
}
