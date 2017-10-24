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
import com.myththewolf.MythBans.lib.feilds.DataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.PlayerLanguage;

public class Mute implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());

	private DatabaseCommands dbc = new DatabaseCommands();
	private MythPlayer PlayerClass;
	private PlayerLanguage PL;
	private OfflinePlayer toMute;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			PL = new PlayerLanguage(sender);
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /ban <user> [reason]");
				return true;
			} else if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NULL_PLAYER"));
				return true;
			} else if (!sender.hasPermission(ConfigProperties.BAN_PERMISSION)) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NO_PERMISSION"));
				return true;
			}
			PlayerClass = DataCache.getPlayerInstance(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString());
			String stat = PlayerClass.getStatus();
			if (!stat.equals("OK") && !stat.equals("muted")) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + PL.languageList.get("ERR_OVERRIDE_CONFLICT"));
				return true;
			}
			// System.out.println(PlayerClass.getStatus(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString()));
			if (!PlayerClass.getStatus().equals("muted")) {
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
						PL = new PlayerLanguage(player);
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.formatMessage(
								toMute.getUniqueId().toString(), PL.languageList.get("PUNISHMENT_MUTE_INFORM"))));
					}
				}
				if (toMute.isOnline()) {
					PL = new PlayerLanguage(toMute);
					toMute.getPlayer().sendMessage(this.formatMessage(toMute.getUniqueId().toString(),
							PL.languageList.get("PUNISHMENT_MUTE_PLAYER")));
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
						PL = new PlayerLanguage(player);
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.formatMessage(
								toMute.getUniqueId().toString(), PL.languageList.get("PUNISHMENT_UNMUTE_INFORM"))));
					}
				}
				if (toMute.isOnline()) {
					PL = new PlayerLanguage(toMute);
					toMute.getPlayer().sendMessage(this.formatMessage(toMute.getUniqueId().toString(),
							PL.languageList.get("PUNISHMENT_UNMUTE_PLAYER")));
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

		if (PlayerClass.getWhoBanned().equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{0\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{0\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(PlayerClass.getWhoBanned())).getName());
		}

		toFormat = toFormat.replaceAll("\\{1\\}", Bukkit.getOfflinePlayer(UUID.fromString(UUID2)).getName());

		return toFormat;
	}
}
