package f2pRuneCrafter.data;

public enum Names {
	AIR,
	MIND,
	WATER,
	EARTH,
	FIRE,
	BODY;
	
	public Alters alter() {
		return Alters.valueOf(this);
	}
	
	public Areas area() {
		return Areas.valueOf(this);
	}
	
	public Paths path() {
		return Paths.valueOf(this);
	}
	
	public Runes rune() {
		return Runes.valueOf(this);
	}
}

