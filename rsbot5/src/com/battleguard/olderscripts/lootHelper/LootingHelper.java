package LootHelper;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.Vector;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.GroundItem;

@Manifest(authors = {"Battleguard"}, name = "Diablo Style Loot Displayer Mod", description = "Helps show loot to player in a Diablo loot showing style", version = 0.6)
public class LootingHelper extends ActiveScript implements PaintListener, MouseListener, KeyListener {
	
	/** update logs
	 * 0.2 made the windows look very similar to runescape menus. This was done using multiple rounded rectangles
	 * 0.3 added Quality enum class that shows rarity of loot. Also formatted price values to be easier to read showed items is noted with printname
	 * 0.4 added in gui
	 * 0.5 added edit settings button
	 * 0.6 added to sdn and now downloads image from my dropbox
	 */
		
	
	
	public Font RUNESCAPE_MENU_FONT;
		
	
	@Override
	public int loop() {						
		return 50;
	}		
	
	
	@Override
	public void onRepaint(Graphics g) {				
		g.setFont(RUNESCAPE_MENU_FONT);
		if(LootHelper.GUI.ToggleLoot.displayLoot()) {
			drawLoot(g);
		}			
		drawButton(g);
	}
	
		
	private static final void drawLoot(Graphics g) {		
		GroundItem[] onScreenLoot = GroundItems.getLoaded(LOOT_FILTER);
		Hashtable<Tile, Vector<Loot>> tilesToDraw = new Hashtable<Tile, Vector<Loot>>();

		for (GroundItem curLoot : onScreenLoot) {
			Loot loot = new Loot(curLoot);					
			if(loot.getPrice() == -1) new Thread(loot).start();	
			
			if(loot.getLootPrice() < Gui.MIN_PRICE) {
				boolean found = false;
				for(int i = 0; i < Gui.ALWAYS_SHOW_MODEL.size(); i++) {
					if(Gui.ALWAYS_SHOW_MODEL.get(i).equalsIgnoreCase(loot.getName()) || Gui.ALWAYS_SHOW_MODEL.get(i).equals("" + loot.getId())) {
						found = true;
					}
				}
				if(!found) continue;
			}
			
			Vector<Loot> tileLoot = tilesToDraw.get(loot.getLocation());
			if(tileLoot == null) {
				tileLoot = new Vector<Loot>();
				tileLoot.add(loot);
				tilesToDraw.put(loot.getLocation(), tileLoot);
			} else {
				tileLoot.add(loot);
			}			
		}		
		
		for (Vector<Loot> loot : tilesToDraw.values()) {
			drawLootMenu(loot, g);
		}		
	}
		
	private static final Filter<GroundItem> LOOT_FILTER = new Filter<GroundItem>() {
				
		public boolean accept(GroundItem loot) {
			final String name = loot.getGroundItem().getName();
			if(loot.isOnScreen() && name != null) {
				for(int i = 0; i < Gui.NEVER_SHOW_MODEL.getSize(); i++) {
					final String idx = Gui.NEVER_SHOW_MODEL.get(i);
					if(idx.equalsIgnoreCase(name) || idx.equals("" + loot.getId())) {
						return false;
					}
				}
				return true;
			}
			return false;
		}		
	};	
	
	
	private static final void drawLootMenu(Vector<Loot> tileLoot, Graphics g) {
		int width = 0;
		for (Loot loot : tileLoot) {
			final int curWidth = g.getFontMetrics().stringWidth(loot.toString());
			if(curWidth > width) width = curWidth;
		}
		final int height = tileLoot.size() * 20;
		
		final Point p = tileLoot.firstElement().getCentralPoint();
		final Rectangle box = new Rectangle(p.x - width / 2, p.y - height - 15, width, height);
		g.setColor(Color.BLACK);		
		g.drawLine(p.x, p.y, p.x, p.y - 15);
		
		drawMenuBoxRunescape(g, box);		
		drawMenuText(g, box.getLocation(), tileLoot);		
	}

	
	private static final Color YELLOW = new Color(202, 203, 1);	
	private static final Color BLUE = new Color(10, 28, 36, 220);
	private static final Color BLACK_180 = new Color(0, 0, 0, 180);
	private static final Color BLACK_240 = new Color(0, 0, 0, 240);
	private static final Color BLACK_220 = new Color(0, 0, 0, 220);
	
	private static final void drawMenuBoxRunescape(Graphics g, Rectangle box) {
		g.setColor(BLUE);
		g.fillRect(box.x, box.y, box.width, box.height);		
		g.setColor(Color.BLACK);
		g.drawRect(box.x - 1, box.y - 1, box.width + 1, box.height + 1);
		g.drawRoundRect(box.x - 3, box.y - 3, box.width + 5, box.height + 5, 5, 5);
		g.setColor(Color.WHITE);
		g.drawRoundRect(box.x - 2, box.y - 2, box.width + 3, box.height + 3, 4, 4);
	}
	
	private static final void drawMenuBoxTorchLight(Graphics g, Rectangle box) {
		g.setColor(BLACK_180);
		g.fillRoundRect(box.x - 5, box.y - 5, box.width + 5, box.height + 5, 10, 10);
		g.setColor(BLACK_240);
		g.drawRoundRect(box.x - 5, box.y - 5, box.width + 5, box.height + 5, 10, 10);
		g.setColor(BLACK_220);
		g.drawRoundRect(box.x - 6, box.y - 6, box.width + 7, box.height + 7, 12, 12);
	}
	
	
	private static final void drawMenuText(Graphics g, Point box, Vector<Loot> tileLoot) {
		for (Loot loot : tileLoot) {
			final int lineWidth = g.getFontMetrics().stringWidth(loot.printName());
			shadowText(g, loot.printName(), box.x, box.y + 15, Quality.getRarity(loot.getLootPrice()));
			shadowText(g, "" + loot.printPrice(), box.x + 5 + lineWidth, box.y + 15, YELLOW);
			box.y += 20;
		}
	}
	
    private static final Rectangle REC = new Rectangle(372, 395, 125, 15);
    public static Gui gui;

    private static final void drawButton(Graphics g) {
    	REC.y = Game.getDimensions().height - 160;
        g.setColor(Color.BLACK);
        g.drawRect(REC.x - 1, REC.y - 1, REC.width + 1, REC.height + 1);
        g.setColor(gui != null && gui.isVisible() ? new Color(85, 110, 36, 180) : new Color(153, 0, 36, 180));
        g.fillRect(REC.x, REC.y, REC.width, REC.height);        
        shadowText(g, "Edit Settings ", REC.x + 3, REC.y + 13, Color.WHITE);
    }
	
    private static final void shadowText(Graphics g, String line, int x, int y, Color color) {
        g.setColor(Color.BLACK);
        g.drawString(line, x + 1, y + 1);
        g.setColor(color);
        g.drawString(line, x, y);
    }

	@Override
	public void mouseClicked(MouseEvent e) {	
	        if (REC.contains(e.getPoint()) && (gui == null || !gui.isVisible())) {
	        	gui = new Gui();
	        }	        
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		LootHelper.GUI.ToggleLoot.toggle(e);
	}
	
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void keyReleased(KeyEvent e) {}
	@Override public void keyTyped(KeyEvent e) {}   
}
