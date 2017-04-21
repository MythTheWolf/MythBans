package com.myththewolf.MythBans.commands;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class PardonUser implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private String toUUID = "";
	private MythPlayer PlayerClass;
	private String byUUID;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /ban <user> [reason]");
				return true;
			} else if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not been on this server.");
				return true;
			} else if (!sender.hasPermission(ConfigProperties.PARDON_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			} else {
				toUUID = pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString();
				PlayerClass = new MythPlayer(toUUID);
				if (sender instanceof ConsoleCommandSender) {
					dbc.pardonUser(toUUID, "CONSOLE");
					byUUID = "CONSOLE";
				} else {
					dbc.pardonUser(toUUID, ((Player) sender).getUniqueId().toString());
					byUUID = ((Player) sender).getUniqueId().toString();
				}
				for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (player.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&',
								this.formatMessage(toUUID, ConfigProperties.SERVER_PARDON_FORMAT)));
					}
				}
				dbc.cleanUser(toUUID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	private String formatMessage(String UUID2, String format) throws SQLException {
		String toFormat = format;
		if (PlayerClass.getWhoBanned().equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", pCache.getName(byUUID));
		}

		toFormat = toFormat.replaceAll("\\{culprit\\}", Bukkit.getOfflinePlayer(UUID.fromString(UUID2)).getName());
		toFormat = toFormat.replaceAll("\\{reason\\}", PlayerClass.getReason());

		return toFormat;
	}
}
