package PotatoPicker.Nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.DepositBox;

import PotatoPicker.Util.VARS;

public class Banker extends Node {

	@Override
	public boolean activate() {
		return Inventory.isFull();
	}

	@Override
	public void execute() {		
		if(DepositBox.open()) {
			DepositBox.depositInventory();
			sleep(500);
			DepositBox.close();
		} else {					
			VARS.TO_BANK_PATH.traverse();
		}
		sleep(1000);
	}
	
	
}
