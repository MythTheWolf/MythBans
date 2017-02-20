package com.myththewolf.MythBans.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Player;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Date;
import com.myththewolf.MythBans.lib.player.IP;
public class Compare implements CommandExecutor {
	private com.myththewolf.MythBans.lib.player.Player mythPlayer = new Player();
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private Date mythDate = new Date();
	private ResultSet packet1;
	private ResultSet packet2;
	private IP ipClass = new IP();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 2) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /compare <user> <user>");
				return true;
			} else if (pCache.getOfflinePlayerExact(args[0]) == null || pCache.getOfflinePlayerExact(args[1]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not been on this server.");
				return true;
			} else if (!sender.hasPermission(ConfigProperties.BAN_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			}
			
			String UUID1 = pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString();
			String UUID2 = pCache.getOfflinePlayerExact(args[1]).getUniqueId().toString();
			packet1 = mythPlayer.getHistoryPack(UUID1);
			packet2 = mythPlayer.getHistoryPack(UUID2);
			int banCount_1 = 0;
			int muteCount_1 = 0;
			int kickCount_1 = 0;
			int probateCount_1 = 0;
			int tempBanCount_1 = 0;
			while (packet1.next()) {
				switch (packet1.getString("action")) {
				case "userBan":
					banCount_1++;
					break;
				case "userKick":
					kickCount_1++;
					break;
				case "userProbate":
					probateCount_1++;
					break;
				case "userTempBan":
					tempBanCount_1++;
					break;
				default:
					break;
				}
			}
			int banCount_2 = 0;
			int muteCount_2 = 0;
			int kickCount_2 = 0;
			int probateCount_2 = 0;
			int tempBanCount_2 = 0;
			while (packet2.next()) {
				switch (packet2.getString("action")) {
				case "userBan":
					banCount_2++;
					break;
				case "userKick":
					kickCount_2++;
					break;
				case "userProbate":
					probateCount_2++;
					break;
				case "userTempBan":
					tempBanCount_2++;
					break;
				default:
					break;
				}
			}
			long pTime1 = mythPlayer.getPlayTime(UUID1);
			long pTime2 = mythPlayer.getPlayTime(UUID2);
			String diff;
			if (pTime1 > pTime2) {
				diff = args[0] + " has " + mythDate.convertToPd(pTime1 - pTime2) + " more playtime";
			} else if (pTime1 < pTime2) {
				diff = args[1] + " has " + mythDate.convertToPd(pTime2 - pTime1) + " more playtime";
			} else {
				diff = "Both players have Equal playTime";
			}
			sender.sendMessage(ChatColor.RED + "--------------------" + ChatColor.AQUA + "User Comparison"
					+ ChatColor.RED + "--------------------");
			sender.sendMessage(diff);
			sender.sendMessage(ChatColor.GOLD + args[0] + " Logged IPs: " + Arrays.toString(ipClass.getIPPack(UUID1)));
			sender.sendMessage(ChatColor.GOLD + args[1] + " Logged IPs: " + Arrays.toString(ipClass.getIPPack(UUID2)));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
