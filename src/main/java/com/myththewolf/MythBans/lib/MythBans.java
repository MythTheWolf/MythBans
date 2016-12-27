package com.myththewolf.MythBans.lib;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.events.player.PlayerJoin;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerChat;

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
            	ConfigProperties.dumpProperties(MythPlugin);
            } else {
            	MythPlugin.getLogger().info("Config.yml found, loading!");
            	ConfigProperties.dumpProperties(MythPlugin);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
	
	
	public void loadEvents()
	{
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerChat(), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerJoin(),MythPlugin);
	}
	public void loadMySQL()
	{
		MythSQLConnect msc = new MythSQLConnect();
		MythSQLConnect.getConnection();
		msc.makeTables();
	}
}
