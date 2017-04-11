package com.myththewolf.MythBans.lib;

import java.io.File;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.commands.Ban;
import com.myththewolf.MythBans.commands.ClearChat;
import com.myththewolf.MythBans.commands.IPBan;
import com.myththewolf.MythBans.commands.IPKick;
import com.myththewolf.MythBans.commands.Kick;
import com.myththewolf.MythBans.commands.Link;
import com.myththewolf.MythBans.commands.Mute;
import com.myththewolf.MythBans.commands.PardonIP;
import com.myththewolf.MythBans.commands.PardonUser;
import com.myththewolf.MythBans.commands.PlayerTime;
import com.myththewolf.MythBans.commands.Potato;
import com.myththewolf.MythBans.commands.Probate;
import com.myththewolf.MythBans.commands.ReloadMythBans;
import com.myththewolf.MythBans.commands.SocialSpy;
import com.myththewolf.MythBans.commands.TempBan;
import com.myththewolf.MythBans.commands.createUI;
import com.myththewolf.MythBans.commands.getFam;
import com.myththewolf.MythBans.commands.importJSON;
import com.myththewolf.MythBans.commands.mythapi;
import com.myththewolf.MythBans.commands.softmute;
import com.myththewolf.MythBans.commands.user;
import com.myththewolf.MythBans.commands.ticket.CloseTicket;
import com.myththewolf.MythBans.commands.ticket.MyTickets;
import com.myththewolf.MythBans.commands.ticket.ReportGrief;
import com.myththewolf.MythBans.commands.ticket.Ticket;
import com.myththewolf.MythBans.commands.ticket.Tickets;
import com.myththewolf.MythBans.commands.ticket.closedtickets;
import com.myththewolf.MythBans.commands.ticket.tickettp;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.events.player.CommandEvent;
import com.myththewolf.MythBans.lib.events.player.PlayerChat;
import com.myththewolf.MythBans.lib.events.player.PlayerDamageEvent;
import com.myththewolf.MythBans.lib.events.player.PlayerEatEvent;
import com.myththewolf.MythBans.lib.events.player.PlayerJoin;
import com.myththewolf.MythBans.lib.events.player.PlayerQuit;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.tool.LanguageGoverner;
import com.myththewolf.MythBans.threads.AlerResolved;
import com.myththewolf.MythBans.threads.WarnUnsolvedTickets;

public class MythBans {
	private JavaPlugin MythPlugin;
	private MythDiscordBot MBD;

	public MythBans(JavaPlugin inst) {
		this.MythPlugin = inst;
	}

