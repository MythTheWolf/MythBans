package com.myththewolf.MythBans.lib.feilds;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class AbstractMaps {
	public static HashMap<String, Integer> new_join_count;
	public static HashMap<String, Boolean> read_rules;
	public static String CONSOLE_TAIL_THREAD;
	public static HashMap<String, String> aliases;
	public static HashMap<Integer, String> commands;
	public static HashMap<String, Boolean> randVals;

	public static void buildMaps() {
		new_join_count = new HashMap<>();
		read_rules = new HashMap<>();
		randVals = new HashMap<>();
		randVals.put("SRV_DATABASE_UPGRADE", false);
	}

	public void generateCommandMaps() {
		try {
			File pluginYAML = new File(getClass().getResource("plugin.yml").toURI());
			FileConfiguration CFG = YamlConfiguration.loadConfiguration(pluginYAML);
			int slot = 0;
			for (String S : CFG.getStringList("commands")) {
				commands.put(slot, S);
				slot++;
				for (String I : CFG.getString("commands." + S).split(",")) {
					aliases.put(S, I);
				}
			}

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean serverIsUpgrading() {
		return randVals.get("SRV_DATABASE_UPGRADE");
	}

	public static void setUpgrade(boolean val) {
		randVals.put("SRV_DATABASE_UPGRADE", val);
	}
}
