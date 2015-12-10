package SpiderSilk.node;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.interactive.NPC;

import SpiderSilk.util.Filters;


public class Fighter extends Node {
	
	@Override
	public void execute() {
		final NPC corpseSpider = NPCs.getNearest(Filters.CORPSE_SPIDER.getFilter());		
		if (corpseSpider != null) {
			if(!corpseSpider.isOnScreen() || !corpseSpider.interact("Attack")) {
				Walking.walk(corpseSpider);
			}
			sleep(1000);
		}			
	}

	
	@Override
	public boolean activate() {
		return Players.getLocal().getInteracting() == null && !Players.getLocal().isMoving()
				&& NPCs.getNearest(Filters.IN_COMBAT.getFilter()) == null;
	}
	
	
}