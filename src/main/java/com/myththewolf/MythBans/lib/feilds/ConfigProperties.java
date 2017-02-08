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
			SQL_HOST = ChatColor.translateAlternateColorCodes('&', ("SQL-HOST"));
			SQL_PORT = ChatColor.translateAlternateColorCodes('&', ("SQL-PORT"));
			SQL_DATABASE = ChatColor.translateAlternateColorCodes('&', ("SQL-DATABASE"));
			SQL_USERNAME = ChatColor.translateAlternateColorCodes('&', ("SQL-USER"));
			SQL_PASSWORD = ChatColor.translateAlternateColorCodes('&', ("SQL-PASSWORD"));
			CRON_INTERVAL = cfg.getInt("CRON-UDPATE-TIME");
			PREFIX = ChatColor.translateAlternateColorCodes('&', ("PREFIX"));
			VIEWMSG_PERM = ChatColor.translateAlternateColorCodes('&', ("BANMESSAGE-PERMISSION"));
			DEBUG = cfg.getBoolean("DEBUG-MODE");
			API_URL = ChatColor.translateAlternateColorCodes('&', ("API-STARTPOINT"));
			API_KEY = ChatColor.translateAlternateColorCodes('&', ("API-KEY"));
			USER_BAN_FORMAT = ChatColor.translateAlternateColorCodes('&', ("USER-BAN-FORMAT"));
			USER_KICK_FORMAT = ChatColor.translateAlternateColorCodes('&', ("USER-KICK-FORMAT"));
			USER_TEMPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', ("USER-TEMPBAN-FORMAT"));
			USER_MUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', ("USER-MUTE-FORMAT"));
			USER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', ("USER-IPBAN-FORMAT"));
			USER_IPTEMPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', ("USER-IPTEMPBAN-FORMAT"));
			USER_IPKICK_FORMAT = ChatColor.translateAlternateColorCodes('&', ("USER-IPKICK-FORMAT"));
			USER_IPMUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', ("USER-IPMUTE-FORMAT"));
			SERVER_BAN_FORMAT = ChatColor.translateAlternateColorCodes('&', ("SERVER-BAN-FORMAT"));
			SERVER_KICK_FORMAT = ChatColor.translateAlternateColorCodes('&', ("SERVER-KICK-FORMAT"));
			SERVER_TEMPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', ("SERVER-TEMPBAN-FORMAT"));
			SERVER_MUTE_FORMAT = ChatColor.translateAlternateColorCodes('&', ("SERVER-MUTE-FORMAT"));
			SERVER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', ("SERVER-IPBAN-FORMAT"));
			SERVER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', ("SERVER-IPTEMPBAN-FORMAT"));
			SERVER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', ("SERVER-IPMUTE-FORMAT"));
			SERVER_IPBAN_FORMAT = ChatColor.translateAlternateColorCodes('&', ("SERVER-IPKICK-FORMAT"));
			DEFAULT_BAN_REASON = ChatColor.translateAlternateColorCodes('&', ("DEFAULT-BAN-REASON"));
			BAN_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("BAN-PERMISSION"));
			TEMPBAN_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("TEMPBAN-PERMISSION"));
			KICK_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("KICK-PERMISSION"));
			MUTE_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("MUTE-PERMISSION"));
			BANIP_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("BANIP-PERMISSION"));
			TEMPBANIP_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("TEMPBANIP-PERMISSION"));
			MUTEIP_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("MUTEIP-PERMISSION"));
			KICKIP_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("KICKIP-PERMISSION"));
			VIEW_PROBATION_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("VIEW-PROBATIONS-PERMISSION"));
			PROBATION_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("PROBATION-PERMISSION"));
			CREATE_UI_PERMISSION = ChatColor.translateAlternateColorCodes('&', ("CREATE-UI-PERMISSION"));
		} catch (Exception e) {
			MythLog.severe("Could not load MythBans... Malformed Config!");
			MythLog.severe(e.getStackTrace().toString());
		}
	}

}
