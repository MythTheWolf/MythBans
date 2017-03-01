package com.myththewolf.MythBans.lib.SQL;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

import java.sql.Connection;

public class MythSQLConnect {
	private static Connection con;
	private PreparedStatement ps;

	public static Connection getConnection() {
		String HOST = ConfigProperties.SQL_HOST;
		String PORT = ConfigProperties.SQL_PORT;
		String USER = ConfigProperties.SQL_USERNAME;
		String PASS = ConfigProperties.SQL_PASSWORD;
		String DATABASE = ConfigProperties.SQL_DATABASE;
		if (!isConnected()) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
						+ "?useUnicode=true&characterEncoding=UTF-8", USER, PASS);
				return con;

			} catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage("SERVERE: MySQL Connection FAILED!");
				e.printStackTrace();
				return null;
			}
		} else {
			return con;
		}
	}

	// isConnected
	public static boolean isConnected() {
		return (con == null ? false : true);
	}

	public void makeTables() {
		try {
			// Logs
			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("Loading MySQL Table: Logs");
			}
			ps = (PreparedStatement) con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_Log` ( `ID` INT NOT NULL AUTO_INCREMENT, `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `action` VARCHAR(255) NULL DEFAULT NULL , `message` VARCHAR(255) NULL DEFAULT NULL , PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			ps.executeUpdate();
			// History
			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("Loading MySQL Table: History");
			}
			ps = (PreparedStatement) con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_History` ( `ID` INT NOT NULL AUTO_INCREMENT, `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `UUID` VARCHAR(255) NOT NULL , `action` VARCHAR(255) NULL , `reason` VARCHAR(255) NULL DEFAULT NULL , `expires` VARCHAR(255) NULL DEFAULT NULL , `byUUID` VARCHAR(255) NOT NULL , PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			ps.executeUpdate();
			// Current List
			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("Loading MySQL Table: PlayerStats");
			}
			ps = (PreparedStatement) con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_PlayerStats` ( `ID` INT NOT NULL AUTO_INCREMENT, `UUID` VARCHAR(255) NOT NULL , `status` VARCHAR(255) NOT NULL , `group` VARCHAR(255) NOT NULL ,`expires` VARCHAR(255) NULL DEFAULT NULL , `reason` VARCHAR(255) NULL DEFAULT NULL , `timestamp` VARCHAR(255) NOT NULL, `byUUID` VARCHAR(255) NULL DEFAULT NULL , `last_quit_date` VARCHAR(255) NULL DEFAULT NULL, `last_name` VARCHAR(255) NOT NULL, `playtime` LONGTEXT NULL DEFAULT NULL, `session_start` VARCHAR(255) NULL DEFAULT NULL,PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			ps.executeUpdate();
			// Player Caches
			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("Loading MySQL Table: NameCache");
			}
			ps = (PreparedStatement) con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_NameCache` ( `ID` INT NOT NULL AUTO_INCREMENT, `UUID` VARCHAR(255) NOT NULL , `Name` VARCHAR(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			ps.executeUpdate();
			// IP Table
			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("Loading MySQL Table: IPCache");
			}
			ps = (PreparedStatement) con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_IPCache` ( `ID` INT NOT NULL AUTO_INCREMENT, `IP_ADDRESS` VARCHAR(255) NOT NULL , `UUID` VARCHAR(255) NOT NULL, `Status` VARCHAR(255) NULL DEFAULT NULL,  `byUUID` VARCHAR(255) NULL DEFAULT NULL, `reason` VARCHAR(255) NULL DEFAULT NULL,PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			ps.executeUpdate();
			// Site Users
			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("Loading MySQL Table: SiteUsers");
			}
			ps = (PreparedStatement) con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_SiteUsers` ( `ID` INT NOT NULL AUTO_INCREMENT, `UUID` VARCHAR(255) NOT NULL , `password` VARCHAR(255) NOT NULL ,`group` VARCHAR(255) NOT NULL,`Last_IP` VARCHAR(255) NULL DEFAULT NULL, PRIMARY KEY (`ID`) ) ENGINE = InnoDB;");
			ps.executeUpdate();

			/// Groups
			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("Loading MySQL Table: Groups");
			}
			ps = (PreparedStatement) con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_Groups` ( `ID` INT NOT NULL , `group_name` VARCHAR(255) NOT NULL , `level` INT NOT NULL , `remove_logs` INT NOT NULL DEFAULT '0' , `pardon` INT NOT NULL DEFAULT '0' ,`manage_groups` INT NOT NULL DEFAULT '0' , `download_logs` INT NOT NULL DEFAULT '0' , `ban` INT NOT NULL DEFAULT '0' , `kick` INT NOT NULL DEFAULT '0' , `probate` INT NOT NULL DEFAULT '0' , `mute` INT NOT NULL DEFAULT '0' , `delete_user` INT NOT NULL DEFAULT '0' , `halt_service` INT NOT NULL DEFAULT '0' , `advanced` INT NOT NULL DEFAULT '0' ) ENGINE = InnoDB;");
			ps.executeUpdate();
			// Tickets & Reports
			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("Loading MySQL Table: Tickets");
			}
			ps = (PreparedStatement) con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_Tickets` ( `ID` INT NOT NULL AUTO_INCREMENT, `SENDER_UUID` VARCHAR(255) NULL DEFAULT NULL , `priority` VARCHAR(255) NULL DEFAULT NULL ,`status` VARCHAR(255) NULL DEFAULT NULL,`message` VARCHAR(255) NULL DEFAULT NULL, `close_message` VARCHAR(255) NULL DEFAULT NULL ,`handler` VARCHAR(255) NULL DEFAULT NULL, `location` VARCHAR(255) NULL DEFAULT NULL, `user_seen` VARCHAR(255) NULL DEFAULT NULL ,PRIMARY KEY (`ID`) ) ENGINE = InnoDB;");
			ps.executeUpdate();
			if (ConfigProperties.DEBUG) {
				Bukkit.getLogger().info("All MySQL tables generated.");
			}
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("SERVERE: Fatal MySQL Error!");
			e.printStackTrace();
		}
	}

}
