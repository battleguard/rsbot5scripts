package f2pRuneCrafter.nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Bank.Amount;
import org.powerbot.game.api.wrappers.Area;

import f2pRuneCrafter.data.Areas;
import f2pRuneCrafter.data.Runes;

public class Banking extends Node {

	private final Runes rune;
	private final Area bankArea;
	
	public Banking(Runes rune, Areas area) {
		this.rune = rune;
		this.bankArea = area.bank();
	}
	
	@Override
	public boolean activate() {
		return bankArea.contains(Players.getLocal());
	}

	@Override
	public void execute() {
		if(Bank.open()) {
			Bank.depositInventory();
			Bank.withdraw(rune.essenceId(), Amount.ALL);
		}		
	}	
}
