package com.myththewolf.MythBans.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.IP;
import com.myththewolf.MythBans.lib.player.PlayerCache;

import net.md_5.bungee.api.ChatColor;

public class getFam implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private IP ipClass = new IP();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (!sender.hasPermission(ConfigProperties.BANIP_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You don't have permission to execute this command.");
				return true;
			}
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /getfam <user|ip>");
				return true;
			}
			if (args[0].charAt(0) == '/' && pCache.ipExist(args[0]) == false) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "IP Not found.");
				return true;
			}
			if (args[0].charAt(0) != '/' && pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player Not found.");
				return true;
			}
			if (args[0].charAt(0) != '/') {
				List<String> list = new ArrayList<String>();
				for (String IP : ipClass.getIPPack(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString())) {
					String[] values = ipClass.getTheFam(IP, pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString());
					for(String v : values){
						list.add(v);
					}
				}
				String[] arr = new String[list.size()];
				arr = list.toArray(new String[list.size()]);
				sender.sendMessage(ConfigProperties.PREFIX+ChatColor.GOLD+"NOTE: This does a DEEP search, it looks through ALL of the user's IPs ");
				sender.sendMessage(ConfigProperties.PREFIX+ChatColor.GOLD+"Mutual users: " + Arrays.toString(arr));
			} else {
				String[] fam = ipClass.getTheFam(args[0], "BOOOOOOOOOOOOOOOOOOOOOOGUUUUUUUSSSSS");
				if (fam == null) {
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "No users sharing this ip.");
					return true;
				}
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.GOLD + " Users on this ip: " + Arrays.toString(fam));
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
