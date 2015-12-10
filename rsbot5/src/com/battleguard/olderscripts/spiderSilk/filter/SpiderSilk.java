package SpiderSilk.filter;

import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.util.Filter;

import static SpiderSilk.util.Constants.SPIDER_SILK_ID;
import SpiderSilk.util.Location;

public class SpiderSilk implements Filter<GroundItem> {
	
	@Override
	public boolean accept(GroundItem groundItem) {
		return groundItem.getId() == SPIDER_SILK_ID && Location.SPIDER_DEN.contains(groundItem);
	}
}