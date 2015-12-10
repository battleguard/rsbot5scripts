package CorpseMage;

import java.awt.Graphics;
import java.awt.Point;

import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.bot.event.MessageEvent;
import org.powerbot.game.bot.event.listener.MessageListener;
import org.powerbot.game.bot.event.listener.PaintListener;

@Manifest(name = "corpse mage killer", authors = { "Battleguard" }, description= "Kills Corpse Mages, Loots Items, Alchs Items")
public class CorpseMageKiller extends ActiveScript implements PaintListener, MessageListener {	
	
	//AllLoot allLoot;
	@Override
	protected void setup() {
		Alcher.fireStaffCheck();
		new AllLoot();
		provide(new Cam());
		provide(new Walker());
		provide(new Looter());
		provide(new Fighter());	
		provide(new Alcher());
		submit(new AllLoot.PriceLoader());
	}
	
	
	public static final class Cam extends Strategy implements Task {	
		
		private static Timer cameraTimer  = new Timer(0);
		
		@Override
		public void run() {
			Camera.setPitch(Random.nextInt(90, 100));
			Camera.setAngle(Random.nextInt(340, 380) % 360);
			cameraTimer = new Timer(Random.nextInt(180, 240) * 1000);
		}
		
		@Override
		public boolean validate(){
			return !cameraTimer.isRunning() || Camera.getPitch() < 50;		
		}
	}
	

	public static class Walker extends Strategy implements Task {

		@Override  
		public void run() {}
		
		
		@Override
		public boolean validate(){
			Time.sleep(Banker.bank());
			return false;
		}
	}
	
	public static class Looter extends Strategy implements Task{
		
		private final static Area ROOM = new Area(new Tile(3988, 5476, 0), new Tile(4004, 5490, 0));		
		
		public static final Filter<GroundItem> lootFilter = new Filter<GroundItem>() {
			public boolean accept(GroundItem item) {
				if(!ROOM.contains(item.getLocation())) return false;
				for(int i = 0; i < AllLoot.LOOT_ID_LIST.length; i++) {
					if(AllLoot.LOOT_ID_LIST[i] == item.getId()) {
						if(AllLoot.HIGH_ALCH) {
							for(int j = 0; j < AllLoot.HIGH_ALCH_ID_LIST.length; j++) {
								if(AllLoot.HIGH_ALCH_ID_LIST[j] == item.getId() && (Inventory.isFull() 
										|| Inventory.getCount(VAR.NATURE_RUNE_ID) == 0 || Inventory.getCount(true, VAR.FIRE_RUNE_ID) < 5)) {
									return false;
								}
							}
						}						
						
						if(item.getId() == VAR.BRONZE_ARROW_ID && item.getGroundItem().getStackSize() < 10) return false;
						
						if((item.getId() == VAR.WATER_TALISMAN_ID || item.getId() == VAR.FIRE_TALISMAN_ID) && Inventory.isFull()) return false;
						else return true;
					}
				}
				return false;
			}
		};
		
		public static final void pickup(final GroundItem item) {
			Time.sleep(250);
			Mouse.click(item.getCentralPoint(), false);
			final String []actions = Menu.getItems();
			for(int i = 0; i < actions.length; i++) {
				if(actions[i].contains("Take " + item.getGroundItem().getName()) || actions[i].equals("Cancel")) {
					Mouse.click(new Point(Mouse.getX() + 10, Menu.getLocation().y + 21 + 16 * i + Random.nextInt(3, 6)), true);
					return;
				}
			}			
		}
		
		@Override
		public void run() {			
			final GroundItem item = GroundItems.getNearest(lootFilter);
			if(item == null ) return;
			Paint.action = "looting " + item.getGroundItem().getName();
			if(item.isOnScreen()) pickup(item);
			else Walking.walk(item.getLocation());
			Time.sleep(1000);		
		}
		
		@Override
		public boolean validate(){
			invJunkCheck();
			weaponCheck();
			RunCheck();
			AllLoot.update();
			final Player myChar = Players.getLocal();
			return NPCs.getNearest(Fighter.attackerFilter) == null && myChar.getInteracting() == null && !myChar.isMoving() && GroundItems.getNearest(lootFilter) != null;		
		}
		
