package com.battleguard.scripts.f2prunecrafter.nodes.impl;

import org.powerbot.script.methods.Bank.Amount;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;

import com.battleguard.scripts.f2prunecrafter.data.Runes;
import com.battleguard.scripts.f2prunecrafter.nodes.Node;

public class Banking extends MethodProvider implements Node {
	
	public Banking(MethodContext ctx) {
		super(ctx);
	}	
	
	@Override
	public boolean activate() {
		return ctx.bank.isOnScreen();		
	}

	@Override
	public void execute() {
		if(ctx.bank.open()) {
			ctx.bank.depositInventory();
			ctx.bank.withdraw(Runes.ESSENCE_ID, Amount.ALL);
		}	
	}	
}
