package com.myththewolf.MythBans;



import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.MythBans;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;


public class Startup extends JavaPlugin {
	
	public void onEnable()
	{
		MythBans mb = new MythBans(this);
		mb.loadConfig();
		this.getLogger().warning(ConfigProperties.API_KEY);
	}

}
