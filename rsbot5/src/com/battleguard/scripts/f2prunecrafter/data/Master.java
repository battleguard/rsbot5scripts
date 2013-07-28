package com.battleguard.scripts.f2prunecrafter.data;

public enum Master {
	AIR(Alters.AIR, Areas.AIR, Paths.AIR, Runes.AIR, Musicians.AIR),
	MIND(Alters.MIND, Areas.MIND, Paths.MIND, Runes.MIND, Musicians.MIND),
	WATER(Alters.WATER, Areas.WATER, Paths.WATER, Runes.WATER, Musicians.WATER),
	EARTH(Alters.EARTH, Areas.EARTH, Paths.EARTH, Runes.EARTH, Musicians.EARTH),
	FIRE(Alters.FIRE, Areas.FIRE, Paths.FIRE, Runes.FIRE, Musicians.FIRE),
	BODY(Alters.BODY, Areas.BODY, Paths.BODY, Runes.BODY, Musicians.BODY);	
	
	private final Alters alter;
	private final Areas area;
	private final Paths path;
	private final Runes rune;
	private final Musicians musician;
	
	private Master(final Alters alter, final Areas area, final Paths path, final Runes rune, final Musicians musician) {
		this.alter = alter;
		this.area = area;
		this.path = path;
		this.rune = rune;
		this.musician = musician;
	}
	
	public Alters alter() {
		return alter;
	}
	
	public Areas area() {
		return area;
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
