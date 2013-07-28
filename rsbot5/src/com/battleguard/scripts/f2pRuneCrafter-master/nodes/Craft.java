package f2pRuneCrafter.nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.node.SceneObject;

import f2pRuneCrafter.data.Alters;
import f2pRuneCrafter.data.Areas;
import f2pRuneCrafter.data.Runes;

public class Craft extends Node {

	private final int alterId;
	private final Area alterArea;
	private final int essenceId;
	
	public Craft(final Alters alter, final Areas area, final Runes rune) {
		this.alterId = alter.alterId();
		this.alterArea = area.insiderAlter();
		this.essenceId = rune.essenceId();
	}	
	
	@Override
	public boolean activate() {
		return alterArea.contains(Players.getLocal());
	}

	@Override
	public void execute() {
		final SceneObject alter = SceneEntities.getNearest(alterId);
		if(alter == null) return;
		final Timer t = new Timer(3000);
		if(alter.isOnScreen() && alter.click(true)) {
			while(t.isRunning() && !Inventory.contains(essenceId)) {
				sleep(50);
			}
		} else if(Walking.walk(alter)) {
			while(t.isRunning() && Players.getLocal().isMoving()) {
				sleep(50);
			}
		}
	}

}
