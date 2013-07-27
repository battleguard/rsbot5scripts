package com.battleguard.scripts.basickiller;

import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;

@Manifest(authors = { "Battleguard" }, description = "Warped Cockroach Killer", name = "Warped Cockroach Killer")
public class CockroachKiller extends PollingScript {

	@Override
	public int poll() {
		return 50;
	}

}
