package SpiderSilk.util;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.widget.WidgetChild;


public enum LoadStone {		
	EDGEVILLE(45),
	LUMBRIDGE(47);
		
	private static final int MAP_WIDGET = 1092;
	private static final int ACTION_BAR_WIDGET = 640;
	private static final int ACTION_BAR_SLOT_WIDGET = 67;
	private static final int ANIMATION = 16385;
		
	private final int widgetIndex;	
	
	LoadStone(final int widgetIndex) {
		this.widgetIndex = widgetIndex;
	}	
	
	/**
	 * Assumes loadstones are already activated and loadstone spell is on far right action bar.
	 */
	public void teleport() {
		final WidgetChild portButton = Widgets.get(ACTION_BAR_WIDGET, ACTION_BAR_SLOT_WIDGET);		
		if(portButton != null && portButton.validate() && portButton.click(true)) {
			Task.sleep(1000);
		}			
		final WidgetChild port = Widgets.get(MAP_WIDGET, widgetIndex);
		if(port != null && port.validate() && port.click(true)) {
			waitWhileAnim(ANIMATION, 15000);
		}		
	}
	
	public static boolean waitWhileAnim(final int id, final int maxWait) {
		Task.sleep(2000);
		Timer timer = new Timer(maxWait);
		while(timer.isRunning() && Players.getLocal().getAnimation() == id) {
			Task.sleep(50);
		}
		Task.sleep(2000);
		return timer.isRunning();
	}	
}


