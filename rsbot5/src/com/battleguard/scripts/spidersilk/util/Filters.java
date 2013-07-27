package SpiderSilk.util;

import org.powerbot.game.api.util.Filter;

import SpiderSilk.filter.CorpseSpider;
import SpiderSilk.filter.InCombat;
import SpiderSilk.filter.SpiderSilk;

@SuppressWarnings("rawtypes")
public enum Filters {
	CORPSE_SPIDER(new CorpseSpider()),
	IN_COMBAT(new InCombat()),
	SPIDER_SILK(new SpiderSilk());
	
	private final Filter filter;
	
	Filters(Filter filter) {
		this.filter = filter;
	}
	
	public Filter getFilter() {
		return filter;
	}
}
