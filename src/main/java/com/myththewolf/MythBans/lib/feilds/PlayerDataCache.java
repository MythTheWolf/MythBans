package com.myththewolf.MythBans.lib.feilds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.myththewolf.MythBans.lib.player.MythPlayer;

public class PlayerDataCache {

	private static HashMap<String, MythPlayer> PlayerMap;

	public static MythPlayer getInstance(String UUID) {
		if (!PlayerMap.containsKey(UUID)) {
			PlayerMap.put(UUID, new MythPlayer(UUID));
		}
		return PlayerMap.get(UUID);
	}

	public static void rebuildCaches() {

		Iterator<Entry<String, MythPlayer>> it = PlayerMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, MythPlayer> pair = it.next();
			PlayerMap.put(pair.getKey(), new MythPlayer(pair.getKey()));
			it.remove(); // avoids a ConcurrentModificationException
		}
	}

	public static void rebuildUser(String UUID) {
		PlayerMap.put(UUID, new MythPlayer(UUID));
	}

	public static void makeMap() {
		PlayerMap = new HashMap<String, MythPlayer>();

	}

}
