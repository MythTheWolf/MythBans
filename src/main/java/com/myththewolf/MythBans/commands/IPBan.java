package com.myththewolf.MythBans.commands;

import java.sql.SQLException;
import java.util.UUID;

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
import com.myththewolf.MythBans.lib.events.player.IP;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

public class IPBan implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private String toIP;
	private IP ipClass = new IP();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /banip <user or IP> [reason]");
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED
						+ "Note: If typing actual IP rather than player name, use a / then ip. e.g: /127.0.0.1");
				return true;
			} else if (args[0].charAt(0) == '/' && !pCache.ipExist(args[0])) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "IP has never joined this server");
				return true;
			} else if (args[0].charAt(0) != '/' && pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not been on this server.");
				return true;

			} else if (!sender.hasPermission(ConfigProperties.BANIP_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			} else if (args[0].charAt(0) == '/' && pCache.ipExist(args[0])) {
				if (sender instanceof ConsoleCommandSender) {
					dbc.banIP(args[0], "CONSOLE", Utils.makeString(args, 1));
					toIP = args[0];
				} else {
					Player s = (Player) sender;
					dbc.banIP(args[0], s.getUniqueId().toString(), Utils.makeString(args, 1));
					toIP = args[0];
				}
			} else if (args[0].charAt(0) != '/' && pCache.getOfflinePlayerExact(args[0]) != null) {
				OfflinePlayer p = pCache.getOfflinePlayerExact(args[0]);
				String IP = pCache.getIPbyUUID(p.getUniqueId().toString());
				if (sender instanceof ConsoleCommandSender) {
					dbc.banIP(IP, "CONSOLE", Utils.makeString(args, 1));
					toIP = IP;
				} else {
					Player s = (Player) sender;
					dbc.banIP(IP, s.getUniqueId().toString(), Utils.makeString(args, 1));
					toIP = IP;
				}

			}
			for (Player i : Bukkit.getOnlinePlayers()) {
				if (i.getAddress().getAddress().toString().equals(toIP)) {
					i.kickPlayer(this.formatMessage(i.getAddress().getAddress().toString(),
							ConfigProperties.USER_IPBAN_FORMAT));
				} else if (i.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
					i.sendMessage(ChatColor.translateAlternateColorCodes('&', this.formatMessage(
							i.getAddress().getAddress().toString(), ConfigProperties.SERVER_IPBAN_FORMAT)));
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	private String formatMessage(String UUID2, String format) throws SQLException {
		String toFormat = format;
		toFormat = toFormat.replaceAll("\\{culprit\\}", UUID2);

		if (ipClass.getWhoBanned(UUID2).equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{staffMember\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(ipClass.getWhoBanned(UUID2))).getName());
		}
		toFormat = toFormat.replaceAll("\\{reason\\}", ipClass.getReason(UUID2));
		return toFormat;
	}

}
