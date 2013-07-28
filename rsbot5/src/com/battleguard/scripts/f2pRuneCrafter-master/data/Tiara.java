package f2pRuneCrafter.data;

public enum Tiara {
	AIR(5527),
	MIND(5529),
	WATER(5531),
	EARTH(5535),
	FIRE(5537),
	BODY(5533);	
	
	private final int tiaraId;
	
	private Tiara(final int tiaraId) {
		this.tiaraId = tiaraId;
	}
	
	public int getTiaraId() {
		return tiaraId;
	}
			
}
