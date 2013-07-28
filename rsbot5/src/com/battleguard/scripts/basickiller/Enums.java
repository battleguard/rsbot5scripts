package com.battleguard.scripts.basickiller;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.wrappers.Tile;
import org.powerbot.script.wrappers.TilePath;

public class Enums extends MethodProvider {
	
	public static MethodContext ctx;
	
	public Enums(MethodContext ctx) {
		super(ctx);		
		Enums.ctx = ctx;		
	}
	
	public enum Path {
		OUTSIDE_DUNGEON(new Tile(3233, 3221, 0), new Tile(3236, 3212, 0), 
				new Tile(3236, 3208, 0), new Tile(3240, 3291, 0), new Tile(3246, 3198, 0)),
		INSIDE_DUNGEON(new Tile(3972,5564, 0), new Tile(3972, 5553, 0), 
				new Tile(3984, 5553, 0)),
		EDGVILLE_BANK(new Tile(3067, 3505, 0), new Tile(3073, 3503, 0), 
				new Tile(3081, 3501, 0), new Tile(3089, 3499, 0), new Tile(3094, 3496, 0));	
		
		private final TilePath path;
		
		public boolean traverse(MethodContext ctx) {
			return path.traverse();
		}
		
		Path(Tile... tiles) {			
			path = new TilePath(ctx, tiles);
		}		
	}

}
