package Miner;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.powerbot.core.script.job.LoopTask;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.node.SceneObjectDefinition;
import org.powerbot.game.api.wrappers.widget.WidgetChild;


public class RockTimer {
	
	public static Area miningArea;
	private static Tile[] tilesArray;
	public static String [] rockNames;
	public static String state = "";
	
	private static final Filter<SceneObject> findRocksFilter = new Filter<SceneObject>() {
		public boolean accept(SceneObject rock) {
			if(miningArea.contains(rock)) {
				for (Rock curRock : Rocks) {
					if(curRock.RockTile.equals(rock.getLocation())) return false;
				}
				
				final SceneObjectDefinition def = rock.getDefinition();
				if(def != null && def.getName() != null) {
					final String curRockName = def.getName();
					for (String name : rockNames) {
						if(curRockName.startsWith(name)) return true; 
					}
				}			
			}
			return false;
		}
	};
	
	
	

	

			
	private static final ArrayList<Rock> Rocks = new ArrayList<Rock>();
	private static final Timer FindRocksTimer = new Timer(0);
	
	public RockTimer() {
		for (String name : rockNames) {
			if(name.equals("Runite")) {
				FindRocksTimer.setEndIn(30 * 60 * 1000);
				return;
			}
		}
		FindRocksTimer.setEndIn(5 * 60 * 1000);
	}
	
	public final void updatetimers() {
		if(FindRocksTimer.isRunning()) {
			final SceneObject [] allRocks = SceneEntities.getLoaded(findRocksFilter);
			for (SceneObject sceneObject : allRocks) {		
				String name = sceneObject.getDefinition().getName();
				name = name.substring(0, name.indexOf(' '));			
				Rocks.add(new Rock(sceneObject.getLocation(), sceneObject.getId(), name));
			}
		}
		
		for (Rock curRock : Rocks) {
			curRock.update();
		}
	}
	
	public static final void makeMiningArea(final int radius, final Tile loc) {			
		final Tile SW = new Tile(loc.getX() - radius, loc.getY() - radius, loc.getPlane());
		final Tile NE = new Tile(loc.getX() + radius + 1, loc.getY() + radius + 1, loc.getPlane());
		miningArea = new Area(SW, NE);
		tilesArray = miningArea.getTileArray();
	}		
	
	public static Rock currentRock;
	public static Rock nextRock;
	public static final Timer ReClickTimer = new Timer(1500);
	
			
	public final class FindRock extends Node {
		
		public boolean prevState = false;		
		
		@Override
		public void execute() {
			currentRock = getBestRock(null);
			
			if(currentRock != null) {
				prevState = currentRock.rockUp;
			}
			
			nextRock = getBestRock(currentRock);
			if(currentRock != null) {
				SceneObject rock = SceneEntities.getAt(currentRock.RockTile);
				if(!rock.isOnScreen() && miningArea.contains(rock.getLocation())) {
					state = "Walking to Rock";
					Walking.walk(rock);
				} else {
					ReClickTimer.reset();
					state = "Waiting on Rock";
					if(!currentRock.rockUp && Calculations.distanceTo(currentRock.RockTile) == 1.0) {
						if(checkOrientation(currentRock.RockTile)) return;
					}				
					state = "Clicking Rock";
					rock.interact("Mine");
					//final Point rockP = rock.getCentralPoint();
					//Mouse.hop(rockP.x, rockP.y);
					//Mouse.click(true);									
					state = "Mining";
					ReClickTimer.reset();
				}				
			}			
		}		
		
		@Override
		public boolean activate() {
			if(Banker.isDepoOpen() ||Inventory.isFull() || Players.getLocal().isInCombat()) return false;					
			updatetimers();			
			if(currentRock == null || !ReClickTimer.isRunning() || prevState != currentRock.rockUp) {				
				return true;
			}
			return false;
		}		
	}
	
	public final class HoverOverNextRock extends Node {
		
		@Override
		public void execute() {
			Mouse.move(currentRock.rockUp ? nextRock.RockTile.getCentralPoint() : currentRock.RockTile.getCentralPoint());			
		}
		@Override
		public boolean activate() {
			if(Banker.isDepoOpen() ||Inventory.isFull()) return false;
			if(currentRock != null && currentRock.rockUp) {
				return nextRock != null && nextRock.RockTile.isOnScreen() && !nextRock.RockTile.getBounds()[0].contains(Mouse.getLocation());
			} else {
				return currentRock != null && currentRock.RockTile.isOnScreen() && !currentRock.RockTile.getBounds()[0].contains(Mouse.getLocation());
			}
		}		
	}
	
	public final class MiningAreaCheck extends Node {
		
