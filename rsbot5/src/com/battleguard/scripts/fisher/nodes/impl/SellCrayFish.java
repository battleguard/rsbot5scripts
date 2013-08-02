package com.battleguard.scripts.fisher.nodes.impl;

import org.powerbot.script.methods.MethodContext;

import com.battleguard.scripts.fisher.debug.DebugMethodProvider;
import com.battleguard.scripts.fisher.util.TradeWindow;
import com.battleguard.scripts.fisher.util.TradeWindow.SellAmount;

public class SellCrayFish extends DebugMethodProvider {

	private final TradeWindow tradeWindow;	
	
	public SellCrayFish(MethodContext ctx) {
		super(ctx);
		tradeWindow = new TradeWindow(ctx);
	}

	@Override
	public void execute() {		
		if(!isInventoryEmpty()) {
			if(tradeWindow.isSellTabOpen()) {
				logMessage("selling fish");
				tradeWindow.sellItem("Raw crayfish", SellAmount.FIFTY);
			} else {
				logMessage("opening sell tab");
				tradeWindow.openSellTab();
			}
		} else {
			logMessage("closing trade window");
			tradeWindow.close();
		}
	}

	@Override
	public boolean activate() {
		return tradeWindow.isOpen();
	}
	
	private boolean isInventoryEmpty() {
		return ctx.backpack.select().count() == 0;
	}

	@Override
	protected void writeDebugLines() {
		writeDebugLine("tradeWindow.isOpen:  " + tradeWindow.isOpen());
		writeDebugLine("tradeWindow.isSellTabOpen:  " + tradeWindow.isSellTabOpen());
		writeDebugLine("tradeWindow.isSlotValid:  " + tradeWindow.isSlotValid());
	}
}
