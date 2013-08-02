package com.battleguard.scripts.fisher.util;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.Widget;

public class TradeWindow extends MethodProvider {
	private static final int WINDOW_WIDGET_ID = 1265;
	private static final int SELL_TAB_COMPONENT_ID = 230;	
	private static final int SELL_SLOTS_COMPONENT_ID = 20;
	private static final int CLOSE_COMPONENT_ID = 87;
	
	private static final int SELL_TAB_SELECTED_TEXT_COLOR = 16248247;
	
	private final Widget window;
	private final Component sellTab;
	private final Component sellSlotOne;
	private final Component closeButton;
	
	public TradeWindow(MethodContext ctx) {	
		super(ctx);
		window = ctx.widgets.get(WINDOW_WIDGET_ID);
		sellTab = window.getComponent(SELL_TAB_COMPONENT_ID);
		sellSlotOne = window.getComponent(SELL_SLOTS_COMPONENT_ID).getChild(1);
		closeButton = window.getComponent(CLOSE_COMPONENT_ID);
	}

	public boolean isOpen() {
		return window.isValid();
	}
	
	public boolean isSellTabOpen() {
		return sellTab.isValid() && sellTab.getTextColor() == SELL_TAB_SELECTED_TEXT_COLOR;
	}
	
	public boolean isSlotValid() {
		return sellSlotOne.isValid();
	}
	
	public boolean close() {
		final Timer wait = new Timer(2000);
		if(closeButton.isValid() && closeButton.interact("Close")) {
			while(wait.isRunning() && closeButton.isValid()) {
				sleep(50);
			}
		}
		return closeButton.isValid();
	}
	
	public boolean openSellTab() {
		final Timer wait = new Timer(2000);
		if(!isSellTabOpen() && sellTab.interact("Sell")) {
			while(wait.isRunning() && !isSellTabOpen()) {
				sleep(50);
			}
		}
		return isSellTabOpen();
	}
	
	public void sellItem(String itemName, SellAmount amount) {
		if(isSellTabOpen()) {
			sellSlotOne.interact("Sell " + amount.getValue(), itemName);
			sleep(2000);
		}		
	}
	
	public enum SellAmount {
		ONE(1), FIVE(5), TEN(10), FIFTY(50), FIVE_HUNDRED(500);
		
		private final int amount;
		
		SellAmount(int amount) {
			this.amount = amount;
		}
		
		public int getValue() {
			return amount;
		}				
	}	
}

