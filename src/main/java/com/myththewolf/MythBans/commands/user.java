package com.myththewolf.MythBans.commands;

import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Player;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Date;
import com.myththewolf.MythBans.lib.player.IP;
import net.md_5.bungee.api.ChatColor;

public class user implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private Player pClass = new Player();
	private Date MythDate = new Date();
	private IP ipClass = new IP();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /user <username>");
			return true;
		}
		try {
			if (pCache.getPlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player not found.");
				return true;
			}
			String UUID = pCache.getPlayerExact(args[0]).getUniqueId().toString();
			String status = pClass.getStatus(UUID);
			String join = MythDate.formatDate(pClass.getJoinDate(UUID));
			String playTime = MythDate.convertToPd(pClass.getPlayTime(UUID));
			String bannedBy = pClass.getWhoBanned(UUID);
			String reason = pClass.getReason(UUID);
			java.util.Date expire = pClass.getExpireDate(UUID);
			sender.sendMessage(ChatColor.YELLOW + "------------------------");
			sender.sendMessage(ChatColor.GOLD + "Status: " + ChatColor.RED + status);
			sender.sendMessage(ChatColor.GOLD + "Join date: " + ChatColor.RED + join);
			sender.sendMessage(ChatColor.GOLD + "Total play time: " + ChatColor.RED + playTime);
			if (!bannedBy.equals("") && (bannedBy != null)) {
				sender.sendMessage(ChatColor.GOLD + "Action applied by: " + ChatColor.RED + pCache.getName(bannedBy));
			}
			if (!reason.equals("") && reason != null) {
				sender.sendMessage(ChatColor.GOLD + "Reason: " + ChatColor.RED + reason);
			}
			if (!(expire == null) && expire != null) {
				sender.sendMessage(ChatColor.GOLD + "Ban expires on: " + ChatColor.RED + MythDate.formatDate(expire));
			}
			sender.sendMessage(
					ChatColor.GOLD + "Logged IPs: " + ChatColor.RED + Arrays.toString(ipClass.getIPPack(UUID)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
