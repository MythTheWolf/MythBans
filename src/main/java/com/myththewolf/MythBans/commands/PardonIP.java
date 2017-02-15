package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
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

import net.md_5.bungee.api.ChatColor;

public class PardonIP implements CommandExecutor {
	private DatabaseCommands dbc = new DatabaseCommands();
	private IP ip = new IP();
	private PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private String toIP;
	private String byUUID;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!sender.hasPermission(ConfigProperties.PARDON_PERMISSION)) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "You don't have permission for this command.");
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /pardonip <IP>");
			return true;
		}
		try {
			if (args[0].charAt(0) == '/' && pc.ipExist(args[0]) == false) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "IP does not exist.");
				return true;
			} else if (args[0].charAt(0) != '/' && pc.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "User does not exist.");
				return true;
			} else {
				if (sender instanceof ConsoleCommandSender) {
					byUUID = "CONSOLE";
					if (args[0].charAt(0) == '/') {
						toIP = args[0];
					} else {
						toIP = pc.getIPbyUUID(pc.getOfflinePlayerExact(args[0]).getUniqueId().toString());
					}
				} else {
					byUUID = ((Player) sender).getUniqueId().toString();
					if (args[0].charAt(0) == '/') {
						toIP = args[0];
						dbc.pardonIP(toIP);
					} else {
						toIP = pc.getIPbyUUID(pc.getOfflinePlayerExact(args[0]).getUniqueId().toString());
						dbc.pardonIP(toIP);
					}
				}
			}
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				if (p.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
					p.sendMessage(this.formatMessage(toIP, ConfigProperties.SERVER_IPUNBAN_FORMAT, byUUID));
				}
			}
			dbc.cleanIP(toIP);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;

	}

	private String formatMessage(String IP, String format, String byUUID) throws SQLException {
		String toFormat = format;
		if (ip.getWhoBanned(IP).equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", pc.getName(byUUID));
		}

		toFormat = toFormat.replaceAll("\\{culprit\\}", IP);
		toFormat = toFormat.replaceAll("\\{reason\\}", ip.getReason(IP));

		return toFormat;
	}
}
