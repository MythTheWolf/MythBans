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
	private OfflinePlayer p;
	private com.myththewolf.MythBans.lib.player.Player PlayerClass = new Player();
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "player has not been on this server.");
				return true;
			} else {
				if (sender instanceof ConsoleCommandSender) {
					dbc.muteUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(), "CONSOLE");
				} else if (args.length < 1) {
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /mute <user>");
					return true;
				} else if (!sender.hasPermission(ConfigProperties.BAN_PERMISSION)) {
					sender.sendMessage(
							ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
					return true;
				} else {
					if (sender instanceof ConsoleCommandSender) {
						dbc.muteUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(), "CONSOLE");
						p = pCache.getOfflinePlayerExact(args[0]);
					} else {
						org.bukkit.entity.Player pp = (org.bukkit.entity.Player) sender;
						dbc.muteUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(),
								pp.getUniqueId().toString());
						p = pCache.getOfflinePlayerExact(args[0]);
					}
					
				}
				for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (player.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						player.sendMessage(this.formatMessage(p.getUniqueId().toString(), ConfigProperties.SERVER_MUTE_FORMAT));
					}
				}
				if (p.isOnline()) {
					p.getPlayer().kickPlayer(this.formatMessage(p.getUniqueId().toString(), ConfigProperties.USER_MUTE_FORMAT));
				}
			}
		} catch (SQLException e) {
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
		toFormat = toFormat.replaceAll("\\{expire\\}", PlayerClass.getExpireDate(UUID2).toString());
		return toFormat;
	}

}
