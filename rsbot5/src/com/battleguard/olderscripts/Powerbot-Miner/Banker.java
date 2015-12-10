package Miner;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.DepositBox;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Banker extends Node {

	private static final int DEPOSIT_BOX_WIDGET = 11;
	private static final int INVENTORY_SPOTS_WIDGET = 17;
	private static final int DEPOSIT_WIDGET = 19;
	private static final int CLOSE_WIDGET = 15;
	private static final Tile DEPOSIT_TILE = new Tile(1042, 4578, 0);
	private static final int RANDOM_BOX_ID = 14664;
	private static final int[] SPIN_TICKET_IDS = {24154, 24155};
	public static final int[] KEEP_IDS = {CoalBag.COAL_BAG_ID, 1265, 1267, 1269, 1271, 1273, 1275, 15259};
	

	
	@Override
	public final void execute() {
		RockTimer.state = "Walking";
		if (isDepoOpen()) {
			if (!isEmpty()) {
				if(Mine.depositAll) {
					depositAll();					
				} else {
					deposit();
				}												
			} else {
				close();
				Task.sleep(1000);
				if(CoalBag.isValid() && CoalBag.bagFull) {
					CoalBag.withdrawCoal();
					Task.sleep(1000);
					DepositBox.open();
					Task.sleep(1000);
				}
			}
		} else {
			
			if(Inventory.getCount(RANDOM_BOX_ID) > 0) {
				RockTimer.state = "Dropping Box";
				Drop.drop(true, RANDOM_BOX_ID);
			}
			
			final Item spinTicket = Inventory.getItem(SPIN_TICKET_IDS);
			if(spinTicket != null) {
				RockTimer.state = "Clicking on Spin Ticket";
				spinTicket.getWidgetChild().click(true);
				Task.sleep(2000);
			}
			
			
			if (Inventory.isFull()) {
				RockTimer.state = "Walking to Deposit Box";
				final SceneObject Deposit = SceneEntities.getAt(DEPOSIT_TILE);
				if (Calculations.distanceTo(Deposit) > 4) {
					Walking.walk(Deposit);
				} else {
					Deposit.interact("Deposit");
				}
			} else {
				RockTimer.state = "Walking to mine";
				Walking.walk(RockTimer.miningArea.getCentralTile());
			}
		}
		Task.sleep(1000);
	}

	public static final boolean isEmpty() {
		final WidgetChild spots = Widgets.get(DEPOSIT_BOX_WIDGET, INVENTORY_SPOTS_WIDGET);
		if (spots != null) {
			MainLoop: for (WidgetChild spot : spots.getChildren()) {
				if (spot.getChildId() != -1) {
					if(!Mine.depositAll) {
						for (int pick : KEEP_IDS) {
							if(spot.getChildId() == pick) {
								continue MainLoop;
							}
						}
					}
					return false;
				}				
			}
		}
		return true;
	}
	
	@Override
	public final boolean activate() {
		final Player Char = Players.getLocal();
		final WidgetChild DepositBox = Widgets.get(11, 0);
		return DepositBox.validate() || Inventory.isFull()
				|| !RockTimer.miningArea.contains(Char.getLocation());
	}

	public static final boolean isDepoOpen() {
		return Widgets.get(DEPOSIT_BOX_WIDGET, 0).validate();
	}
	
	
	public static final void depositAll() {
		RockTimer.state = "Depositing All";
		Mouse.click(Widgets.get(DEPOSIT_BOX_WIDGET, DEPOSIT_WIDGET).getNextViewportPoint(), true);
	}
	
	public static final void close() {
		RockTimer.state = "Closing Deposit Box";
		Mouse.click(Widgets.get(DEPOSIT_BOX_WIDGET, CLOSE_WIDGET).getNextViewportPoint(), true);
	}
	
	/**
	 * Waits till an animation sequence is done being performed by your local character.
	 * <p>The function will wait till a timer counts down from the <b>timeoutMS</b> as the starting time.
	 * <br>The timer will be reset whenever the local player has the same animation as the <b>animationID</b> passed.
	 * @param timeoutMS : max interval in milliseconds to wait before returning
	 * @param animationID : ID that is checked against the local players animation 
	 */
	public static final void waitTillAnimationDone(final long timeoutMS, final int animationID) {
		final Timer resetTimer = new Timer(timeoutMS);
		while(resetTimer.isRunning()) {
			if(Players.getLocal().getAnimation() == animationID) {
				resetTimer.reset();				
			}
			Task.sleep(50);
		}
	}
	
	public static final void deposit() {
		final WidgetChild spots = Widgets.get(DEPOSIT_BOX_WIDGET, INVENTORY_SPOTS_WIDGET);
		if (spots != null) {
			MainLoop: for (WidgetChild spot : spots.getChildren()) {
				if (spot.getChildId() != -1) {
					for (int pick : KEEP_IDS) {
						if(spot.getChildId() == pick) {
							continue MainLoop;
						}
					}
					RockTimer.state = "Depositing " + (new Item(spot).getName());
					spot.interact("Deposit-All");
					Task.sleep(1000);
				}
			}
		}
	}



}
