package com.myththewolf.MythBans;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.myththewolf.MythBans.lib.MythBans;
import com.myththewolf.MythBans.lib.discord.MythDiscordBot;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.tool.Date;
import com.myththewolf.MythBans.tasks.DisableDueToError;

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

		MythLogger.info("Loaded 6 tables.");
		

		if (ConfigProperties.use_bot) {
			mb.startDiscordBot();
			ConfigProperties.dumpDiscord();

		}
		mb.loadCommands();
		mb.loadEvents();
		try {
			mb.startDaemon();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean RUN = false;
		try {
			System.out.println("****** RUNNING TESTS ******");
			RUN = mb.runTests();
			if (!RUN) {
				this.MythLogger.severe("Lang files are messed up! Run 'mbfix' to try to fix this issue.");
				this.MythLogger
						.severe("MythBans will disable istself in 1 minute, if you want to run fixes, do them now.");
				BukkitTask ID = Bukkit.getScheduler().runTaskLater(this, new DisableDueToError(this), 1200L);
				MB.setDisableTask(ID);
			}
		} catch (IOException e) {
			this.MythLogger.severe("Lang files are messed up! Run 'mbfix' to try to fix this issue.");
			this.MythLogger.severe("MythBans will disable istself in 1 minute, if you want to run fixes, do them now.");
			BukkitTask ID = Bukkit.getScheduler().runTaskLater(this, new DisableDueToError(this), 1200L);
			MB.setDisableTask(ID);
		}
		if (RUN) {
			System.out.println("****** TEST RAN OK ******");
		}

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
		try {
			if (MythDiscordBot.getBot().isSetup()) {
				this.MB.shutdown();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
