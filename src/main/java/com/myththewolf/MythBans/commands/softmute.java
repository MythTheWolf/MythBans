package com.myththewolf.MythBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerDataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class softmute implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private MythPlayer pClass;

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
				pClass = PlayerDataCache.getInstance(pCache.getUUID(args[0]));
				if (pClass.getStatus().equals("softmuted")) {
					pClass.setStatus("OK");
					MythPlayer.setOverride(pClass.getId(),false);
					sender.sendMessage(ConfigProperties.PREFIX + "Unsoftmuted player.");
				} else {
					pClass.setStatus("softmuted");
					sender.sendMessage(ConfigProperties.PREFIX + "Softmuted player.");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}

}
