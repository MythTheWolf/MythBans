package com.myththewolf.MythBans.lib.feilds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;

import net.md_5.bungee.api.ChatColor;

public class ConfigProperties {
	public static String SYSTEM_LOCALE = "en_US";
	public static final String VERSION = "2.6.1";
	public static final String[] LANGS = { "en_US" };
	public static String SERVER_UNMUTE_FORMAT;
	public static String SQL_HOST;
	public static String SQL_PORT;
	public static String SQL_DATABASE;
	public static String SQL_USERNAME;
	public static String SQL_PASSWORD;
	public static int CRON_INTERVAL;
	public static String PREFIX;
	public static String VIEWMSG_PERM;
	public static String USER_BAN_FORMAT;
	public static String USER_KICK_FORMAT;
	public static String USER_TEMPBAN_FORMAT;
	public static String USER_MUTE_FORMAT;
	public static String USER_IPMUTE_FORMAT;
	public static String USER_IPBAN_FORMAT;
	public static String USER_IPKICK_FORMAT;
	public static String USER_IPTEMPBAN_FORMAT;
	public static String SERVER_BAN_FORMAT;
	public static String SERVER_KICK_FORMAT;
	public static String SERVER_IPUNBAN_FORMAT;
	public static String SERVER_TEMPBAN_FORMAT;
	public static String SERVER_MUTE_FORMAT;
	public static String SERVER_IPBAN_FORMAT;
	public static String SERVER_IPTEMPBAN_FORMAT;
	public static String SERVER_IPMUTE_FORMAT;
	public static String SERVER_IPKICK_FORMAT;
	public static String CREATE_UI_PERMISSION;
	public static String PARDON_PERMISSION;
	public static Boolean DEBUG;
	public static String API_URL;
	public static String API_KEY;
	public static String DEFAULT_BAN_REASON;
	public static String BAN_PERMISSION;
	public static String TEMPBAN_PERMISSION;
	public static String KICK_PERMISSION;
	public static String MUTE_PERMISSION;
	public static String BANIP_PERMISSION;
	public static String TEMPBANIP_PERMISSION;
	public static String MUTEIP_PERMISSION;
	public static String KICKIP_PERMISSION;
	public static String VIEW_PROBATION_PERMISSION;
	public static String PROBATION_PERMISSION;
	public static String CLOSETICKET_PERMISSION;
	public static String TICKETS_OTHER_PERMISSION;
	public static String SOCIALSPY_PERMISSION;
	private static Logger MythLog;
	public static boolean use_bot;
	public static String POTATO_PERM;
	public static String SERVER_PARDON_FORMAT;
	public static String USER_UNMUTE_FORMAT;
	public static String STAFF_CHAT_SEND;
	public static String STAFF_CHAT_GET;
	public static String IMPORTANT_SEND;
	public static String BOT_API;
	public static JavaPlugin pl;
	public static boolean DISCORD_SETUP;
	public static String TEMP_THREAD;
	public static String DISCORD_SERVER_ID;
	public static String DISCORD_MINECRAFT_CHANNEL_ID;
	public static String DISCORD_LOGGER_ID;
	public static String DISCORD_THREAD_ID;
	public static String RELOAD;
	public static String SOFTMUTE_RELEASE_COMMAND;
	public static boolean AUTO_MUTE = false;
	public static FileConfiguration lang;
	public static HashMap<String,FileConfiguration> langMap = new HashMap<String,FileConfiguration>();
	public static void dumpProperties(FileConfiguration cfg) {
		try {

			SQL_HOST = cfg.getString("SQL-HOST");
			SQL_PORT = cfg.getString("SQL-PORT");
			SQL_DATABASE = cfg.getString("SQL-DATABASE");
			SQL_USERNAME = cfg.getString("SQL-USER");
			SQL_PASSWORD = cfg.getString("SQL-PASSWORD");
			CRON_INTERVAL = cfg.getInt("CRON-UDPATE-TIME");
			PREFIX = ChatColor.translateAlternateColorCodes('&', cfg.getString("PREFIX"));
			VIEWMSG_PERM = ChatColor.translateAlternateColorCodes('&', cfg.getString("BANMESSAGE-PERMISSION"));
			DEBUG = cfg.getBoolean("DEBUG-MODE");
			API_URL = ChatColor.translateAlternateColorCodes('&', cfg.getString("API-STARTPOINT"));
			API_KEY = ChatColor.translateAlternateColorCodes('&', cfg.getString("API-KEY"));
			USER_BAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-BAN-FORMAT"));
			USER_KICK_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-KICK-FORMAT"));
			USER_TEMPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-TEMPBAN-FORMAT"));
			USER_UNMUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-UNMUTE-FORMAT"));
			USER_MUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-MUTE-FORMAT"));
			USER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-IP-BAN-FORMAT"));
			USER_IPTEMPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&',
					cfg.getString("USER-IP-TEMPBAN-FORMAT"));
			USER_IPKICK_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-IP-KICK-FORMAT"));
			USER_IPMUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-IP-MUTE-FORMAT"));
			SERVER_BAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-BAN-FORMAT"));
			SERVER_KICK_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-KICK-FORMAT"));
			SERVER_TEMPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-TEMPBAN-FORMAT"));
			SERVER_MUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-MUTE-FORMAT"));
			SERVER_UNMUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-UNMUTE-FORMAT"));
			SERVER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-IP-BAN-FORMAT"));
			SERVER_IPTEMPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&',
					cfg.getString("SERVER-IP-TEMPBAN-FORMAT"));
			SERVER_IPMUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-IP-MUTE-FORMAT"));
			SERVER_IPKICK_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-IP-KICK-FORMAT"));
			SERVER_PARDON_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-PARDON-FORMAT"));
			SERVER_IPUNBAN_FORMAT = ChatColor.translateAlternateColorCodes('&',
					cfg.getString("SERVER-IPPARDON-FORMAT"));
			DEFAULT_BAN_REASON = ChatColor.translateAlternateColorCodes('&', cfg.getString("DEFAULT-BAN-REASON"));
			BAN_PERMISSION = cfg.getString("BAN-PERMISSION");
			TEMPBAN_PERMISSION = cfg.getString("TEMPBAN-PERMISSION");
			KICK_PERMISSION = cfg.getString("KICK-PERMISSION");
			MUTE_PERMISSION = cfg.getString("MUTE-PERMISSION");
			BANIP_PERMISSION = cfg.getString("BANIP-PERMISSION");
			TEMPBANIP_PERMISSION = cfg.getString("TEMPBANIP-PERMISSION");
			MUTEIP_PERMISSION = cfg.getString("MUTEIP-PERMISSION");
			KICKIP_PERMISSION = cfg.getString("KICKIP-PERMISSION");
			VIEW_PROBATION_PERMISSION = cfg.getString("VIEW-PROBATIONS-PERMISSION");
			PROBATION_PERMISSION = cfg.getString("PROBATION-PERMISSION");
			CREATE_UI_PERMISSION = cfg.getString("CREATE-UI-PERMISSION");
			PARDON_PERMISSION = cfg.getString("PARDON-PERMISSION");
			CLOSETICKET_PERMISSION = cfg.getString("CLOSE-TICKET-PERMISSION");
			TICKETS_OTHER_PERMISSION = cfg.getString("VIEW-SERVER-TICKETS-PERMISSION");
			IMPORTANT_SEND = cfg.getString("IMPORTANT-SEND");
			STAFF_CHAT_GET = cfg.getString("STAFF-CHAT-GET");
			STAFF_CHAT_SEND = cfg.getString("STAFF-CHAT-SEND");
			BOT_API = cfg.getString("DISCORD-BOT-KEY");
			use_bot = cfg.getBoolean("USE-BOT");
			POTATO_PERM = cfg.getString("POTATO-PERMISSION");
			RELOAD = cfg.getString("RELOAD-PERMISSION");
			SOFTMUTE_RELEASE_COMMAND = cfg.getString("SOFTMUTE-RELEASE");
			SOCIALSPY_PERMISSION = cfg.getString("SOCIALSPY-PERMISSION");
			AUTO_MUTE = cfg.getBoolean("AUTO-MUTE");
			if (cfg.getBoolean("use_bot") == false) {
				DISCORD_SETUP = false;
			}
		} catch (Exception e) {
			MythLog.severe("Could not load MythBans... Malformed Config!");
			e.printStackTrace();
		}
	}

	public static void dumpDiscord() {
		MythLog.info("Dumping..");
		Connection c = MythSQLConnect.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = (PreparedStatement) c.prepareStatement("SELECT * FROM Mythbans_Discord WHERE `key` = ?");
			ps.setString(1, "SERVER-SETUP");
			rs = ps.executeQuery();
			while (rs.next()) {
				DISCORD_SETUP = rs.getBoolean("value");
			}
			ps.setString(1, "SERVER-ID");
			rs = ps.executeQuery();
			while (rs.next()) {
				DISCORD_SERVER_ID = rs.getString("value");
			}
			ps.setString(1, "MINECRAFT-CHANNEL-ID");
			rs = ps.executeQuery();
			while (rs.next()) {
				DISCORD_MINECRAFT_CHANNEL_ID = rs.getString("value");
				System.out.println(DISCORD_MINECRAFT_CHANNEL_ID);
			}
			ps.setString(1, "LOG-CHANNEL-ID");
			rs = ps.executeQuery();
			while (rs.next()) {
				DISCORD_LOGGER_ID = rs.getString("value");
				System.out.println(DISCORD_LOGGER_ID);
			}
			ps.setString(1, "SERVER-CHAT-MESSAGE-ID");
			rs = ps.executeQuery();
			while (rs.next()) {
				DISCORD_THREAD_ID = rs.getString("value");
				System.out.println(DISCORD_THREAD_ID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void writeDiscordInfo(String serverID, String MCID, String logID) {
		Connection c = MythSQLConnect.getConnection();
		PreparedStatement ps;
		try {
			ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_Discord  SET `value` = ? WHERE  `key` = ?");
			ps.setString(2, "SERVER-SETUP");
			ps.setString(1, "true");
			ps.executeUpdate();
			ps.setString(2, "SERVER-ID");
			ps.setString(1, serverID);
			ps.executeUpdate();
			ps.setString(2, "MINECRAFT-CHANNEL-ID");
			ps.setString(1, MCID);
			ps.executeUpdate();
			ps.setString(2, "LOG-CHANNEL-ID");
			ps.setString(1, logID);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void updateThread(String newID) {
		Connection c = MythSQLConnect.getConnection();
		PreparedStatement ps;
		try {
			ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_Discord  SET `value` = ? WHERE  `key` = ?");
			ps.setString(2, "SERVER-CHAT-MESSAGE-ID");
			ps.setString(1, newID);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}
