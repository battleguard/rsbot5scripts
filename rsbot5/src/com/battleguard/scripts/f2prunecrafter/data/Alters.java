package com.battleguard.scripts.f2prunecrafter.data;

public enum Alters {
	AIR(2478,2465,2452),
	MIND(2479,2466,2453),
	WATER(0,0,0),
	EARTH(0,0,0),
	FIRE(2482,2469,2456),
	BODY(0,0,0);	
	
	private final int alterId;
	private final int insideDoorId;
	private final int outsideDoorId;
	
	Alters(final int alterId, final int insideDoorId, final int outsideDoorId) {
		this.alterId = alterId;
		this.insideDoorId = insideDoorId;
		this.outsideDoorId = outsideDoorId;
	}
	
	public int alterId() {
		return alterId;
	}
	
	public int insideDoorId() {
		return insideDoorId;
	}
	
	public int outsideDoorId() {
		return outsideDoorId;
	}
}
