package SpiderSilk.filter;

import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.util.Filter;

import static SpiderSilk.util.Constants.SPIDER_ID;

public class InCombat implements Filter<NPC> {

	@Override
	public boolean accept(NPC npc) {
		return npc.getId() == SPIDER_ID && npc.getInteracting() != null
				&& npc.getInteracting().equals(Players.getLocal()) && npc.getModel() != null
				&& npc.getHealthPercent() > 0;
	}
}