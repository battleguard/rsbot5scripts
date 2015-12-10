package CorpseMage;

import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.TilePath;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Banker {
	
	private static final Area CORPSE_MAGE_AREA = new Area(new Tile(3988, 5476, 0), new Tile(4004, 5490, 0));	
	private static final Area LUMBRIDGE_AREA = new Area(new Tile(3227, 3193, 0), new Tile(3251, 3227, 0));
	private static final Area EDGEVILLE_AREA = new Area(new  Tile(3064, 3487, 0), new Tile(3100, 3508, 0));
	
	private final static Tile[] TO_MAGE_TILES = {new Tile(3972,5564, 0), new Tile(3972, 5553, 0), 
		new Tile(3984, 5553, 0), new Tile(3991, 5542, 0), new Tile(4002, 5544, 0),
			new Tile(4015, 5544, 0), new Tile(4022, 5533, 0), new Tile(4024, 5519, 0), 
			new Tile(4017, 5514, 0), new Tile(4016, 5506, 0), new Tile(4008, 5495, 0), 
			new Tile(3995, 5496, 0), new Tile(3994, 5484, 0), new Tile(3995, 5480, 0)};
	
	private final static Tile[] TO_DUNG_TILES = {new Tile(3233, 3221, 0), new Tile(3236, 3212, 0), 
		new Tile(3236, 3208, 0), new Tile(3240, 3291, 0), new Tile(3246, 3198, 0)};    
	
	private final static Tile[] TO_BANK_TILES = {new Tile(3067, 3505, 0), new Tile(3073, 3503, 0), 
		new Tile(3081, 3501, 0), new Tile(3089, 3499, 0), new Tile(3094, 3496, 0)};
	
	private static final TilePath TO_DUNG_PATH = new TilePath(TO_DUNG_TILES);
	private static final TilePath TO_MAGE_PATH = new TilePath(TO_MAGE_TILES);
	private static final TilePath TO_BANK_PATH = new TilePath(TO_BANK_TILES);	

	
	public static final void teleport() {
		Tabs.MAGIC.open();
		final WidgetChild port = Widgets.get(192, 24);
		if(port != null) port.click(true);
	}
	
	public static final void depositTalismans() {
		Paint.action = "depositing talismans";
		final int WATER_TAL = Inventory.getCount(VAR.WATER_TALISMAN_ID);
		final int FIRE_TAL = Inventory.getCount(VAR.FIRE_TALISMAN_ID);
		Bank.deposit(VAR.WATER_TALISMAN_ID, 0);
		Time.sleep(1500);
		Bank.deposit(VAR.FIRE_TALISMAN_ID, 0);
		Time.sleep(1500);
		
		for(int i = 0; i < AllLoot.LOOT_LIST.length; i++) {
			if(AllLoot.LOOT_LIST[i].id == VAR.WATER_TALISMAN_ID) {
				if(AllLoot.LOOT_LIST[i].startamount > 0) AllLoot.LOOT_LIST[i].startamount = 0;
				AllLoot.LOOT_LIST[i].startamount -= WATER_TAL - Inventory.getCount(VAR.WATER_TALISMAN_ID);
			} else 	if(AllLoot.LOOT_LIST[i].id == VAR.FIRE_TALISMAN_ID) {
				if(AllLoot.LOOT_LIST[i].startamount > 0) AllLoot.LOOT_LIST[i].startamount = 0;
				AllLoot.LOOT_LIST[i].startamount -= FIRE_TAL - Inventory.getCount(VAR.FIRE_TALISMAN_ID);
			}
		}
	}
	
	
	public static final int bank() {		
		
		if(Bank.isOpen()) {			
			if(Inventory.getCount(VAR.WATER_TALISMAN_ID, VAR.FIRE_TALISMAN_ID) > 0) depositTalismans();
			else Bank.close();
			return 2000;
		}
		
		final Tile loc = Players.getLocal().getLocation();			
		
					
		if(Inventory.isFull() && Inventory.getItem(AllLoot.HIGH_ALCH_ID_LIST) == null) {
			if(EDGEVILLE_AREA.contains(loc)) {
				if(!Bank.open()) {
					Paint.action = "TO_BANK_PATH.traverse()";
					TO_BANK_PATH.traverse();
				}
				return 2000;
			} else {
				Paint.action = "EDGE_PORT.doClick()";
				
				teleport();
				Time.sleep(1500);
				final WidgetChild EDGE_PORT = Widgets.get(1092, 45);
				if(EDGE_PORT != null) EDGE_PORT.click(true);
				Time.sleep(5000);
				while(Players.getLocal().getAnimation() == 16385); {
					Time.sleep(1000);
				}
				return 1000;
			}
		} else {
			if(CORPSE_MAGE_AREA.contains(loc)) return 0;
			
			if(EDGEVILLE_AREA.contains(loc)) {
				Paint.action = "LUMBRIDGE_PORT.doClick()";
				teleport();
				Time.sleep(1500);
				final WidgetChild LUMBRIDGE_PORT = Widgets.get(1092, 47);
				if(LUMBRIDGE_PORT != null) LUMBRIDGE_PORT.click(true);
				Time.sleep(5000);
				while(Players.getLocal().getAnimation() == 16385); {
					Time.sleep(1000);
				}
				return 1000;
			}
			
			if(loc.getPlane() == 1) {
				final SceneObject obj = SceneEntities.getAt(3874, 5527);
				Paint.action = "Climb down " + obj.getId();
				obj.interact("Climb");
				return 2000;
			}
			
			final SceneObject doors = SceneEntities.getNearest(48797, 48688);
			if(doors != null && Calculations.distanceTo(doors) < 5) {			
				if(doors.isOnScreen()) {
					Paint.action = "clicking door " + doors.getId();
					doors.click(true);
				} else {
					Paint.action = "turning to door " + doors.getId();
					Camera.turnTo(doors);
				}
				return 2000;
			}
						
			if(LUMBRIDGE_AREA.contains(loc)) {
				Paint.action = "Walking to dungeon";
				TO_DUNG_PATH.traverse();
			} else {
				Paint.action = "Walking to mages";
				TO_MAGE_PATH.traverse();
			}
		}
		return 1000;
	}
}
