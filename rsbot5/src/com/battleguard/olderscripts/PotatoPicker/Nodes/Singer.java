package PotatoPicker.Nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.interactive.NPC;

import SpiderSilk.Util.Methods;

public class Singer extends Node {
		
	public static final int SINGER_ID = 30;
	
	@Override
	public boolean activate() {
		if(Walking.getEnergy() < 50) {
			final NPC singer = NPCs.getNearest(SINGER_ID);
			return singer != null && Calculations.distanceTo(singer) < 10;	
		}
		return false;
	}

	@Override
	public void execute() {	
		final NPC singer = NPCs.getNearest(SINGER_ID);
		if(singer != null) {
			if(Methods.isOnScreen(singer)) {
				singer.interact("Listen");
				sleep(3000);
				while(Players.getLocal().getAnimation() != -1 && Walking.getEnergy() != 100) {
					sleep(1000);
				}
			} else {
				Walking.walk(singer);
			}
		}		
	}
	
	
}
