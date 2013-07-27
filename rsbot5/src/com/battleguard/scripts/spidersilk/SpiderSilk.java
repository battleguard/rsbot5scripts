package SpiderSilk;

import java.awt.Graphics;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.Manifest;

import SpiderSilk.node.DepositItems;
import SpiderSilk.node.Fighter;
import SpiderSilk.node.ItemCounter;
import SpiderSilk.node.Looter;
import SpiderSilk.node.ToSpiders;
import SpiderSilk.util.Constants;


@Manifest(authors = { "Battleguard" }, name = "Spider Silk Collector", description = "Loots Spider Silk For Mad Money") 
public final class SpiderSilk extends ActiveScript implements PaintListener {		
	
	private String currentNodeName = "null";	
	private final ItemCounter spiderSilkCounter = new ItemCounter(Constants.SPIDER_SILK_ID);		
	private final Node[] nodes = {new DepositItems(), new ToSpiders(), new Looter() ,new Fighter()};				
			
	@Override
	public int loop() {		
		for (Node job : nodes) {
			if(job.activate()) {
				currentNodeName = job.getClass().getName();
				job.execute();
				return 200;
			}
		}		
		return 200;
	}		
	
	@Override
	public void onRepaint(Graphics g) {
		g.drawString(currentNodeName, 10, 10);		
		spiderSilkCounter.draw(g, 10, 50);
	}
}
