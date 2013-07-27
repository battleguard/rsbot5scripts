package com.battleguard.scripts.spidersilk.util.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public final class GrandExchange {

	/**
	 * Looks up grand exchange information and returns a string array with the following contents
	 * String[0] = item name
	 * String[1] = item price
	 * @param itemID for the item being looked up on the grand exchange
	 * @return : a string array of grand exchange information on the item id provided
	 */
	public static final String[] lookup(final int itemID) {		
		try {
			final String[] info = {"0", "0"};
			final URL url = new URL("http://www.tip.it/runescape/index.php?gec&itemid=" + itemID);
			final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String input;
			while ((input = br.readLine()) != null) {
				if(input.startsWith("        	<title>")) {	
					info[0] = input.substring(16, input.indexOf('-') - 1);
				}
				if(input.startsWith("<tr><td colspan=\"4\"><b>Current Market Price: </b>")) {
					info[1] = input.substring(49, input.lastIndexOf("gp")).replaceAll(",", "");
					return info;
				}
			}
		} catch (final Exception ignored) {}
	return new String[] {"Error", "0"};
	}
	
}
