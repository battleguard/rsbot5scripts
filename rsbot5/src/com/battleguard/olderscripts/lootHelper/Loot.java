package LootHelper;

import java.util.Hashtable;

import org.powerbot.game.api.util.net.GeItem;
import org.powerbot.game.api.wrappers.node.GroundItem;

public class Loot extends GroundItem implements Runnable {
	
	private final static Hashtable<String, Integer> PriceTable = new Hashtable<String, Integer>();
	private int price;
	
	public Loot(final GroundItem loot) {
		super(loot.getLocation(), loot.getGroundItem());
		Integer price = PriceTable.get(loot.getGroundItem().getName());
		if(price == null) {
			PriceTable.put(loot.getGroundItem().getName(), 0);
			this.price = -1;
		}  else {
			this.price = price;
		}
	}
	
	@Override
	public void run() {			
		final GeItem geInfo = GeItem.lookup(getName());
		PriceTable.put(getName(), geInfo != null ? geInfo.getPrice() : 1);
		price = PriceTable.get(getName());
		System.out.println("Grabbed Price for " + getName() + " : " + price);
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getLootPrice() {
		return price * getGroundItem().getStackSize();
	}
	
	public String printPrice() {
		return formatter(getLootPrice());
	}
	
	
	public String printName() {
		return getName() + ((getGroundItem().getStackSize() > 1) ? "*" : "");
	}
	
	public String getName() {
		return getGroundItem().getName();
	}
	
	@Override
	public String toString() {
		return printName() + "  " + printPrice();
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
				
    private static final String formatter(final int num) {
		if(num < 1000) return "" + num;
		return num / 1000 + "." + (num % 1000) / 100 + "K"; 
	}
}
