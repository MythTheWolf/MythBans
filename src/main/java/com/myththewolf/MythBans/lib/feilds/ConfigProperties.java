package com.myththewolf.MythBans.lib.feilds;

import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class ConfigProperties {
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
	public static String SERVER_TEMPBAN_FORMAT;
	public static String SERVER_MUTE_FORMAT;
	public static String SERVER_IPBAN_FORMAT;
	public static String SERVER_IPTEMPBAN_FORMAT;
	public static String SERVER_IPMUTE_FORMAT;
	public static String SERVER_IPKICK_FORMAT;
	public static String CREATE_UI_PERMISSION;
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
	private static FileConfiguration cfg;
	private static Logger MythLog;

	public static void dumpProperties(JavaPlugin i) {
		MythLog = i.getLogger();
		try {
			cfg = i.getConfig();
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
			USER_MUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-MUTE-FORMAT"));
			USER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-IP-BAN-FORMAT"));
			USER_IPTEMPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-IP-TEMPBAN-FORMAT"));
			USER_IPKICK_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-IP-KICK-FORMAT"));
			USER_IPMUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("USER-IP-MUTE-FORMAT"));
			SERVER_BAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-BAN-FORMAT"));
			SERVER_KICK_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-KICK-FORMAT"));
			SERVER_TEMPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-TEMPBAN-FORMAT"));
			SERVER_MUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-MUTE-FORMAT"));
			SERVER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-IP-BAN-FORMAT"));
			SERVER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-IP-TEMPBAN-FORMAT"));
			SERVER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-IP-MUTE-FORMAT"));
			SERVER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', cfg.getString("SERVER-IP-KICK-FORMAT"));
			DEFAULT_BAN_REASON = ChatColor.translateAlternateColorCodes('&', cfg.getString("DEFAULT-BAN-REASON"));
			BAN_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("BAN-PERMISSION"));
			TEMPBAN_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("TEMPBAN-PERMISSION"));
			KICK_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("KICK-PERMISSION"));
			MUTE_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("MUTE-PERMISSION"));
			BANIP_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("BANIP-PERMISSION"));
			TEMPBANIP_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("TEMPBANIP-PERMISSION"));
			MUTEIP_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("MUTEIP-PERMISSION"));
			KICKIP_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("KICKIP-PERMISSION"));
			VIEW_PROBATION_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("VIEW-PROBATIONS-PERMISSION"));
			PROBATION_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("PROBATION-PERMISSION"));
			CREATE_UI_PERMISSION = ChatColor.translateAlternateColorCodes('&', cfg.getString("CREATE-UI-PERMISSION"));
		} catch (Exception e) {
			MythLog.severe("Could not load MythBans... Malformed Config!");
			e.printStackTrace();
		}
	}

}
