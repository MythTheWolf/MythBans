package com.myththewolf.MythBans.commands;

import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.MythPlayerIP;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.MythDate;

import net.md_5.bungee.api.ChatColor;

public class user implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private MythPlayer pClass;
	private MythDate mythDate;
	private MythPlayerIP ipClass = new MythPlayerIP();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /user <username>");
			return true;
		}
		try {
			if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player not found.");
				return true;
			}
			if (!sender.hasPermission(ConfigProperties.PROBATION_PERMISSION)) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "No permisson");
				return true;
			}
			pClass = DataCache.getPlayerInstance(pCache.getUUID(args[0]));
			mythDate = new com.myththewolf.MythBans.lib.tool.MythDate();
			String UUID = pClass.getId();
			String status = pClass.getStatus();
			String join = mythDate.formatDate(pClass.getJoinDate());
			OfflinePlayer p = pCache.getOfflinePlayerExact(args[0]);

			long init;
			String PD = "ERROR";
			if (!p.isOnline()) {
				init = pClass.getPlayTime();
				PD = mythDate.convertToPd(init);
			} else {
				init = p.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK);
				PD = mythDate.convertToPd((init / 20) * 1000);
			}
			String bannedBy = pClass.getWhoBanned();
			String reason = pClass.getReason();
			java.util.Date expire = pClass.getExpireDate();
			sender.sendMessage(ChatColor.YELLOW + "------------------------");
			sender.sendMessage(ChatColor.GOLD + "Status: " + ChatColor.RED + status);
			sender.sendMessage(ChatColor.GOLD + "Join date: " + ChatColor.RED + join);
			sender.sendMessage(ChatColor.GOLD + "Total play time: " + ChatColor.RED + PD);
			if ((bannedBy != null) && !bannedBy.equals("")) {
				sender.sendMessage(ChatColor.GOLD + "Action applied by: " + ChatColor.RED + pCache.getName(bannedBy));
			}
			if (reason != null && !reason.equals("")) {
				sender.sendMessage(ChatColor.GOLD + "Reason: " + ChatColor.RED + reason);
			}
			if (!(expire == null)) {
				sender.sendMessage(ChatColor.GOLD + "Ban expires on: " + ChatColor.RED + mythDate.formatDate(expire));
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
