package f2pRuneCrafter;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Branch;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

import f2pRuneCrafter.data.Master;
import f2pRuneCrafter.nodes.Banking;
import f2pRuneCrafter.nodes.Craft;
import f2pRuneCrafter.nodes.Doors;
import f2pRuneCrafter.nodes.Walk;

@Manifest(authors = { "Battleguard" }, description = "AIO free to play runecrafter", name = "AIO F2P Runecrafter")
public class Runecrafter extends ActiveScript implements PaintListener {
	
    private final Queue<Node> bankNodes = new ConcurrentLinkedQueue<>();
    private final Queue<Node> craftNodes = new ConcurrentLinkedQueue<>();
	
	private String currentNodeName = "null";
	private Master rune = Master.AIR;	
	
	@Override
	public void onStart() {		
		rune = Master.AIR;		
		
		Node toAlter = Walk.alterPathInstance(rune);
		Node enterAlter = Doors.enterAlterInstance(rune);
		Node crafting = new Craft(rune.alter(), rune.area(), rune.rune());
		
		Node bank = new Banking(rune.rune(), rune.area());
		Node toBank = Walk.bankPathInstance(rune);
		Node exitAlter = Doors.exitAlterInstance(rune);
		
		craftNodes.addAll(Arrays.asList(crafting, enterAlter, toAlter));
		bankNodes.addAll(Arrays.asList(bank, exitAlter, toBank));
	}
	
	
	@Override
	public int loop() {
		final Queue<Node> tree = Inventory.contains(rune.rune().essenceId()) ? craftNodes : bankNodes;
		for (Node node : tree) {
			if(node.activate()) {
				currentNodeName = node.getClass().getName();
				node.execute();
				return 50;
			}
		}
		return 50;
	}


	@Override
	public void onRepaint(Graphics g) {		
		g.drawString("Current Node: " + currentNodeName, 10, 10);
		
		
		final int[] ids = {rune.alter().alterId(), rune.alter().insideDoorId(), rune.alter().outsideDoorId()};
		final Area[] areas = {rune.area().bank(), rune.area().insiderAlter(), rune.area().outsideAlter()};		
		
		
		final SceneObject[] objects = SceneEntities.getLoaded(ids);
		for (SceneObject sceneObject : objects) {
			sceneObject.draw(g);
		}
		for (Area area : areas) {
			for (Tile tile : area.getTileArray()) {
				tile.draw(g);
			}
		}
		for (Tile tile : rune.path().toBank().toArray()) {
			tile.draw(g);
		}
	}

}
