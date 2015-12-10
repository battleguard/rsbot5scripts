package PotatoPicker.Nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;

import PotatoPicker.Util.VARS;

public class ToPotatoes extends Node {

	@Override
	public boolean activate() {		
		return !Inventory.isFull() && !VARS.POTATO_AREA.contains(Players.getLocal());
	}

	@Override
	public void execute() {
		VARS.TO_POTATOES_PATH.traverse();
		sleep(1000);
	}

}
