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
		private StringBuilder tilePathText = new StringBuilder("TilePath path = new TilePath(");
		private Enumeration<Tile> tiles;
		
		public TilePathCode(Enumeration<Tile> tiles) {			
			this.tiles = tiles;
		}
		
		public String generateCode() {
			while(tiles.hasMoreElements()) {
				addTileToTilePath();
			}
			return tilePathText.toString();
		}
		
		private void addTileToTilePath() {
			tilePathText.append("new Tile" + tiles.nextElement());
			tilesOnCurrentLine++;
			if(isCodeLineTooLong()) {
				addNewLine();
			}
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
