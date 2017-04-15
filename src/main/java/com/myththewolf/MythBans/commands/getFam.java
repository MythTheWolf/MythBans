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
import com.myththewolf.MythBans.lib.player.PlayerLanguage;

public class getFam implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private IP ipClass = new IP();
	private PlayerLanguage PL;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			PL = new PlayerLanguage(sender);
			if (!sender.hasPermission(ConfigProperties.BANIP_PERMISSION)) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NO_PERMISSION"));
				return true;
			}
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("COMMAND_GETFAM_USAGE"));
				return true;
			}
			if (args[0].charAt(0) == '/' && pCache.ipExist(args[0]) == false) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NULL_IP"));
				return true;
			}
			if (args[0].charAt(0) != '/' && pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NULL_PLAYER"));
				return true;
			}
			if (args[0].charAt(0) != '/') {
				List<String> commonUsers = new ArrayList<String>();
				String theID = pCache.getUUID(args[0]);
				String[] IPs = ipClass.getIPPack(theID);
				for (String IP : IPs) {
					System.out.println(IP);
					for (String singleUser : ipClass.getTheFam(IP, theID)) {
						if (!commonUsers.contains(singleUser)) {
							commonUsers.add(singleUser);
						}
					}
				}
				String[] arr = new String[commonUsers.size()];
				arr = commonUsers.toArray(new String[commonUsers.size()]);
				sender.sendMessage(ConfigProperties.PREFIX
						+ PL.languageList.get("MUTUAL_USERS").replaceAll("\\{0\\}", Arrays.toString(arr)));
				return true;
			} else {
				String[] fam = ipClass.getTheFam(args[0], "BOOOOOOOOOOOOOOOOOOOOOOGUUUUUUUSSSSS");
				if (fam == null) {
					sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NOMUTUALUSERS"));
					return true;
				}

				sender.sendMessage(ConfigProperties.PREFIX
						+ PL.languageList.get("MUTUAL_USERS").replaceAll("\\{0\\}", Arrays.toString(fam)));
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
