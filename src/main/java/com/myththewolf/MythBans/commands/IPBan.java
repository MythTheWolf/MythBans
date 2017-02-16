package com.myththewolf.MythBans.commands;

import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.IP;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

public class IPBan implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private String toIP;
	private IP ipClass = new IP();
	private String[] packet;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (!sender.hasPermission(ConfigProperties.BANIP_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You don't have permission to execute this command.");
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /banip <user|ip> [reason]");
				return true;
			}
			if (args[0].charAt(0) == '/' && pCache.ipExist(args[0]) == false) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "IP Not found.");
				return true;
			}
			if (args[0].charAt(0) != '/' && pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player Not found.");
				return true;
			}
			if (args[0].charAt(0) != '/') {
				packet = pCache.getIPbyUUID(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString());
				System.out.print(Arrays.toString(packet));
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
