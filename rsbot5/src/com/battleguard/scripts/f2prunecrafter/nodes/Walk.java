package com.battleguard.scripts.f2prunecrafter.nodes;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.TilePath;
import org.powerbot.script.wrappers.Path.TraversalOption;

import com.battleguard.scripts.f2prunecrafter.data.Master;

public class Walk extends MethodProvider implements Node {

	private final TilePath path;
	final Timer nextStep = new Timer(1000);
	
	private Walk(final TilePath path, MethodContext ctx) {
		super(ctx);
		this.path = path;
	}
	
	public static Walk bankPathInstance(Master master, MethodContext ctx) {
		return new Walk(master.path().toBank(), ctx);
	}
	
	public static Walk alterPathInstance(Master master, MethodContext ctx) {
		return new Walk(master.path().toAlter(), ctx);
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