		public final Filter<Item> junkFilter = new Filter<Item>() {
			public boolean accept(Item item) {
				for(int i = 0; i < AllLoot.LOOT_ID_LIST.length; i++) {
					if(AllLoot.LOOT_ID_LIST[i] == item.getId() || item.getId() == VAR.FIRE_STAFF_ID || item.getId() == VAR.WEAPON_ID) return false;
				}
				return true;
			}
		};
		
		public void invJunkCheck() {
			Item[] junk = Inventory.getItems(junkFilter);
			for (Item item : junk) {
				item.getWidgetChild().interact("Drop");
			}
		}
		
		public static final void weaponCheck() {
			final Item weapon = Inventory.getItem(VAR.WEAPON_ID);
			if(weapon == null) return; 
			weapon.getWidgetChild().click(true);
			Time.sleep(500);			
		}
		
		public static final void RunCheck() {
			if(!Walking.isRunEnabled() && Walking.getEnergy() > 80) {
				Walking.setRun(true);
			}
		}
		
	}
	
	
	public static class Fighter extends Strategy implements Task{
		
		public final static Filter<NPC> corpseMageFilter = new Filter<NPC>() {
			public boolean accept(NPC mage) {
				return mage.getName().equals("Corpse mage") && mage.validate() && mage.getHpPercent() == 100 && mage.getAnimation() == -1 && mage.getInteracting() == null && mage.getModel() != null;
			}
		};
		
		public final static Filter<NPC> attackerFilter = new Filter<NPC>() {
			public boolean accept(NPC mage) {
				return mage.getName().equals("Corpse mage") && mage.getInteracting() != null && mage.getInteracting().equals(Players.getLocal()) && mage.getModel() != null && mage.getHpPercent() > 0;
			}
		};
		
		@Override
		public void run() {
			Paint.action = "fighting";
			final NPC curmage = NPCs.getNearest(corpseMageFilter);
			if(curmage != null && curmage.interact("Attack")) Time.sleep(1000);
		}
		
		@Override
		public boolean validate() {
			final Player myChar = Players.getLocal();
			return NPCs.getNearest(attackerFilter) == null && myChar.getInteracting() == null && !myChar.isMoving() && GroundItems.getNearest(Looter.lootFilter) == null;
		}
	}
	
	public static class Alcher extends Strategy implements Task{
		
		public static final void fireStaffCheck() {		
			final Item FireStaff = Inventory.getItem(VAR.FIRE_STAFF_ID);
			if(FireStaff != null ) {
				Tabs.EQUIPMENT.open();
				VAR.WEAPON_ID = Widgets.get(387, 15).getChildId();
				Tabs.INVENTORY.open();
			}
		}
		
		@Override
		public void run() {
			Paint.action = "Alching";
			final Item AlchItem = Inventory.getItem(AllLoot.HIGH_ALCH_ID_LIST);
			if(AlchItem == null || Inventory.getCount(VAR.NATURE_RUNE_ID) == 0 || Inventory.getCount(true, VAR.FIRE_RUNE_ID) < 5) return;
			
			final Item FireStaff = Inventory.getItem(VAR.FIRE_STAFF_ID);
			if(FireStaff != null ) {
				FireStaff.getWidgetChild().click(true);
			}

			
			Tabs.MAGIC.open();
			Widgets.get(192, 59).click(true);
			AlchItem.getWidgetChild().click(true);
			Time.sleep(1000);
			Tabs.INVENTORY.open();
			
			Looter.weaponCheck();
			
			for(int i = 0; i < AllLoot.HIGH_ALCH_LIST.length; i++) {
				if(AllLoot.HIGH_ALCH_LIST[i].id == AlchItem.getId()) {
					AllLoot.HIGH_ALCH_LIST[i].collected++;
					return;
				}
			}
		}
		
		@Override
		public boolean validate() {			
			return AllLoot.HIGH_ALCH && Inventory.getItem(AllLoot.HIGH_ALCH_ID_LIST) != null;
		}
	}
	
	
	@Override
	public void onRepaint(Graphics g) {
		try {
			Paint.paint(g);
		} catch (Exception Ignored) {}
	}
	
	@Override
	public void messageReceived(MessageEvent e) {
		if(e.getId() == 109 && e.getMessage().contains("coins")) AllLoot.LOOT_LIST[0].collected += Integer.parseInt(e.getMessage().substring(0, e.getMessage().indexOf(' ')));
	}
}
