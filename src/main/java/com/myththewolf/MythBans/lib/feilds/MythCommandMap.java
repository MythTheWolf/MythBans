package com.myththewolf.MythBans.lib.feilds;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.commands.CallUserName;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;


public class MythCommandMap {
	private JavaPlugin pl;
	private CommandMap cm;
	private PreparedStatement ps;
	private FileConfiguration config;
	private Logger logger;
	private ResultSet rs;
	public MythCommandMap(JavaPlugin instance)
	{
		this.pl = instance;
		this.cm = this.getCommandMap();
		this.config = pl.getConfig();
		this.logger = pl.getLogger();
	}

	public CommandMap getCommandMap()
	{
		try {
			   final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

			   bukkitCommandMap.setAccessible(true);
			   CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

			  return commandMap;
			} catch(Exception e) {
				
			   e.printStackTrace();
			  
			}
		return cm;
	}
	
	public void rebuildCommandMap() throws SQLException
	{
		
		logger.info("Rebuilding Map...");
		ps = (PreparedStatement) MythSQLConnect.getConnection().prepareStatement("SELECT * FROM MythBans_NameCache");
		rs = ps.executeQuery();
		while(rs.next())
		{
			cm.register(rs.getString("tagName"), new CallUserName(rs.getString("tagName")));
			if (config.getBoolean("extended-debug") == true)
			{
				logger.info("Added command: " + rs.getString("tagName"));
			}
		}
		logger.info("Rebuild Complete!");
	}
	
	
}
