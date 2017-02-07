package com.myththewolf.MythBans.lib;

import java.io.File;
import java.sql.Connection;
import org.bukkit.plugin.java.JavaPlugin;
import com.myththewolf.MythBans.commands.Ban;
import com.myththewolf.MythBans.commands.IPBan;
import com.myththewolf.MythBans.commands.Kick;
import com.myththewolf.MythBans.commands.Mute;
import com.myththewolf.MythBans.commands.Probate;
import com.myththewolf.MythBans.commands.TempBan;
import com.myththewolf.MythBans.commands.importJSON;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.events.player.PlayerChat;
import com.myththewolf.MythBans.lib.events.player.PlayerJoin;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class MythBans {
	private JavaPlugin MythPlugin;

	public MythBans(JavaPlugin inst) {
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

	public void loadEvents() {
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerChat(), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerJoin(), MythPlugin);
	}

	public Connection loadMySQL() {
		MythSQLConnect msc = new MythSQLConnect();
		if (MythSQLConnect.getConnection() == null) {
			return null;
		}
		msc.makeTables();
		return MythSQLConnect.getConnection();
	}

	public void loadCommands() {
		MythPlugin.getCommand("mute").setExecutor(new Mute());
		MythPlugin.getCommand("ban").setExecutor(new Ban());
		MythPlugin.getCommand("tempban").setExecutor(new TempBan());
		MythPlugin.getCommand("kick").setExecutor(new Kick());
		MythPlugin.getCommand("probate").setExecutor(new Probate());
		MythPlugin.getCommand("banip").setExecutor(new IPBan());
		MythPlugin.getCommand("import").setExecutor(new importJSON(MythPlugin));
	}
}
