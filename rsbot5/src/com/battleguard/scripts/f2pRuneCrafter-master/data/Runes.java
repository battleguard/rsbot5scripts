package f2pRuneCrafter.data;

public enum Runes {
	AIR(556,1436,5527),
	MIND(0,1436,0),
	WATER(0,1436,0),
	EARTH(0,1436,0),
	FIRE(0,1436,0),
	BODY(0,1436,0);	
	
	private final int runeId;
	private final int essenceId;	
	private final int tiaraId;		
	
	Runes(final int runeId, final int essenceId, final int tiaraId) {
		this.runeId = runeId;
		this.essenceId = essenceId;
		this.tiaraId = tiaraId;				
	}
	
	public int runeId() {
		return runeId;
	}
	
	public int essenceId() {
		return essenceId;
	}
	
	public int tiaraId() {
		return tiaraId;
	}
	
	public static Runes valueOf(Names name) {
		return valueOf(name.toString());
	}
}
