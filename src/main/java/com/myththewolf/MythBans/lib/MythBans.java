package com.myththewolf.MythBans.lib;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class MythBans {
	private JavaPlugin MythPlugin;
	
	public MythBans(JavaPlugin inst)
	{
		this.MythPlugin = inst;
	}
	
	public void loadConfig() {
        try {
            if (!MythPlugin.getDataFolder().exists()) {
            	MythPlugin.getDataFolder().mkdirs();
            }
            File file = new File(MythPlugin.getDataFolder(), "config.yml");
            if (!file.exists()) {
            	MythPlugin.getLogger().info("Config.yml not found, creating!");
            	MythPlugin.saveDefaultConfig();
            } else {
            	MythPlugin.getLogger().info("Config.yml found, loading!");
            	ConfigProperties.dumpProperties(MythPlugin);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
	
	
	
	public void loadMySQL()
	{
		MythSQLConnect msc = new MythSQLConnect();
		msc.getConnection();
		msc.makeTables();
	}
}
