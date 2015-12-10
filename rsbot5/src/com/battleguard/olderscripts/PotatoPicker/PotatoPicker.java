package PotatoPicker;

import java.awt.Graphics;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.util.Timer;

import PotatoPicker.Nodes.*;
import PotatoPicker.Util.*;

@Manifest(authors = { "battleguard" }, description = "potato", name = "potato")
public class PotatoPicker extends ActiveScript implements PaintListener {
	
	
	private static final Node[] jobs = {new Singer(), new Gate(), new Banker(), new ToPotatoes(), new Picker()};
	private final InventoryCounter inventoryCounter = new InventoryCounter(VARS.POTATO_ID, getContainer());
	
	private static String CurrentJob = "Nothing";
	
	@Override
	public int loop() {		
		for (Node job : jobs) {
			if(job.activate()) {
				CurrentJob = job.getClass().getName();
				job.execute();
				return 200;
			}
		}		
		CurrentJob = "Nothing";
		return 50;
	}

	private static final Timer runTime = new Timer(0);
	
	@Override
	public void onRepaint(Graphics g) {
		try {
			Debug.drawLine(g, "Current Job: " + CurrentJob, 0, 20);
			Debug.drawArea(g, VARS.POTATO_AREA);
			Debug.drawGateStatus(g);	
			Debug.drawTiles(g, VARS.TO_POTATOES_TILES);
			g.drawString("runTime: " + runTime.toElapsedString(), 10, 80);
			inventoryCounter.draw(g, 300, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	


	
}
