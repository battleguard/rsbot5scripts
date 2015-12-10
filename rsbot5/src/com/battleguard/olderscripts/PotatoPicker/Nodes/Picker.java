package PotatoPicker.Nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.SceneObject;

import PotatoPicker.Util.VARS;
import SpiderSilk.Util.Methods;

public class Picker extends Node {

	@Override
	public boolean activate() {
		final Player player = Players.getLocal();
		return Players.getLocal().getAnimation() == -1 && !player.isMoving() && !Inventory.isFull()
				&& VARS.POTATO_AREA.contains(player);
	}

	@Override
	public void execute() {
		final SceneObject plant = SceneEntities.getNearest(VARS.POTATO_PLANT_ID);
		if (plant != null) {
			if (Methods.isOnScreen(plant)) {
				plant.interact("Pick");
			} else {
				Walking.walk(plant);
			}
			sleep(1000);
		}
	}

}
