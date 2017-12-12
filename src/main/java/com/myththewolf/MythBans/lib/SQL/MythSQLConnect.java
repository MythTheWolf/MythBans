package com.myththewolf.MythBans.lib.SQL;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class MythSQLConnect {
	private static Connection con;
	private PreparedStatement ps;
	private static boolean Error = false;

	public static Connection getConnection() {
		String HOST = ConfigProperties.SQL_HOST;
		String PORT = ConfigProperties.SQL_PORT;
		String USER = ConfigProperties.SQL_USERNAME;
		String PASS = ConfigProperties.SQL_PASSWORD;
		String DATABASE = ConfigProperties.SQL_DATABASE;

		if (!isConnected()) {
			try {
				String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
				con = DriverManager.getConnection(url, USER, PASS);
				return con;
			} catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage("SERVERE: MySQL Connection FAILED!");
				e.printStackTrace();
				Error = true;
				return null;
			}
		}
		return con;
	}

	public static boolean isConnected() {
		try {
			return con != null && !con.isClosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Error = true;
		}
		return false;
	}

	public void makeTables() {
		try {
			if (ConfigProperties.DEBUG.booleanValue()) {
				Bukkit.getLogger().info("Loading MySQL Table: Logs");
			}
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_Log` ( `ID` INT NOT NULL AUTO_INCREMENT, `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `action` VARCHAR(255) NULL DEFAULT NULL , `message` VARCHAR(255) NULL DEFAULT NULL , PRIMARY KEY (`ID`)) ENGINE = InnoDB;");

			this.ps.executeUpdate();

			if (ConfigProperties.DEBUG.booleanValue()) {
				Bukkit.getLogger().info("Loading MySQL Table: History");
			}
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_History` ( `ID` INT NOT NULL AUTO_INCREMENT, `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `UUID` VARCHAR(255) NOT NULL , `action` VARCHAR(255) NULL , `reason` VARCHAR(255) NULL DEFAULT NULL , `expires` VARCHAR(255) NULL DEFAULT NULL , `byUUID` VARCHAR(255) NOT NULL , PRIMARY KEY (`ID`)) ENGINE = InnoDB;");

			this.ps.executeUpdate();

			if (ConfigProperties.DEBUG.booleanValue()) {
				Bukkit.getLogger().info("Loading MySQL Table: PlayerStats");
			}
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_PlayerStats` ( `ID` INT NOT NULL AUTO_INCREMENT, `UUID` VARCHAR(255) NOT NULL , `override` VARCHAR(255) NULL DEFAULT NULL, `probated` VARCHAR(255) NULL DEFAULT NULL ,`status` VARCHAR(255) NOT NULL,`lang_file` VARCHAR(255) NOT NULL DEFAULT 'en_US' , `group` VARCHAR(255) NOT NULL ,`expires` VARCHAR(255) NULL DEFAULT NULL , `reason` VARCHAR(255) NULL DEFAULT NULL , `timestamp` VARCHAR(255) NOT NULL, `byUUID` VARCHAR(255) NULL DEFAULT NULL , `last_quit_date` VARCHAR(255) NULL DEFAULT NULL, `last_name` VARCHAR(255) NOT NULL, `playtime` LONGTEXT NULL DEFAULT NULL,`notes` TEXT NULL DEFAULT NULL ,`session_start` VARCHAR(255) NULL DEFAULT NULL,`ignores` TEXT DEFAULT NULL,`activeChannel` VARCHAR(255) NOT NULL DEFAULT `GLOBAL`,PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			this.ps.executeUpdate();

			if (ConfigProperties.DEBUG.booleanValue()) {
				Bukkit.getLogger().info("Loading MySQL Table: NameCache");
			}
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_NameCache` ( `ID` INT NOT NULL AUTO_INCREMENT, `UUID` VARCHAR(255) NOT NULL , `Name` VARCHAR(255) NOT NULL, `discord_id` VARCHAR(255) NULL DEFAULT NULL, `discord_secret` VARCHAR(255) NULL DEFAULT NULL , PRIMARY KEY (`ID`)) ENGINE = InnoDB;");

			this.ps.executeUpdate();

			if (ConfigProperties.DEBUG.booleanValue()) {
				Bukkit.getLogger().info("Loading MySQL Table: IPCache");
			}
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_IPCache` ( `ID` INT NOT NULL AUTO_INCREMENT, `IP_ADDRESS` VARCHAR(255) NOT NULL , `UUID` VARCHAR(255) NOT NULL, `Status` VARCHAR(255) NULL DEFAULT NULL,  `byUUID` VARCHAR(255) NULL DEFAULT NULL, `reason` VARCHAR(255) NULL DEFAULT NULL,PRIMARY KEY (`ID`)) ENGINE = InnoDB;");

			this.ps.executeUpdate();

			if (ConfigProperties.DEBUG.booleanValue()) {
				Bukkit.getLogger().info("Loading MySQL Table: SiteUsers");
			}
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_SiteUsers` ( `ID` INT NOT NULL AUTO_INCREMENT, `UUID` VARCHAR(255) NOT NULL , `password` VARCHAR(255) NOT NULL ,`group` VARCHAR(255) NOT NULL,`Last_IP` VARCHAR(255) NULL DEFAULT NULL, PRIMARY KEY (`ID`) ) ENGINE = InnoDB;");

			this.ps.executeUpdate();

			if (ConfigProperties.DEBUG.booleanValue()) {
				Bukkit.getLogger().info("Loading MySQL Table: Groups");
			}
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_Groups` ( `ID` INT NOT NULL , `group_name` VARCHAR(255) NOT NULL , `level` INT NOT NULL , `remove_logs` INT NOT NULL DEFAULT '0' , `pardon` INT NOT NULL DEFAULT '0' ,`manage_groups` INT NOT NULL DEFAULT '0' , `download_logs` INT NOT NULL DEFAULT '0' , `ban` INT NOT NULL DEFAULT '0' , `kick` INT NOT NULL DEFAULT '0' , `probate` INT NOT NULL DEFAULT '0' , `mute` INT NOT NULL DEFAULT '0' , `delete_user` INT NOT NULL DEFAULT '0' , `halt_service` INT NOT NULL DEFAULT '0' , `advanced` INT NOT NULL DEFAULT '0' ) ENGINE = InnoDB;");

			this.ps.executeUpdate();

			if (ConfigProperties.DEBUG.booleanValue()) {
				Bukkit.getLogger().info("Loading MySQL Table: Tickets");
			}
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_Tickets` ( `ID` INT NOT NULL AUTO_INCREMENT, `SENDER_UUID` VARCHAR(255) NULL DEFAULT NULL , `priority` VARCHAR(255) NULL DEFAULT NULL ,`status` VARCHAR(255) NULL DEFAULT NULL,`message` VARCHAR(255) NULL DEFAULT NULL, `close_message` VARCHAR(255) NULL DEFAULT NULL ,`handler` VARCHAR(255) NULL DEFAULT NULL, `location` VARCHAR(255) NULL DEFAULT NULL, `user_seen` VARCHAR(255) NULL DEFAULT NULL ,PRIMARY KEY (`ID`) ) ENGINE = InnoDB;");

			this.ps.executeUpdate();

			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_GameChat` ( `ID` INT NOT NULL AUTO_INCREMENT, `text` VARCHAR(255) NULL DEFAULT NULL , `name` VARCHAR(255) NULL DEFAULT NULL , PRIMARY KEY (`ID`) ) ENGINE = InnoDB;");

			this.ps.executeUpdate();

			if (ConfigProperties.DEBUG.booleanValue()) {
				Bukkit.getLogger().info("Loading MySQL Table: AbstractData");
			}
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_AbstractData` ( `ID` INT NOT NULL AUTO_INCREMENT , `key` VARCHAR(255) NOT NULL , `value` VARCHAR(255) NOT NULL , PRIMARY KEY (`ID`)) ENGINE = InnoDB;");

			this.ps.executeUpdate();
			if (ConfigProperties.DEBUG.booleanValue()) {
				Bukkit.getLogger().info("Loading MySQL Table: Discord");
			}
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_Discord` ( `ID` INT NOT NULL AUTO_INCREMENT, `key` VARCHAR(255) NULL DEFAULT NULL , `value` VARCHAR(255) NULL DEFAULT NULL , PRIMARY KEY (`ID`) ) ENGINE = InnoDB;");

			this.ps.executeUpdate();

			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_Channels` ( `ID` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(255) NULL DEFAULT NULL , `node` VARCHAR(255) NULL DEFAULT NULL , `prefix` VARCHAR(255) NULL DEFAULT NULL, `shortcut` VARCHAR(255) NULL DEFAULT NULL,PRIMARY KEY (`ID`) ) ENGINE = InnoDB;");
			this.ps.executeUpdate();
			
			this.ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `MythBans_Emotes` `MythBans_Emotes` ( `ID` INT NOT NULL AUTO_INCREMENT , `send_message` TEXT NOT NULL , `user_template` TEXT NOT NULL , `channel_template` TEXT NOT NULL , `command` TEXT NOT NULL , `permission_node` TEXT NOT NULL , `sound` TEXT NOT NULL , PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			this.ps.executeUpdate();
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("SERVERE: Fatal MySQL Error!");
			e.printStackTrace();
			Error = true;
		}
	}

	public static boolean hasErrored() {
		return Error;
	}
}
