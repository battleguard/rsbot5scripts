package CorpseMage;

import org.powerbot.concurrent.Task;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Timer;

import GrandExchange.GELookup;

public class AllLoot {
	public static int  Gain = 0, Loss = 0, Total = 0;
	public static final  Timer runtime = new Timer(0);
	public static Loot[] LOOT_LIST;
	public static Loot[] HIGH_ALCH_LIST;
	public static int[] LOOT_ID_LIST, HIGH_ALCH_ID_LIST;
	public static boolean HIGH_ALCH;
	
	
	public AllLoot() {		
		HIGH_ALCH = Skills.getRealLevel(Skills.MAGIC) > 54;
		final Loot[] temp = {new Loot(VAR.COINS_ID), new Loot(VAR.THREAD_ID), new Loot(VAR.FEATHER_ID), new Loot(VAR.AIR_RUNE_ID), new Loot(VAR.FIRE_RUNE_ID), new Loot(VAR.MIND_RUNE_ID), 
				new Loot(VAR.BRONZE_ARROW_ID), new Loot(VAR.FISHING_BAIT_ID),  new Loot(VAR.WATER_TALISMAN_ID), new Loot(VAR.FIRE_TALISMAN_ID), new Loot(VAR.NATURE_RUNE_ID)};			
		LOOT_LIST = temp;
		LOOT_ID_LIST = new int[LOOT_LIST.length];
		for(int i = 0; i < LOOT_LIST.length; i++) {
			LOOT_ID_LIST[i] = LOOT_LIST[i].id;
		}
		
		if(HIGH_ALCH) {
			final Loot[] temp2 = {new Loot(VAR.AIR_STAFF_ID), /*new Loot(GOLD_AMULET_ID), new Loot(VAR.GOLD_NECKLACE_ID), new Loot(VAR.GOLD_RING_ID),*/ new Loot(VAR.DEFENSE_AMULET_ID), new Loot(VAR.MAGIC_AMULET_ID)};;
			HIGH_ALCH_LIST = temp2;
			
			HIGH_ALCH_ID_LIST = new int[HIGH_ALCH_LIST.length];
			for(int i = 0; i < HIGH_ALCH_LIST.length; i++) {
				HIGH_ALCH_ID_LIST[i] = HIGH_ALCH_LIST[i].id;
			}
			
			LOOT_ID_LIST = new int[LOOT_ID_LIST.length + HIGH_ALCH_LIST.length];
			for(int i = 0; i < LOOT_ID_LIST.length; i++) {
				if(i >= HIGH_ALCH_ID_LIST.length) {
					LOOT_ID_LIST[i] = LOOT_LIST[i - HIGH_ALCH_ID_LIST.length].id;
				} else {
					LOOT_ID_LIST[i] = HIGH_ALCH_ID_LIST[i];
				}
			}
		}	
	}
	

	public void itemAlched(final int ID) {
		for (Loot curitem : HIGH_ALCH_LIST) {
			if(curitem.id == ID) {
				curitem.collected++;
				return;
			}
		}
	}
	
	
	public static void update() {
		int temp = Gain = Total = Loss = 0;
		for (Loot curloot : LOOT_LIST) {
			temp = curloot.update();
			if(temp > 0) Gain += temp;
			else Loss += temp;
		}
		Total = Gain + Loss;
	}
	

	
	public class Loot {
		public final int id;
		public String name = "loading...";
		public int startamount;
		public int collected = 0;
		public int price = 0;
		public int worth = 0;
		
		
		public Loot(final int id) {
			this.id = id;
			if(VAR.COINS_ID == id) {
				name = "Coins";
				startamount = 0;
				price = 1;
				return;
			}
			startamount = Inventory.getCount(true, id);
		}
		
		public int update() {				
			if(id != VAR.COINS_ID) collected = Inventory.getCount(true, id) - startamount;
			return worth = collected * price;				
		}
	}
	
	
	public static class PriceLoader implements Task{
		
		@Override
		public void run() {
	    	for(int i = 0; i < LOOT_LIST.length; i++) {
	    		if(LOOT_LIST[i].id == 995) continue;
	    		final String[] info = GELookup.lookup(LOOT_LIST[i].id);	    		
	    		LOOT_LIST[i].name = info[0];
	    		LOOT_LIST[i].price = Integer.parseInt(info[1]);
	    	}
	    	if(!HIGH_ALCH) return;
	    	for(int i = 0; i < HIGH_ALCH_LIST.length; i++) {
	    		final String[] info = GELookup.lookup(HIGH_ALCH_LIST[i].id);
	    		HIGH_ALCH_LIST[i].name = info[0];
	    		HIGH_ALCH_LIST[i].price = Integer.parseInt(info[1]);
	    	}
		}		
	}
}

