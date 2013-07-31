package com.battleguard.scripts.f2prunecrafter;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;

import com.battleguard.scripts.f2prunecrafter.data.Master;
import com.battleguard.scripts.f2prunecrafter.nodes.Node;
import com.battleguard.scripts.f2prunecrafter.nodes.impl.Banking;
import com.battleguard.scripts.f2prunecrafter.nodes.impl.Craft;
import com.battleguard.scripts.f2prunecrafter.nodes.impl.Doors;
import com.battleguard.scripts.f2prunecrafter.nodes.impl.Walk;

@Manifest(authors = { "Battleguard" }, description = "AIO free to play runecrafter", name = "AIO F2P Runecrafter")
public class Runecrafter extends PollingScript {	
	
	private static final int RUNE_ESSENCE_ID = 1436;
	
	private final Queue<Node> bankNodes = new ConcurrentLinkedQueue<>();
    	private final Queue<Node> craftNodes = new ConcurrentLinkedQueue<>();
	
	public Runecrafter() {
		getExecQueue(State.START).offer(new Runnable() {
			 
	            @Override
	            public void run() {
	            	// this will be changed later to be selected by a gui
	            	final Master master = Master.FIRE;	
	        		
	            	final Node crafting = new Craft(master, ctx);
	            	final Node enterAlter = Doors.createEnterAlterInstance(master, ctx);
	            	final Node toAlter = Walk.createAlterPathInstance(master, ctx);             		        		        		        		        		        	
	        		
	        	final Node bank = new Banking(master, ctx);
	        	final Node exitAlter = Doors.createExitAlterInstance(master, ctx);
	        	final Node toBank = Walk.createBankPathInstance(master, ctx);         		
	        	
	        	craftNodes.addAll(Arrays.asList(crafting, enterAlter, toAlter));
	        	bankNodes.addAll(Arrays.asList(bank, exitAlter, toBank));
	            }
	        });		
	}
	
	
	@Override
	public int poll() {		
		final Queue<Node> tree = ctx.backpack.select().id(RUNE_ESSENCE_ID).isEmpty() ? bankNodes : craftNodes;
		for (Node node : tree) {
			if(node.activate()) {
				node.execute();
				break;
			}
		}
		return 50;
	}
}