		@Override
		public void execute() {
			Walking.walk(miningArea.getCentralTile());		
		}
		@Override
		public boolean activate() {
			return !miningArea.contains(Players.getLocal());
		}		
	}
	
	public final class RunCheck extends Node {
		
		public boolean isRunEnable() {
			final WidgetChild runWidget = Widgets.get(750, 0);
			return runWidget != null && runWidget.getTextureId() == 783;	
		}
		
		@Override
		public void execute() {
			Walking.setRun(true);	
		}
		@Override
		public boolean activate() {
			return (!isRunEnable() && Walking.getEnergy() > 50);
		}		
	}
	
	
	
	public final class RockUpdater extends LoopTask {

		@Override
		public int loop() {
			for (Rock rock : Rocks) {
				rock.update();
			}
			nextRock = getBestRock(currentRock);			
			return 1000;
		}
	}
	
	public final class MiningTimer extends LoopTask {
		private Tile prevTile = new Tile(0, 0, 0);
		private int prevO = 0;		

		@Override
		public int loop() {
			final Player Char = Players.getLocal();
			if(prevO != Char.getOrientation() || Char.getAnimation() != -1 || Char.isMoving() || !Char.getLocation().equals(prevTile)) {
				prevTile = Players.getLocal().getLocation();
				prevO = Players.getLocal().getOrientation();
				ReClickTimer.reset();	
			}
			return 50;
		}			
	}
	
	/** Checks to see if we are facing the tile
	 * 0 = E 90 = N 180 = W 270 = S
	 * @return true if we are facing the tile
	 */
	public static final boolean checkOrientation(final Tile rockTile) {
		final int Orientation = Players.getLocal().getOrientation(); 
		final Tile CurLoc = Players.getLocal().getLocation(); 
		if(rockTile.getX() != CurLoc.getX()) {
			return (Orientation == ((rockTile.getX() > CurLoc.getX()) ?  0 : 180)); 
		} else {
			return (Orientation == ((rockTile.getY() > CurLoc.getY()) ?  90 : 270));
		}
	}
	
	public static final Rock getBestRock(final Rock curRock) {		
		Rock bestRock = null;
		double minT = Double.MAX_VALUE;
		for (Rock rock : Rocks) {
			if(curRock != null && rock.equals(curRock)) continue;
			final double timeTillSpawn = rock.getPriorityTimer();
			if(timeTillSpawn < minT) {
				minT = timeTillSpawn;
				bestRock = rock;
			}
		}
		return bestRock;
	}		
	
	public static final Color BLUE = new Color(0, 0, 255, 50);
	public static final Color GREEN = new Color(0, 255, 0, 60);
	public static final Color RED = new Color(255, 0, 0, 60);
	public static final Color YELLOW = new Color(255, 255, 0, 60);
	
	public static final void drawRocks(Graphics g) {
		final Font font = new Font(g.getFont().getName(), Font.PLAIN, 10);
		g.setFont(font);
		drawArea(g);

		for(int i = 0; i < Rocks.size(); i++) {
			if(currentRock != null && Rocks.get(i).equals(currentRock)) {
				drawTile(g, Rocks.get(i), GREEN);
			} else if(nextRock != null && Rocks.get(i).equals(nextRock)){
				drawTile(g, Rocks.get(i), YELLOW);
			} else {
				drawTile(g, Rocks.get(i), Rocks.get(i).rockUp ? BLUE : RED);
			}
		}
	}
	
	private static final void drawTile(Graphics g, Rock rock, Color color) {
		try {
			if(rock.RockTile != null && rock.RockTile.isOnScreen()) {
				g.setColor(color);
				g.drawPolygon(rock.RockTile.getBounds()[0]);
				g.fillPolygon(rock.RockTile.getBounds()[0]);				
				final Point p = rock.RockTile.getCentralPoint();
				shadowText(g, formatter(rock.getRemaining()), p);
				//shadowText(g, formatter(rock.getPriorityTimer()), new Point(p.x, p.y - 10));				
			}
		} catch (Exception ignored) {}
  	}
	
	private static final void drawArea(Graphics g) {
		g.setColor(new Color(0, 0, 0, 50));
		for(int i = 0; tilesArray != null && i < tilesArray.length; i++) {
			try {
				g.drawPolygon(tilesArray[i].getBounds()[0]);
				g.fillPolygon(tilesArray[i].getBounds()[0]);
			} catch (Exception ignored) {}
		}
  	}
	
	
	
	private static final String formatter(long time) {
		return time / 1000 + ":" + ((time % 1000) / 100);
	}
	
	
	private static final void shadowText(Graphics g, final String line, final Point p) throws Exception {
		g.setColor(Color.BLACK);
		g.drawString(line, p.x+1, p.y+1);
		g.setColor(Color.WHITE);
		g.drawString(line, p.x, p.y);		
	}

}
