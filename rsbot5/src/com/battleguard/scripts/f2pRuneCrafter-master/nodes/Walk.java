package f2pRuneCrafter.nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.map.TilePath;

import f2pRuneCrafter.data.Master;

public class Walk extends Node {

	private final TilePath path;
	final Timer nextStep = new Timer(1000);
	
	private Walk(final TilePath path) {
		this.path = path;
	}
	
	public static Walk bankPathInstance(Master master) {
		return new Walk(master.path().toBank());
	}
	
	public static Walk alterPathInstance(Master master) {
		return new Walk(master.path().toAlter());
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
