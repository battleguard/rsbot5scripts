package Miner;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.util.net.GeItem;

public class ProfitTracker extends Task {
	private static final int[] IDS = {453, 447, 442, 1623, 1621, 1619, 1617};
	private static final Item [] ITEMS = new Item[IDS.length];
	private static boolean draw = false;
	
	public static final boolean isValid() {
		return draw;
	}
	
	public class Item {
		private int price;
		private int amount = 0;
		private final int ID;
		private String Name;
		
		public Item(final int Id) {
			final GeItem info = GeItem.lookup(Id);
			this.ID = Id;
			try {
				Name = info.getName().replaceAll("Uncut ", "").replaceAll(" ore", "");				
				price = info.getPrice();
			} catch (Exception e) {
				System.out.println("Failed Getting Price on ID: " + ID + e.getMessage());
				price = 0;
				Name = "Error";
			}			
		}	
		
		public boolean isValid() {
			return amount != 0;
		}
		
		public boolean updateAmount(final String msg) {
			if(msg.equalsIgnoreCase(Name)) {
				amount++;
				return true;				
			}
			return false;
		}
		
		public int drawBox(Graphics g, final int idx) {
			final int gold = amount * price;
			final int goldPerHour = (int) (gold * (3600000d / SkillData.RUN_TIME.getElapsed()));			
			drawItemInfo(g, idx, Name, K.format(amount), formatter(gold), formatter(goldPerHour));
			return gold;
		}
		
		@Override
		public String toString() {
			return Name + " ID: " + ID + " Collected: " + amount + " Gained: " + price * amount + "gp" + "  GE: " + price; 
		}
	}
	
	private static final NumberFormat K = new DecimalFormat("###,###,###");
	
	public static final String formatter(final int num) {
		if(num < 1000) return "" + num;
		return num / 1000 + "." + (num % 1000) / 100 + "K"; 
	}
	
	public static void drawItemInfo(Graphics g, final int idx, final String... text) {
		drawBox(g, 0, 390 + idx * 20, 65, text[0]);
		drawBox(g, 65, 390 + idx * 20, 65, text[1]);
		drawBox(g, 130, 390 + idx * 20, 75, text[2] + " gp");
		drawBox(g, 195, 390 + idx * 20, 95, text[3] + " gp/h");
	}
	
	public static void drawBox(Graphics g, int x, int y, int width, final String text) {
		g.setColor(BLACK);
		g.fillRect(x, y, width, 20);
		g.setColor(Color.black);
		g.drawRect(x, y, width, 20);
		shadowText(g, text, x + 5, y + 18);
	}
	
	private static final Color BLACK = new Color(0, 0, 0, 160);
	public static final void drawProfitTracker(Graphics g) {
		if(!isValid()) return;		
			
		int totalGold = 0;
		int totalCollected = 0;
		int counter = 0;
		for(int i = 0; i < ITEMS.length; i++) {
			if(ITEMS[i].isValid()) {
				totalCollected += ITEMS[i].amount;
				totalGold += ITEMS[i].drawBox(g, counter);
				counter++;
			}					
		}
		final int goldPerHour = (int) (totalGold * (3600000d / SkillData.RUN_TIME.getElapsed()));
		drawItemInfo(g, counter, "Total", K.format(totalCollected), formatter(totalGold), formatter(goldPerHour));
	}
	
	
	
	
	/**
	 * Looks up grand exchange information and returns a string array with the following contents
	 * String[0] = item name
	 * String[1] = item price
	 * @param itemID for the item being looked up on the grand exchange
	 * @return : a string array of grand exchange information on the item id provided
	 */
	public static String[] lookup(final int itemID) {		
		try {
			String[] info = {"0", "0"};
			final URL url = new URL("http://www.tip.it/runescape/index.php?gec&itemid=" + itemID);
			final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String input;
			while ((input = br.readLine()) != null) {
				if(input.startsWith("<h2>")) {
					info[0] = input.substring(4, input.length() - 5);
				}
				if(input.startsWith("<tr><td colspan=\"4\"><b>Current Market Price: </b>")) {
					info[1] = input.substring(49, input.lastIndexOf("gp")).replaceAll(",", "");
					return info;
				}
			}
		} catch (final Exception ignored) {}
	return null;
	}


	public static final void updateAmounts(String msg) {	
		msg = msg.substring(msg.lastIndexOf(" ")+1, msg.length()-1);		
		for (Item curItem : ITEMS) {
			if(curItem.updateAmount(msg)) {				
				return;
			}
		}
	}
	
	public static final void shadowText(Graphics g, final String line, final int x, final int y) {
		g.setColor(Color.BLACK);
		g.drawString(line, x+1, y+1);
		g.setColor(Color.WHITE);
		g.drawString(line, x, y);		
	}
	
	@Override
	public void execute() {
		for(int i = 0; i < ITEMS.length; i++) {
			ITEMS[i] = new Item(IDS[i]);
			System.out.println(ITEMS[i].toString());
		}
		draw = true;
	}
	
}
