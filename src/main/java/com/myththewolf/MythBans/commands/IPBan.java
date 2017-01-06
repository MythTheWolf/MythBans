package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

public class IPBan implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private String toIP;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args[0].charAt(0) == '/') {
				if (!pCache.ipExist(args[0])) {
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "That IP Has never joined this server.");
					return true;
				}
			} else if (args[0].charAt(0) == '/' && pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not been on this server.");
				return true;
			} else if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED
						+ "Usage: /banip <user or IP> [reason] (NOTE: IP must start with slash. E.g /120.0.0.1");
				return true;
			} else if (!sender.hasPermission(ConfigProperties.BANIP_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			} else {

				toIP = args[0];
				if (sender instanceof ConsoleCommandSender) {
					String reason = Utils.makeString(args, 1);
					
				} else {
					String reason = Utils.makeString(args, 1);

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
