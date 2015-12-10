package PotatoPicker.Nodes;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.powerbot.core.script.job.Container;
import org.powerbot.core.script.job.LoopTask;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.util.net.GeItem;

public class InventoryCounter extends LoopTask {
	
	public String itemName;
	public final int itemID;	
	public int itemPrice;
	
	public int Collected = 0;
	private int inventoryCache = 0;
	
	private final Timer runTime = new Timer(0);
	
	public InventoryCounter(final int itemID, Container container) {	
		this.itemID = itemID;
		container.submit(this);
		container.submit(new Task() {
			
			@Override
			public void execute() {
				final GeItem geInfo = GeItem.lookup(itemID);		
				if(geInfo != null) {
					itemName = geInfo.getName();
					itemPrice = geInfo.getPrice();
				} else {
					itemName = "Error";
					itemPrice = 0;
				}	
			}
		});
	}

	@Override
	public int loop() {		
		final int count = Inventory.getCount(true, itemID);
		if(count > inventoryCache) {
			Collected += count - inventoryCache;
		}
		inventoryCache = count;
		return 2500;
	}	
	
	public final void draw(Graphics g, int x, int y) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, 300, 20);
		g.setColor(Color.WHITE);
		shadowText(g, toString(), x + 5, y + 18);		
	}
	
	public final int getProfit() {
		return Collected * itemPrice;
	}
	
	public final int getProfitPerHour() {
		return (int) (getProfit() * (3600000d / runTime.getElapsed()));
	}
	
	@Override
	public String toString() {
		return itemName + " - Collected " + K.format(Collected) + " - Worth : " + formatter(getProfit()) + " - p/h : " + formatter(getProfitPerHour());
	}
	
	public static final NumberFormat K = new DecimalFormat("###,###,###");
	
	
	public static final String formatter(final int num) {
		if(num < 1000) return "" + num;
		return num / 1000 + "." + (num % 1000) / 100 + "K"; 
	}
	
	
	public static final void shadowText(Graphics g, final String line, final int x, final int y) {
		g.setColor(Color.BLACK);
		g.drawString(line, x+1, y+1);
		g.setColor(Color.WHITE);
		g.drawString(line, x, y);		
	}
}
