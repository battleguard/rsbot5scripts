package SpiderSilk.node;

import java.awt.Color;
import java.awt.Graphics;

import org.powerbot.core.Bot;
import org.powerbot.core.script.job.Container;
import org.powerbot.core.script.job.LoopTask;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;

import SpiderSilk.util.Paint;
import SpiderSilk.util.net.GrandExchange;

public class ItemCounter extends LoopTask {

	private String name = "Loading";

	private final int id;
	private int price = 0;
	private int collected = 0;
	private int oldAmount = 0;

	private final Timer runTime = new Timer(0);

	public ItemCounter(final int id) {
		this.id = id;

		if (Game.isLoggedIn() && Tabs.INVENTORY.isOpen() && !Bank.isOpen()) {
			oldAmount = Inventory.getCount(true, id);
		}

		final Container container = Bot.instance().getScriptHandler().getScriptContainer();
		container.submit(this);
		container.submit(new Task() {

			@Override
			public void execute() {
				String[] data = GrandExchange.lookup(id);
				name = data[0];
				price = Integer.parseInt(data[1]);
			}
		});
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public int getPrice() {
		return price;
	}

	public int getCollected() {
		return collected;
	}

	public int getProfit() {
		return collected * price;
	}

	public int getProfitPerHour() {
		return (int) (getProfit() * (3600000d / runTime.getElapsed()));
	}

	public void draw(final Graphics g, final int x, final int y) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, 300, 20);
		g.setColor(Color.WHITE);
		Paint.shadowText(g, toString(), x + 5, y + 18);
	}

	@Override
	public int loop() {
		final int count = Inventory.getCount(true, id);
		if (count > oldAmount) {
			collected += count - oldAmount;
		}
		oldAmount = count;
		return 2500;
	}

	@Override
	public String toString() {
		return name + " - Collected " + Paint.numFormat.format(collected) + " - Worth : "
				+ Paint.formatter(getProfit()) + " - p/h : " + Paint.formatter(getProfitPerHour());
	}
}
