package com.myththewolf.MythBans.lib;

import java.io.File;
import java.sql.Connection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.commands.Ban;
import com.myththewolf.MythBans.commands.Compare;
import com.myththewolf.MythBans.commands.IPBan;
import com.myththewolf.MythBans.commands.IPKick;
import com.myththewolf.MythBans.commands.Kick;
import com.myththewolf.MythBans.commands.Mute;
import com.myththewolf.MythBans.commands.PardonIP;
import com.myththewolf.MythBans.commands.PardonUser;
import com.myththewolf.MythBans.commands.PlayerTime;
import com.myththewolf.MythBans.commands.Probate;
import com.myththewolf.MythBans.commands.TempBan;
import com.myththewolf.MythBans.commands.createUI;
import com.myththewolf.MythBans.commands.getFam;
import com.myththewolf.MythBans.commands.importJSON;
import com.myththewolf.MythBans.commands.ticket.CloseTicket;
import com.myththewolf.MythBans.commands.ticket.MyTickets;
import com.myththewolf.MythBans.commands.ticket.ReportGrief;
import com.myththewolf.MythBans.commands.ticket.Ticket;
import com.myththewolf.MythBans.commands.ticket.Tickets;
import com.myththewolf.MythBans.commands.ticket.tickettp;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.events.player.PlayerChat;
import com.myththewolf.MythBans.lib.events.player.PlayerJoin;
import com.myththewolf.MythBans.lib.events.player.PlayerQuit;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.threads.WarnUnsolvedTickets;

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
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerQuit(), MythPlugin);
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
		MythPlugin.getCommand("banip").setExecutor(new IPBan(MythPlugin));
		MythPlugin.getCommand("import").setExecutor(new importJSON(MythPlugin));
		MythPlugin.getCommand("createUI").setExecutor(new createUI());
		MythPlugin.getCommand("pardon").setExecutor(new PardonUser());
		MythPlugin.getCommand("pardonIP").setExecutor(new PardonIP(MythPlugin));
		MythPlugin.getCommand("getFam").setExecutor(new getFam());
		MythPlugin.getCommand("kickip").setExecutor(new IPKick(MythPlugin));
		MythPlugin.getCommand("compare").setExecutor(new Compare());
		MythPlugin.getCommand("reportGreif").setExecutor(new ReportGrief());
		MythPlugin.getCommand("playertime").setExecutor(new PlayerTime());
		MythPlugin.getCommand("tickettp").setExecutor(new tickettp());
		MythPlugin.getCommand("tickets").setExecutor(new Tickets());
		MythPlugin.getCommand("mytickets").setExecutor(new MyTickets());
		MythPlugin.getCommand("ticket").setExecutor(new Ticket());
		MythPlugin.getCommand("close").setExecutor(new CloseTicket());
	}

	public void buildCommandMap() {

	}

	public void startDaemon() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MythPlugin, new WarnUnsolvedTickets(MythPlugin), 20, 6000);
	}
}
