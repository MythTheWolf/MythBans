package com.myththewolf.MythBans;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.myththewolf.MythBans.lib.MythBans;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.AbstractMaps;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.tool.MythDate;
import com.myththewolf.MythBans.tasks.DisableDueToError;

public class Startup extends JavaPlugin {
	private Logger MythLogger = this.getLogger();
	private MythBans MB;

	public void onEnable() {

		/*
		 * ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)
		 * org.slf4j.LoggerFactory
		 * .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		 * root.setLevel(Level.INFO);
		 */
		MythBans mb = new MythBans(this);
		mb.loadConfig(this);
	}

	public void onConfigReady(MythBans mb) {
		mb.loadCommands();
		mb.loadEvents();
		try {
			mb.startDaemon();
			mb.loadMySQL();
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
		AbstractMaps.buildMaps();
		DataCache.makeMap();
		if (MythSQLConnect.hasErrored()) {
			System.out.println("Dected error in startup, shutting self down.");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		if (ConfigProperties.USE_DISCORD) {

		}
	}

	public void onDisable() {

		MythDate date = new MythDate();
		try {
			for (Player p : Bukkit.getOnlinePlayers()) {
				MythPlayer pClass = new MythPlayer(p.getUniqueId().toString());
				pClass.setQuitTime(date.formatDate(date.getNewDate()));
				long tick = p.getStatistic(Statistic.PLAY_ONE_TICK);
				pClass.setPlayTime((tick / 20) * 1000);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public MythBans getInstance() {
		return this.MB;
	}
}
