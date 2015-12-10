package Miner;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;

public class CoalBag extends Node {
	public static final int COAL_BAG_ID = 18339;
	private static final int COAL_ID = 453;
	
	private static final String EMTPY_MSG = "Your coal bag is empty.";	
	private static final String FULL_MSG = "Your coal bag is already full.";
	
	private static boolean inUse = false;
	public static boolean bagFull = false;

	public static final boolean setup() {
		return inUse = Inventory.getCount(COAL_BAG_ID) > 0;
	}
	
	public static final boolean isValid() {
		return inUse;
	}

	public static final void withdrawCoal() {
		RockTimer.state = "Withdrawing Coal";
		final Item CoalBag = Inventory.getItem(COAL_BAG_ID);
		if (CoalBag != null) {
			if(CoalBag.getWidgetChild().interact("Withdraw-many")) {
				bagFull = false;
			} else {
				checkBag();
			}
			Task.sleep(1000);
		}
	}

	public static final void checkBag() {
		RockTimer.state = "Checking Bag";
		final Item CoalBag = Inventory.getItem(COAL_BAG_ID);
		if (CoalBag != null) {
			CoalBag.getWidgetChild().click(true);
		}
	}

	public static final void putCoalInBag() {
		RockTimer.state = "Adding coal to bag";
		final Item CoalBag = Inventory.getItem(COAL_BAG_ID);
		final Item Coal = Inventory.getItem(COAL_ID);
		if (CoalBag != null && Coal != null) {
			Coal.getWidgetChild().click(true);
			Task.sleep(1000);
			CoalBag.getWidgetChild().click(true);
			Task.sleep(1000);
		}
	}

	public static final void checkMessage(final MessageEvent msg) {
		if (msg.getId() == 0) {
			if (msg.getMessage().equals(FULL_MSG)) {
				bagFull = true;
			}
			if (msg.getMessage().equals(EMTPY_MSG)) {
				bagFull = false;
			}
		}
	}

	@Override
	public boolean activate() {
		return !Banker.isDepoOpen() && Inventory.isFull() && !bagFull;
	}

	@Override
	public void execute() {
		putCoalInBag();
	}

}
