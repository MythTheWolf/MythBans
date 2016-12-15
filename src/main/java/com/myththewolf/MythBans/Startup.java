package com.myththewolf.MythBans;



import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.MythBans;



public class Startup extends JavaPlugin {
	private Logger MythLogger = this.getLogger();
	public void onEnable()
	{
		MythBans mb = new MythBans(this);
		mb.loadConfig();
		mb.loadMySQL();
		MythLogger.info("Loaded 6 tables.");
	}

}
