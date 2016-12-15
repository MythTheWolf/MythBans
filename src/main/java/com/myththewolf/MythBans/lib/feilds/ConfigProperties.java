package com.myththewolf.MythBans.lib.feilds;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
public class ConfigProperties {
	public static String SQL_HOST;
	public static String SQL_PORT;
	public static String SQL_DATABASE;
	public static String SQL_USERNAME;
	public static String SQL_PASSWORD;
	public static int CRON_INTERVAL;
	public static String PREFIX;
	public static String VIEWMSG_PERM;
	public static String DEFAULT_BAN_MSG;
	public static Boolean DEBUG;
	public static String API_URL;
	public static String API_KEY;
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
		SQL_USERNAME = cfg.getString("SQL-USER:");
		SQL_PASSWORD = cfg.getString("SQL-PASSWORD");
		CRON_INTERVAL = cfg.getInt("CRON-UDPATE-TIMEL");
		PREFIX = cfg.getString("PREFIX");
		VIEWMSG_PERM = cfg.getString("BANMESSAGE-PERMISSION");
		DEFAULT_BAN_MSG = cfg.getString("DEFAULT-BAN");
		DEBUG = cfg.getBoolean("DEBUG-MODE");
		API_URL = cfg.getString("API-STARTPOINT");
		API_KEY = cfg.getString("API_KEY");
		}catch(Exception e){
			MythLog.severe("Could not load MythBans... Malformed Config!");
			MythLog.severe(e.getStackTrace().toString());
		}
	}
	
}
