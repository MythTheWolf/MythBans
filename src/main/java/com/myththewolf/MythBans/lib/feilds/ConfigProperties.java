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
	public static String VIEW_PROBATION_PERMISSION;
	public static String PROBATION_PERMISSION;
	private static FileConfiguration cfg;
	private static Logger MythLog;
	public static void dumpProperties(JavaPlugin i)
	{
		MythLog = i.getLogger();
		try{
		cfg = i.getConfig();
		SQL_HOST = cfg.getString("SQL-HOST");
		SQL_PORT = cfg.getString("SQL-PORT");
		SQL_DATABASE = cfg.getString("SQL-DATABASE");
		SQL_USERNAME = cfg.getString("SQL-USER");
		SQL_PASSWORD = cfg.getString("SQL-PASSWORD");
		CRON_INTERVAL = cfg.getInt("CRON-UDPATE-TIME");
		PREFIX = ChatColor.translateAlternateColorCodes('&', cfg.getString("PREFIX"));
		VIEWMSG_PERM = cfg.getString("BANMESSAGE-PERMISSION");
		DEBUG = cfg.getBoolean("DEBUG-MODE");
		API_URL = cfg.getString("API-STARTPOINT");
		API_KEY = cfg.getString("API-KEY");
		USER_BAN_FORMAT = cfg.getString("USER-BAN-FORMAT");
		USER_KICK_FORMAT = cfg.getString("USER-KICK-FORMAT");
		USER_TEMPBAN_FORMAT = cfg.getString("USER-TEMPBAN-FORMAT");
		USER_MUTE_FORMAT = cfg.getString("USER-MUTE-FORMAT");
		USER_IPBAN_FORMAT = cfg.getString("USER-IPBAN-FORMAT");
		USER_IPTEMPBAN_FORMAT = cfg.getString("USER-IPTEMPBAN-FORMAT");
		USER_IPKICK_FORMAT = cfg.getString("USER-IPKICK-FORMAT");
		USER_IPMUTE_FORMAT = cfg.getString("USER-IPMUTE-FORMAT");
		SERVER_BAN_FORMAT = cfg.getString("SERVER-BAN-FORMAT");
		SERVER_KICK_FORMAT = cfg.getString("SERVER-KICK-FORMAT");
		SERVER_TEMPBAN_FORMAT = cfg.getString("SERVER-TEMPBAN-FORMAT");
		SERVER_MUTE_FORMAT = cfg.getString("SERVER-MUTE-FORMAT");
		SERVER_IPBAN_FORMAT = cfg.getString("SERVER-IPBAN-FORMAT");
		SERVER_IPBAN_FORMAT = cfg.getString("SERVER-IPTEMPBAN-FORMAT");
		SERVER_IPBAN_FORMAT = cfg.getString("SERVER-IPMUTE-FORMAT");
		SERVER_IPBAN_FORMAT = cfg.getString("SERVER-IPKICK-FORMAT");
		DEFAULT_BAN_REASON = cfg.getString("DEFAULT-BAN-REASON");
		BAN_PERMISSION = cfg.getString("BAN-PERMISSION");
		TEMPBAN_PERMISSION = cfg.getString("TEMPBAN-PERMISSION");
		KICK_PERMISSION = cfg.getString("KICK-PERMISSION");
		MUTE_PERMISSION = cfg.getString("MUTE-PERMISSION");
		BANIP_PERMISSION = cfg.getString("BANIP-PERMISSION");
		TEMPBANIP_PERMISSION = cfg.getString("TEMPBANIP-PERMISSION");
		VIEW_PROBATION_PERMISSION = cfg.getString("VIEW-PROBATIONS-PERMISSION");
		PROBATION_PERMISSION = cfg.getString("PROBATION-PERMISSION");
		}catch(Exception e){
			MythLog.severe("Could not load MythBans... Malformed Config!");
			MythLog.severe(e.getStackTrace().toString());
		}
	}
	
}
