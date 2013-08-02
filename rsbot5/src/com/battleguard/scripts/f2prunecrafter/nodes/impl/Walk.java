package com.battleguard.scripts.f2prunecrafter.nodes.impl;


import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.TilePath;

import com.battleguard.scripts.f2prunecrafter.data.Paths;
import com.battleguard.scripts.f2prunecrafter.nodes.Node;

public class Walk extends MethodProvider implements Node {

	private final TilePath path;
	private final Timer nextStep = new Timer(1000);
	
	protected Walk(MethodContext ctx, final TilePath path) {
		super(ctx);
		this.path = path;
	}
	
	public static Walk createBankPathInstance(MethodContext ctx, Paths path) {		
		return new Walk(ctx, new TilePath(ctx, path.tiles()).reverse());
	}
	
	public static Walk createAlterPathInstance(MethodContext ctx, Paths path) {		
		return new Walk(ctx, new TilePath(ctx, path.tiles()));
	}
	
	@Override
	public boolean activate() {
		return !nextStep.isRunning();
	}

	@Override
	public void execute() {		
		path.traverse();
		nextStep.reset();
	}
}
