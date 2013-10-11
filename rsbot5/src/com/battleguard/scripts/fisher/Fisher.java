package com.battleguard.scripts.fisher;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.util.Timer;
import com.battleguard.scripts.fisher.debug.DebugMethodProvider;
import com.battleguard.scripts.fisher.nodes.impl.Fish;
import com.battleguard.scripts.fisher.nodes.impl.OpenTrader;
import com.battleguard.scripts.fisher.nodes.impl.SellCrayFish;

@Manifest(authors = { "Battleguard" }, description = "Crayfish Fisher", name = "Crayfish Fisher", hidden=true)
public class Fisher extends PollingScript implements PaintListener {

	private final Queue<DebugMethodProvider> fishingNodes = new ConcurrentLinkedQueue<>();
	private final Timer runTime = new Timer(1 * 60 * 1000);

	public Fisher() {
		getExecQueue(State.START).offer(new Runnable() {

			@Override
			public void run() {
				fishingNodes.add(new SellCrayFish(ctx));
				fishingNodes.add(new OpenTrader(ctx));
				fishingNodes.add(new Fish(ctx));
			}
		});
	}

	@Override
	public int poll() {		
		for (DebugMethodProvider node : fishingNodes) {
			if (node.activate()) {
				node.execute();
				return 50;
			}
		}
		return 50;
	}

	@Override
	public void repaint(Graphics g) {
		DebugMethodProvider.drawStack(g, new Point(0, 300));		
		g.drawString(Timer.format(getRuntime()), 10, 280);
		if (!runTime.isRunning()) {
			System.out.println(ctx.players.local().getName() + " " + Timer.format(getTotalRuntime())
					+ "  CrayFish caught: " + ctx.backpack.getMoneyPouch());
			runTime.reset();
		}

	}
}
