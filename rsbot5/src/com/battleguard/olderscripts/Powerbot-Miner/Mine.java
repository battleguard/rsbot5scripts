package Miner;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Hashtable;


import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.LoopTask;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.util.net.GeItem;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.Item;

@Manifest(name = "Dwarven Resource & Power Miner", authors = { "Battleguard" }, description= "Can either power mine or use deposit box if inside Dwarven Resource Mine. Feautures advanced next rock chooser, easy to use, nice paint", version=1.95, website="http://www.powerbot.org/community/topic/740761-sdn-advanced-powerminer-all-locations-all-rocks-no-ids/")
public class Mine extends ActiveScript implements PaintListener, MessageListener  {
	
	
	public SkillData skilldata;
	public RockTimer rocktimer;
	
	public static boolean guiDone = false;
	public static boolean killScript = false;
	public static boolean useResource = false;
	public static boolean depositAll = true;
	public static int updateArea = 3;		
	private ArrayList<Node> jobList = new ArrayList<Node>();
	
	public void startScript() {
		rocktimer = new RockTimer();
		
		if(useResource) {
			if(CoalBag.setup()) {
				System.out.println("Using Coal Bag");
				jobList.add(new CoalBag());
			}
			jobList.add(new Banker());
			this.getContainer().submit(new ProfitTracker());			
		} else {
			jobList.add(new Dropper());
			jobList.add(rocktimer.new MiningAreaCheck());
		}
		rocktimer = new RockTimer();		
		jobList.add(rocktimer.new FindRock());
		jobList.add(rocktimer.new HoverOverNextRock());
		//jobList.add(rocktimer.new MiningTimer());
		jobList.add(rocktimer.new RunCheck());		
		jobList.add(new Cam());
		//jobs = new Tree(jobList.toArray(new Node[jobList.size()]));	
		getContainer().submit(rocktimer.new RockUpdater());
		getContainer().submit(rocktimer.new MiningTimer());	
	}
	
	private final Filter<GroundItem> LootFilter = new Filter<GroundItem>() {
		
		// hash table to cache item price
		private final Hashtable<String, Integer> PriceTable = new Hashtable<String, Integer>();
		
		private final static int MIN_PRICE = 800;
		
		
		public boolean accept(GroundItem loot) {			
			final Item item = loot.getGroundItem();
			// we need the Item to get the stack size and the item name so need to do a null check incase of random error
			if(item == null) return false;
			
			// look in table for price of item by checking for the items name in the table
			Integer price = PriceTable.get(item.getName());
			if(price == null)  {
				// submit a threaded price loader so we do not lag our script
				getContainer().submit(new PriceLoader(item.getName()));
				// put in a temporary place holder until the result is found then it will be overriden when the thread is done			
				price = PriceTable.put(item.getName(), 0);				
			}
			// check if the item price times the stack size is greater than or equal to the MIN_PRICE
			return price * loot.getGroundItem().getStackSize() >= MIN_PRICE;			
		}
		
		class PriceLoader extends Task {
			
			private final String ItemName;
			
			public PriceLoader(final String ItemName) {
				this.ItemName = ItemName;
			}
			
			@Override
			public void execute() {
				final GeItem geInfo = GeItem.lookup(ItemName);
				 // if the item cannot be found found we can assume it is not sellable for give it a price of 0
				PriceTable.put(ItemName, geInfo != null ? geInfo.getPrice() : 0);
			}
			
		}
	};
	
	@Override public void onStart() {
		if(Inventory.getCount(Banker.KEEP_IDS) > 0) depositAll = false;
		skilldata = new SkillData();				
		new MiningGUI();
		getContainer().submit(new GuiChecker());
	};
	
	
	@Override
	public int loop() {
		for (Node curjob : jobList) {
			if(curjob.activate()) {
				curjob.execute();
				return 50;
			}
		}
		return 50;
	}
	
	public final class GuiChecker extends LoopTask {

		@Override
		public int loop() {			
			if(guiDone) {
				if(killScript) stop();
				else startScript();
				return -1;
			}
			if(updateArea != -1) {
				if(useResource) {
					RockTimer.makeMiningArea(updateArea, new Tile(1063, 4575, 0));
				} else {
					RockTimer.makeMiningArea(updateArea, Players.getLocal().getLocation());
				}				
				updateArea = -1;
				
			}

			return 50;
		}		
	}
	
	
	
	

	
	public static final class Cam extends Node {	
		
		private static final Timer cameraTimer  = new Timer(0);
		private final int START_ANGLE;
		
		public Cam() {
			START_ANGLE = Camera.getYaw();
		}
		
		@Override
		public void execute() {
			RockTimer.state = "Moving Camera";
			Camera.setPitch(Random.nextInt(90, 100));
			Camera.setAngle(Random.nextInt(START_ANGLE - 20, START_ANGLE + 20));
			cameraTimer.setEndIn(Random.nextInt(180, 240) * 1000);
		}
		
		@Override
		public boolean activate(){
			return !cameraTimer.isRunning() || Camera.getPitch() < 50;		
		}
	}
	
	public static class Dropper extends Node {
		
		public static boolean hop = true;
		public static int[] skiplistArray = new int[0];
		
		public Dropper() {
			final int[] PICK_IDS = {1265, 1267, 1269, 1271, 1273, 1275, 15259};
			final int[] Temp = skiplistArray;
			skiplistArray = new int[skiplistArray.length + PICK_IDS.length];
			int i;
			for(i = 0; i < PICK_IDS.length; i++) {
				skiplistArray[i] =  PICK_IDS[i];
			}
			for(int j = 0; j < Temp.length; j++) {
				skiplistArray[j+i] =  Temp[j];
			}
			for (int ID : skiplistArray) {
				System.out.print(ID + ", ");
			}			
		}
		
		@Override
		public final void execute() {
			RockTimer.state = "Dropping";
			Drop.dropAllExcept(hop, skiplistArray);
		}
		
		@Override
		public final boolean activate(){	
			return Inventory.isFull();	
		}
	}
	
	@Override
	public void onRepaint(Graphics g) {		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		RockTimer.drawRocks(g);
		final Font font = new Font(g.getFont().getName(), Font.PLAIN, 12);
		g.setFont(font);
		skilldata.drawSkillPaint(g, "    " + RockTimer.state);
		ProfitTracker.drawProfitTracker(g);
		drawMouse(g);
	}
	
	public void drawMouse(Graphics g) {
		g.setColor(Mouse.isPressed() ? Color.RED : Color.GREEN);
		final Point m = Mouse.getLocation();
		g.drawLine(m.x -5, m.y + 5, m.x + 5, m.y - 5);
		g.drawLine(m.x -5, m.y - 5, m.x + 5, m.y + 5);		
	}
	
	public void drawPlusMouse(Graphics g) {
		g.setColor(Mouse.isPressed() ? Color.RED : Color.GREEN);
		final Point m = Mouse.getLocation();
		g.drawLine(m.x -5, m.y, m.x + 5, m.y);
		g.drawLine(m.x, m.y - 5, m.x, m.y + 5);		
	}

	@Override
	public void messageReceived(MessageEvent e) {		
		if(CoalBag.isValid()) {
			CoalBag.checkMessage(e);
		}
		if(ProfitTracker.isValid() && e.getId() == 109) {
			ProfitTracker.updateAmounts(e.getMessage());
		}		
	}
	
}

