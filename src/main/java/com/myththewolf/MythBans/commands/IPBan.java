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
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

public class IPBan implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private String toIP;
	private com.myththewolf.MythBans.lib.player.Player PlayerClass = new com.myththewolf.MythBans.lib.player.Player();
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args[0].charAt(0) == '/') {
				if (!pCache.ipExist(args[0])) {
					sender.sendMessage(
							ConfigProperties.PREFIX + ChatColor.RED + "That IP Has never joined this server.");
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
					dbc.banIP(toIP, "CONSOLE", reason);
				} else {
					String reason = Utils.makeString(args, 1);
					Player p = (Player) sender;
					dbc.banIP(toIP, p.getUniqueId().toString(), reason);
				}
				for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
					if(player.getAddress().getAddress().toString().equals(toIP))
					{
						player.kickPlayer(this.formatMessage(toIP, ConfigProperties.USER_IPBAN_FORMAT));
					}else
					if (player.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						player.sendMessage(this.formatMessage(toIP, ConfigProperties.SERVER_IPBAN_FORMAT));
					}else{
						continue;
					}
				}
			}
		} catch (SQLException e) {
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
