package com.battleguard.scripts.f2prunecrafter;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;

import com.battleguard.scripts.f2prunecrafter.data.Master;
import com.battleguard.scripts.f2prunecrafter.graphics.Gui;
import com.battleguard.scripts.f2prunecrafter.nodes.Node;
import com.battleguard.scripts.f2prunecrafter.nodes.impl.Banking;
import com.battleguard.scripts.f2prunecrafter.nodes.impl.Craft;
import com.battleguard.scripts.f2prunecrafter.nodes.impl.Doors;
import com.battleguard.scripts.f2prunecrafter.nodes.impl.Walk;

@Manifest(authors = { "Battleguard" }, description = "AIO free to play runecrafter", name = "AIO F2P Runecrafter")
public class Runecrafter extends PollingScript {	
	
	private final Queue<Node> bankNodes = new ConcurrentLinkedQueue<>();
    private final Queue<Node> craftNodes = new ConcurrentLinkedQueue<>();
	private static final int RUNE_ESSENCE_ID = 1436;		
	
	public Runecrafter() {		
		getExecQueue(State.START).offer(new Runnable() {
			 
            @Override
            public void run() {
            	final Gui gui = new Gui();
            	while(gui.isDone()) {
            		sleep(50);
            	}
            	final Master master = gui.getSelectedEnum();
            	            	
				final Node crafting = new Craft(ctx, master.alter());
				final Node enterAlter = Doors.createEnterAlterInstance(ctx, master.alter());
				final Node toAlter = Walk.createAlterPathInstance(ctx, master.path());             		        		        		        		        		        	
				
				final Node bank = new Banking(ctx);
				final Node exitAlter = Doors.createExitAlterInstance(ctx, master.alter());
				final Node toBank = Walk.createBankPathInstance(ctx, master.path());         	        		
        		
        		craftNodes.addAll(Arrays.asList(crafting, enterAlter, toAlter));
        		bankNodes.addAll(Arrays.asList(bank, exitAlter, toBank)); 		
            }
        });		
	}	
		
	@Override
	public int poll() {		
		for (Node node : outOfEssence() ? bankNodes : craftNodes) {
			if(node.activate()) {
				node.execute();
				break;
			}
		}
		return 50;
	}
	
	private boolean outOfEssence() {
		return ctx.backpack.select().id(RUNE_ESSENCE_ID).isEmpty();
	}	
}
