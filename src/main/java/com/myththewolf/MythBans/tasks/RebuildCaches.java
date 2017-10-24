package com.myththewolf.MythBans.tasks;

import com.myththewolf.MythBans.lib.feilds.DataCache;

public class RebuildCaches implements Runnable {

	public RebuildCaches() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		DataCache.rebuildCaches();
	}

}
