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
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

public class Ban implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private OfflinePlayer p;
	private String toUUID;
	private com.myththewolf.MythBans.lib.player.Player PlayerClass = new com.myththewolf.MythBans.lib.player.Player();

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
			} else {
				p = pCache.getOfflinePlayerExact(args[0]);
				if (sender instanceof ConsoleCommandSender) {
					String reason = Utils.makeString(args, 1);
					dbc.banUser(p.getUniqueId().toString(), "CONSOLE", reason);

					toUUID = p.getUniqueId().toString();
				} else {
					String reason = Utils.makeString(args, 1);
					org.bukkit.entity.Player p = (org.bukkit.entity.Player) sender;
					dbc.banUser(p.getUniqueId().toString(), p.getUniqueId().toString(), reason);

					toUUID = p.getUniqueId().toString();
				}
			}
			for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (player.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('7', this.formatMessage(toUUID, ConfigProperties.SERVER_BAN_FORMAT)));
				}
			}
			if (p.isOnline()) {
				p.getPlayer()
						.kickPlayer(this.formatMessage(p.getUniqueId().toString(), ConfigProperties.USER_BAN_FORMAT));
			}
		} catch (

		SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
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
