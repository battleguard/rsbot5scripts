package com.battleguard.scripts.fisher.debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;

import com.battleguard.scripts.fisher.nodes.Node;

public abstract class DebugMethodProvider extends MethodProvider implements Node {
	
	private Point p;	
	private Graphics g;
	private static final Queue<String> logQueue = new ConcurrentLinkedQueue<>();
	private static final int MAX_QUEUE_SIZE = 10;
	
	public DebugMethodProvider(MethodContext ctx) {
		super(ctx);
	}	
		
	public final Point dragDebugger(Graphics g, Point p) {
		final Point initial = new Point(p);
		SetupDebugger(g, p);
		writeDebugLine(getClass().getSimpleName());
		writeDebugLines();
		draw(g);
		g.setColor(Color.RED);
		g.drawRect(initial.x, initial.y - 15, 300, p.y - initial.y);
		return p;
	}
	
	private void SetupDebugger(Graphics g, Point p) {
		this.p = p;
		this.g = g;
	}
	
	public final void writeDebugLine(String text) {
		g.drawString(text, p.x + 5, p.y);
		p.y += 20;
	}
	
	public synchronized void logMessage(String message) {
		if(logQueue.size() == MAX_QUEUE_SIZE) {
			logQueue.remove();
		}
		logQueue.add(message);
	}
	
	public static void drawStack(Graphics g, Point loc) {
		g.setColor(Color.BLACK);
		g.fillRect(loc.x, loc.y, 300, 20 * MAX_QUEUE_SIZE);
		g.setColor(Color.WHITE);
		for (String stack : logQueue) {
			g.drawString(stack, 5, loc.y+=20);
		}		
	}
	
	protected void writeDebugLines() {}	
	protected void draw(Graphics g) {}	
}
