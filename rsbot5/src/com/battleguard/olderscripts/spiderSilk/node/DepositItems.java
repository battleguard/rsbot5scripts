package SpiderSilk.node;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

import SpiderSilk.util.LoadStone;
import SpiderSilk.util.Location;
import SpiderSilk.util.Path;

public class DepositItems extends Node {
		
	@Override
	public void execute() {		
		if(Location.EDGEVILLE.contains(Players.getLocal())) {
			if(Bank.open()) {
				Bank.depositInventory();
				Bank.close();
			} else {
				Path.EDGVILLE_BANK.traverse();
			}
		} else {
			LoadStone.EDGEVILLE.teleport();
		}
	}	
	
	@Override
	public boolean activate() {
		return Inventory.isFull() || Bank.isOpen();
	}

	
}
