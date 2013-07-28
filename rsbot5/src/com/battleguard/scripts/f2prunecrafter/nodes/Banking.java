package com.battleguard.scripts.f2prunecrafter.nodes;

import org.powerbot.script.methods.Bank.Amount;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;

import com.battleguard.scripts.f2prunecrafter.data.Master;

import com.battleguard.scripts.f2prunecrafter.wrappers.Area;

public class Banking extends MethodProvider implements Node {

	private final int essenceId;
	private final Area bankArea;
	
	public Banking(Master master, MethodContext ctx) {
		super(ctx);
		this.essenceId = master.rune().essenceId();
		this.bankArea = master.area().bank();
	}
	
	@Override
	public boolean activate() {
		return bankArea.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		if(ctx.bank.open()) {
			ctx.bank.depositInventory();
			ctx.bank.withdraw(essenceId, Amount.ALL);
		}	
	}	
}
