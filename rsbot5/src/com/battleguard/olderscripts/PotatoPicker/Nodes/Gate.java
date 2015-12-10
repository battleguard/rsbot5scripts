package PotatoPicker.Nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

import PotatoPicker.Util.VARS;
import SpiderSilk.Util.Methods;

public class Gate extends Node {

	private static final Tile OUTSIDE_TILE = new Tile(3136, 3293, 0);	
	private static final Tile INSIDE_TILE = new Tile(3148, 3282, 0);

	
	@Override
	public boolean activate() {
		if(Inventory.isFull()) {
			return VARS.POTATO_AREA.contains(Players.getLocal());
		}
		
		final SceneObject gate = SceneEntities.getNearest(VARS.GATE_OPEN_ID, VARS.GATE_CLOSED_ID);
		return gate != null && Calculations.distanceTo(gate) < 10;
	}

	@Override
	public void execute() {	
		final SceneObject gate = SceneEntities.getNearest(VARS.GATE_OPEN_ID, VARS.GATE_CLOSED_ID);
		if(gate != null) {
			if(gate.getId() == VARS.GATE_CLOSED_ID) {
				if(Methods.isOnScreen(gate)) {
					gate.interact("Open");
				} else {
					Walking.walk(gate.getLocation().derive(0, Inventory.isFull() ? -2 : 2));					
				}
			} else {
				Walking.walk(Inventory.isFull() ? OUTSIDE_TILE : INSIDE_TILE);
			}
		}		
	}
	
	
}
