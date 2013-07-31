package com.battleguard.scripts.f2prunecrafter.nodes.impl;

import org.powerbot.script.methods.Bank.Amount;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;

import com.battleguard.scripts.f2prunecrafter.data.Master;
import com.battleguard.scripts.f2prunecrafter.nodes.Node;

public class Banking extends MethodProvider implements Node {

	private final int essenceId;
	
	public Banking(Master master, MethodContext ctx) {
		super(ctx);
		this.essenceId = master.rune().essenceId();
	}
	
	
	@Override
	public boolean activate() {
		return ctx.bank.isOnScreen();		
	}

	@Override
	public void execute() {
		if(ctx.bank.open()) {
			ctx.bank.depositInventory();
			ctx.bank.withdraw(essenceId, Amount.ALL);
		}	
	}	
}
