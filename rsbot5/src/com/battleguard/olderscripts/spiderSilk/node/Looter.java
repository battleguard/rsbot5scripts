package SpiderSilk.node;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.wrappers.node.GroundItem;

import SpiderSilk.util.Filters;

public class Looter extends Node {
	
	private GroundItem loot;
	
	@Override
	public void execute() {				
		if(loot.isOnScreen()) {
			loot.interact("Take",  "Spider silk");
		} else {
			Walking.walk(loot);
		}			
		sleep(1000);			
	}
		
	@Override
	public boolean activate() {
		return (loot= GroundItems.getNearest(Filters.SPIDER_SILK.getFilter())) != null;
	}


}
