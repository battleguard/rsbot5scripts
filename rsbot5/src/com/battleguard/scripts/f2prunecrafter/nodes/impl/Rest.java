package com.battleguard.scripts.f2prunecrafter.nodes.impl;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Npc;

import com.battleguard.scripts.f2prunecrafter.data.Master;
import com.battleguard.scripts.f2prunecrafter.data.Musicians;
import com.battleguard.scripts.f2prunecrafter.nodes.Node;

public class Rest extends MethodProvider implements Node {

	private final Musicians musician;
	
	public Rest(Master master, MethodContext ctx) {
		super(ctx); 
		musician = master.musician();
	} 

	
	@Override
	public boolean activate() {
		return musician.area().contains(ctx.players.local()) && ctx.movement.getEnergyLevel() == 0;
	}

	@Override
	public void execute() {		
		if(!ctx.npcs.select().id(musician.id()).first().isEmpty()) {
			Npc singer = ctx.npcs.iterator().next();
			if(singer.interact("Listen-To", singer.getName())) {
				final Timer t = new Timer(2000);
				while(t.isRunning() && ctx.movement.getEnergyLevel() < 100) {
					if(ctx.players.local().getAnimation() == Musicians.Animation) {
						t.reset();
					}
					sleep(50);
				}				
			}
		}
	}
}
	
