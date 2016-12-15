package com.myththewolf.MythBans;



import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.MythBans;


public class Startup extends JavaPlugin {
	
	public void onEnable()
	{
		MythBans mb = new MythBans(this);
		mb.loadConfig();
		mb.loadMySQL();
	}

}
