package com.battleguard.scripts.fisher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.Npc;
import org.powerbot.script.wrappers.Widget;

import com.battleguard.scripts.fisher.debug.DebugMethodProvider;
import com.battleguard.scripts.fisher.nodes.Node;
import com.battleguard.scripts.fisher.nodes.impl.Fish;
import com.battleguard.scripts.fisher.nodes.impl.OpenTrader;
import com.battleguard.scripts.fisher.nodes.impl.SellCrayFish;
import com.battleguard.scripts.fisher.util.TradeWindow;
import com.battleguard.scripts.fisher.util.TradeWindow.SellAmount;

@Manifest(authors = { "Battleguard" }, description = "Crayfish Fisher", name = "Crayfish Fisher")
public class Fisher extends PollingScript implements PaintListener {

	private final Queue<DebugMethodProvider> fishingNodes = new ConcurrentLinkedQueue<>();	
	private DebugMethodProvider currentNode;
	
	
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
			if(node.activate()) {				
				node.execute();				
				currentNode = node;
				return 50;
			}
		}
		return 50;
	}
	

	@Override
	public void repaint(Graphics g) {		
		Point p = new Point(10, 10);
		for (DebugMethodProvider node : fishingNodes) {
			if(currentNode != null && currentNode.equals(node)) {
				g.setColor(Color.GREEN);
			}
			node.dragDebugger(g, p);
			g.setColor(Color.WHITE);			
		}
		DebugMethodProvider.drawStack(g, new Point(0,300));
	}
}
