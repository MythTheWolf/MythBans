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

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Player;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class Mute implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());

	private DatabaseCommands dbc = new DatabaseCommands();
	private com.myththewolf.MythBans.lib.player.Player PlayerClass = new Player();

	private OfflinePlayer toMute;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /ban <user> [reason]");
				return true;
			} else if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not been on this server.");
				return true;
			} else if (!sender.hasPermission(ConfigProperties.BAN_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			}
			String stat = PlayerClass
					.getStatus(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString().toLowerCase());
			if (!stat.equals("ok") || !stat.equals("muted")) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + " Can't override status; User is not currently set to \"OK\"");
				return true;
			}
			// System.out.println(PlayerClass.getStatus(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString()));
			if (!PlayerClass.getStatus(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString())
					.equals("muted")) {
				toMute = pCache.getOfflinePlayerExact(args[0]);
				if (sender instanceof ConsoleCommandSender) {
					dbc.muteUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(), "CONSOLE");
				} else {
					org.bukkit.entity.Player pp = (org.bukkit.entity.Player) sender;
					dbc.muteUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(),
							pp.getUniqueId().toString());
				}

				for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (player.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this
								.formatMessage(toMute.getUniqueId().toString(), ConfigProperties.SERVER_MUTE_FORMAT)));
					}
				}
				if (toMute.isOnline()) {
					toMute.getPlayer().sendMessage(
							this.formatMessage(toMute.getUniqueId().toString(), ConfigProperties.USER_MUTE_FORMAT));
				}
			} else {
				toMute = pCache.getOfflinePlayerExact(args[0]);
				if (sender instanceof ConsoleCommandSender) {
					dbc.UnmuteUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(), "CONSOLE");

				} else {
					org.bukkit.entity.Player pp = (org.bukkit.entity.Player) sender;
					dbc.UnmuteUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(),
							pp.getUniqueId().toString());

				}

				for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (player.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.formatMessage(
								toMute.getUniqueId().toString(), ConfigProperties.SERVER_UNMUTE_FORMAT)));
					}
				}
				if (toMute.isOnline()) {
					toMute.getPlayer().sendMessage(
							this.formatMessage(toMute.getUniqueId().toString(), ConfigProperties.USER_UNMUTE_FORMAT));
				}
				dbc.cleanUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString());

			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	private String formatMessage(String UUID2, String format) throws SQLException {
		String toFormat = format;

		if (PlayerClass.getWhoBanned(UUID2).equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{staffMember\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(PlayerClass.getWhoBanned(UUID2))).getName());
		}

		toFormat = toFormat.replaceAll("\\{culprit\\}", Bukkit.getOfflinePlayer(UUID.fromString(UUID2)).getName());
		toFormat = toFormat.replaceAll("\\{reason\\}", PlayerClass.getReason(UUID2));

		return toFormat;
	}
}
