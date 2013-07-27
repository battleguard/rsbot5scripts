package com.battleguard.scripts.basickiller.Filters;

import java.awt.Graphics;

import org.powerbot.event.PaintListener;
import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.wrappers.Area;
import org.powerbot.script.wrappers.GroundItem;
import org.powerbot.script.wrappers.Tile;

public class LootFilter extends MethodProvider implements Filter<GroundItem> {

	private static final Area ROACH_AREA = new Area(new Tile(3968, 5550, 0), new Tile(3976, 5556, 0));
	private static final int[] LOOT_IDS = { 995, 557, 558, 313, 1734, 556, 314, 882, 554 };

	public LootFilter(MethodContext arg0) {
		super(arg0);
	}

	@Override
	public boolean accept(GroundItem loot) {
		if(ROACH_AREA.contains(loot)) {
			for (int id : LOOT_IDS) {
				if (id == loot.getId())
					return true;
			}
		}		
		return false;
	}

}
