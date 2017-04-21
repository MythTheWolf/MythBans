package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Date;

import net.md_5.bungee.api.ChatColor;

public class PlayerTime implements CommandExecutor {
	private MythPlayer mythPlayer;
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private Date mythDate = new Date();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /playtime <user>");
				return true;
			}
			if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player not found");
				return true;
			}
			OfflinePlayer p = pCache.getOfflinePlayerExact(args[0]);
			String time;
			long dump;
			mythPlayer = new MythPlayer(p.getUniqueId().toString());
			if (p.isOnline()) {
				dump = mythDate.getTimeDifference(mythPlayer.getSessionJoinDate(p.getUniqueId().toString()),
						mythDate.getNewDate()) + mythPlayer.getPlayTime();
				time = mythDate.convertToPd(dump);
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "User has play time of " + time);
				return true;
			} else {
				time = mythDate.convertToPd(mythPlayer.getPlayTime());
				java.util.Date off = mythPlayer.getQuitDate();
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "User has play time of " + time
						+ "(First joined on " + mythDate.formatDate(mythPlayer.getJoinDate()) + ")");
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "User has been offline for "
						+ mythDate.convertToPd(mythDate.getTimeDifference(off, mythDate.getNewDate())) + "(Last seen: "
						+ mythDate.formatDate(mythPlayer.getQuitDate()) + ")");
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
