package com.myththewolf.MythBans;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.MythBans;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.tool.Date;

import ch.qos.logback.classic.Level;



public class Startup extends JavaPlugin {
	private Logger MythLogger = this.getLogger();
	private MythBans MB;

	public void onEnable() {
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
				.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
		MythBans mb = new MythBans(this);
		mb.loadConfig();
		if (mb.loadMySQL() == null) {
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		this.MB = mb;
		mb.loadCommands();
		MythLogger.info("Loaded 6 tables.");
		mb.startDaemon();

		
		if (ConfigProperties.use_bot) {
			mb.startDiscordBot();
			ConfigProperties.dumpDiscord();
			
		}
		
		
		mb.loadEvents();
		try {
			System.out.println("****** RUNNING TESTS ******");
			if(!mb.runTests()){
				this.getPluginLoader().disablePlugin(this);
				System.out.println("****** Disabling Plugin due to test failure ******");
			}
		} catch (IOException e) {
			this.getPluginLoader().disablePlugin(this);
			System.out.println("****** Disabling Plugin due to test failure ******");
		}
		System.out.println("****** Tests Passed! ******");
	}

	public void onDisable() {
		com.myththewolf.MythBans.lib.player.Player pClass = new com.myththewolf.MythBans.lib.player.Player();
		Date date = new Date();
		try {
			for (Player p : Bukkit.getOnlinePlayers()) {
				String UUID = p.getUniqueId().toString();
				pClass.setQuitTime(date.formatDate(date.getNewDate()), UUID);
				pClass.setPlayTime(UUID, date.getTimeDifference(pClass.getSessionJoinDate(UUID), date.getNewDate())
						+ pClass.getPlayTime(UUID));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (ConfigProperties.use_bot) {
			this.MB.shutdown();
		}
	}
}
