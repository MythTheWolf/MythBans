package com.myththewolf.MythBans.lib.SQL;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import com.mysql.jdbc.PreparedStatement;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

import java.sql.Connection;

public class MythSQLConnect {
	private static Connection con;
	private PreparedStatement ps;
	public Connection getConnection()
	{
		String HOST = ConfigProperties.SQL_HOST;
		String PORT = ConfigProperties.SQL_PORT;
		String USER = ConfigProperties.SQL_USERNAME;
		String PASS = ConfigProperties.SQL_PASSWORD;
		String DATABASE = ConfigProperties.SQL_DATABASE;
		if(!isConnected())
		{
		try{
			con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE  + "?useUnicode=true&characterEncoding=UTF-8", USER,
					PASS);
			return con;
			
		}catch(SQLException e)
		{
			Bukkit.getConsoleSender().sendMessage("SERVERE: MySQL Connection FAILE!");
			e.printStackTrace();
			return null;
		}
		}else{
			return con;
		}
	}
	
	
		// isConnected
		public static boolean isConnected()
		{
			return (con == null ? false : true);
		}

		
		public void makeTables()
		{
			try{
			//Logs
			ps = (PreparedStatement) con.prepareStatement("CREATE TABLE IF NOT EXISTS `MythBans_Log` ( `ID` INT NOT NULL , `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `action` VARCHAR(255) NULL DEFAULT NULL , `message` VARCHAR(255) NULL DEFAULT NULL ) ENGINE = InnoDB;");
			ps.executeUpdate();
			//History
			ps = (PreparedStatement) con.prepareStatement("CREATE TABLE IF NOT EXISTS `MythBans_History` ( `ID` INT NOT NULL , `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `UUID` VARCHAR(255) NOT NULL , `action` VARCHAR(255) NULL , `reason` VARCHAR(255) NULL DEFAULT NULL , `expires` VARCHAR(255) NULL DEFAULT NULL , `byUUID` VARCHAR(255) NOT NULL ) ENGINE = InnoDB;");
			ps.executeUpdate();
			//Current List
			ps = (PreparedStatement) con.prepareStatement("CREATE TABLE IF NOT EXISTS `MythBans_PlayerStats` ( `ID` INT NOT NULL , `UUID` VARCHAR(255) NOT NULL , `status` VARCHAR(255) NOT NULL , `group` VARCHAR(255) NOT NULL ,`expires` VARCHAR(255) NULL DEFAULT NULL , `reason` VARCHAR(255) NULL DEFAULT NULL , `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `byUUID` VARCHAR(255) NOT NULL ) ENGINE = InnoDB;");
			ps.executeUpdate();
			//Player Caches
			ps = (PreparedStatement) con.prepareStatement("CREATE TABLE IF NOT EXISTS `MythBans_NameCache` ( `ID` INT NOT NULL , `UUID` VARCHAR(255) NOT NULL , `Name` VARCHAR(255) NOT NULL ) ENGINE = InnoDB;");
			//Cron jobs
			ps = (PreparedStatement) con.prepareStatement("CREATE TABLE IF NOT EXISTS `MythBans_CronJobs` ( `ID` INT NOT NULL , `action` VARCHAR(255) NOT NULL , `UUID` VARCHAR(255) NOT NULL , `value1` VARCHAR(255) NULL DEFAULT NULL , `value2` VARCHAR(255) NULL DEFAULT NULL , `value3` VARCHAR(255) NULL DEFAULT NULL , `value4` VARCHAR(255) NULL DEFAULT NULL ) ENGINE = InnoDB;");
			//TODO: Make groups table
			//Site Users
			ps = (PreparedStatement) con.prepareStatement("CREATE TABLE IF NOT EXISTS `MythBans_SiteUsers` ( `ID` INT NOT NULL , `UUID` VARCHAR(255) NOT NULL , `password` VARCHAR(255) NOT NULL , `Last_IP` VARCHAR(255) NOT NULL ) ENGINE = InnoDB;");
			}catch(SQLException e){
				Bukkit.getConsoleSender().sendMessage("SERVERE: Fatal MySQL Error!");
				e.printStackTrace();
			}
		}
	
}
