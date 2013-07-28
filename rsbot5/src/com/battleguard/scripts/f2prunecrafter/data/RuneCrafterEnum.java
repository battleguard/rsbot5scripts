package com.battleguard.scripts.f2prunecrafter.data;

public enum RuneCrafterEnum {
	FIRE,
	WATER,
	AIR,
	EARTH,
	MIND,
	BODY;		
	
	private static final int INSIDE_ALTER_DOOR_BASE_ID = 2476;
	private static final int CRAFTING_ALTER_BASE_ID = 2463;
	private static final int OUTSIDE_ALTER_DOOR_BASE_ID = 2450;
	private static final int RUNE_BASE_ID = 554;
	
	public int alter() {
		return ordinal() + CRAFTING_ALTER_BASE_ID;
	}
	
	public int insideAlterDoor() {
		return ordinal() + INSIDE_ALTER_DOOR_BASE_ID;
	}
	
	public int outsideAlterDoor() {
		return ordinal() + OUTSIDE_ALTER_DOOR_BASE_ID;
	}
	
	public int rune() {
		return ordinal() * 2 + RUNE_BASE_ID;
	}
	
	@Override
	public String toString() {
		return alter() + " , " + insideAlterDoor() + " , " + outsideAlterDoor() + " , " + rune();
	}
	
	public static void main(String[] args) {
		System.out.println(AIR.toString());
		System.out.println(MIND.toString());
	}
}

