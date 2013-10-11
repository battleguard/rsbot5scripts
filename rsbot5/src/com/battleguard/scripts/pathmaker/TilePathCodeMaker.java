package com.battleguard.scripts.pathmaker;

import java.util.Enumeration;

import org.powerbot.script.wrappers.Tile;

public class TilePathCodeMaker {
	
	public static String create(Enumeration<Tile> tile) {
		TilePathCode tilePathcode = new TilePathCode(tile);
		return tilePathcode.generateCode();
	}
	
	private static class TilePathCode {
		private static final int MAX_TILES_PER_LINE = 3;
		private int tilesOnCurrentLine = 0;
		private StringBuilder tilePathText = new StringBuilder("Tile[] path = {");
		private Enumeration<Tile> tiles;
		
		public TilePathCode(Enumeration<Tile> tiles) {			
			this.tiles = tiles;
		}
		
		public String generateCode() {			
			while(tiles.hasMoreElements()) {
				addTileToTilePath();
			}
			tilePathText.append("};");
			return tilePathText.toString();
		}
		
		private void addTileToTilePath() {
			if(isCodeLineTooLong()) {
				addNewLine();
			}					
			tilePathText.append(" new Tile" + tiles.nextElement());
			if(tiles.hasMoreElements()) {
				tilePathText.append(",");
			}
			tilesOnCurrentLine++;

		}
		
		private boolean isCodeLineTooLong() {
			return tilesOnCurrentLine == MAX_TILES_PER_LINE;
		}
		
		private void addNewLine() {
			tilePathText.append("\n\t\t");
			tilesOnCurrentLine = 0;
		}		
	}
}