	public void startDiscordBot() {
		MBD = new MythDiscordBot();
		Connection con = MythSQLConnect.getConnection();
		PreparedStatement ps;
		try {
			// Discord table
			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("Loading MySQL Table: Discord");
			}
			DatabaseMetaData dbm = con.getMetaData();
			// check if "employee" table is there
			ResultSet tables = dbm.getTables(null, null, "Mythbans_Discord", null);
			if (tables.next()) {
				// Table exists
			} else {
				ps = (PreparedStatement) con.prepareStatement(
						"CREATE TABLE `MythBans_Discord` ( `ID` INT NOT NULL AUTO_INCREMENT, `key` VARCHAR(255) NULL DEFAULT NULL , `value` VARCHAR(255) NULL DEFAULT NULL , PRIMARY KEY (`ID`) ) ENGINE = InnoDB;");
				ps.executeUpdate();
				ps = null;
				ps = (PreparedStatement) con
						.prepareStatement("INSERT INTO MythBans_Discord (`key`,value) VALUES ( ? , ? )");
				ps.setString(1, "SERVER-SETUP");
				ps.setString(2, "false");
				ps.executeUpdate();
				ps.setString(1, "SERVER-ID");
				ps.setString(2, "");
				ps.executeUpdate();
				ps.setString(1, "MINECRAFT-CHANNEL-ID");
				ps.setString(2, "");
				ps.executeUpdate();
				ps.setString(1, "LOG-CHANNEL-ID");
				ps.setString(2, "");
				ps.executeUpdate();
				ps.setString(1, "SERVER-CHAT-MESSAGE-ID");
				ps.setString(2, "");
				ps.executeUpdate();

			}

			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("All MySQL tables generated.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void loadConfig() {

		try {
			if (!MythPlugin.getDataFolder().exists()) {
				MythPlugin.getDataFolder().mkdirs();

			}
			File FF = new File(MythPlugin.getDataFolder() + File.separator + "lang");
			if (!FF.exists()) {
				FF.mkdirs();
			}
			for (String theLanguage : ConfigProperties.LANGS) {
				File specialf = new File(MythPlugin.getDataFolder() + File.separator + "lang", theLanguage + ".yml");
				if (!specialf.exists()) {
					specialf.getParentFile().mkdirs();
					MythPlugin.saveResource("lang" + File.separator + theLanguage + ".yml", false);
				}

				FileConfiguration daFonts = YamlConfiguration.loadConfiguration(specialf);
				ConfigProperties.langMap.put(theLanguage, daFonts);
			}
			File file = new File(MythPlugin.getDataFolder(), "config.yml");
			if (!file.exists()) {
				MythPlugin.getLogger().info("Config.yml not found, creating!");
				MythPlugin.saveDefaultConfig();
				ConfigProperties.dumpProperties(MythPlugin.getConfig());
			} else {
				MythPlugin.getLogger().info("Config.yml found, loading!");
				ConfigProperties.dumpProperties(MythPlugin.getConfig());
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void loadEvents() {

		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerChat(MBD), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerJoin(MythPlugin), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerQuit(), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerEatEvent(MythPlugin), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerDamageEvent(), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new CommandEvent(MythPlugin), MythPlugin);
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
		MythPlugin.getCommand("reportGreif").setExecutor(new ReportGrief());
		MythPlugin.getCommand("playertime").setExecutor(new PlayerTime());
		MythPlugin.getCommand("tickettp").setExecutor(new tickettp());
		MythPlugin.getCommand("tickets").setExecutor(new Tickets());
		MythPlugin.getCommand("mytickets").setExecutor(new MyTickets());
		MythPlugin.getCommand("ticket").setExecutor(new Ticket());
		MythPlugin.getCommand("close").setExecutor(new CloseTicket());
		MythPlugin.getCommand("mythbans").setExecutor(new mythapi());
		MythPlugin.getCommand("closedtickets").setExecutor(new closedtickets());
		MythPlugin.getCommand("player").setExecutor(new user());
		MythPlugin.getCommand("link").setExecutor(new Link());
		MythPlugin.getCommand("potato").setExecutor(new Potato(MythPlugin));
		MythPlugin.getCommand("softmute").setExecutor(new softmute());
		MythPlugin.getCommand("mbreload").setExecutor(new ReloadMythBans(MythPlugin));
		MythPlugin.getCommand("socialspy").setExecutor(new SocialSpy());
		MythPlugin.getCommand("clearchat").setExecutor(new ClearChat());
	}

	public void buildCommandMap() {

	}

	public boolean runTests() throws IOException {
		File specialf = new File(MythPlugin.getDataFolder() + File.separator + "lang" + File.separator+ "expected_codes.txt");

		specialf.getParentFile().mkdirs();
		MythPlugin.saveResource("lang" + File.separator + "expected_codes.txt", true);
		LanguageGoverner LG = new LanguageGoverner(specialf);
		return LG.run();
	}

	public void startDaemon() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MythPlugin, new WarnUnsolvedTickets(MythPlugin), 20, 6000);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MythPlugin, new AlerResolved(), 20, 3000);
	}

	public void shutdown() {

		Bukkit.getScheduler().cancelAllTasks();
		if (ConfigProperties.use_bot) {
			MBD.disconnect();
		}
	}
}