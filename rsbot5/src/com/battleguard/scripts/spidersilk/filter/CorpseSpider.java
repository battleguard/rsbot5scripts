package SpiderSilk.filter;

import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.util.Filter;

import static SpiderSilk.util.Constants.SPIDER_ID;

public class CorpseSpider implements Filter<NPC> {	

	@Override
	public boolean accept(NPC npc) {
		return npc.getId() == SPIDER_ID && npc.validate() && npc.getHealthPercent() == 100
				&& npc.getAnimation() == -1 && npc.getInteracting() == null && npc.getModel() != null;
	}
}
