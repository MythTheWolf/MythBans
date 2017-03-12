package com.myththewolf.MythBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Player;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class softmute implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private Player pClass = new Player();
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
				if(pClass.getStatus(pCache.getUUID(args[0])).equals("softmuted")){
					pClass.setStatus(pCache.getUUID(args[0]),"OK");
					sender.sendMessage(ConfigProperties.PREFIX + "Softmuted player.");
				}else{
					pClass.setStatus(pCache.getUUID(args[0]),"softmuted");
					sender.sendMessage(ConfigProperties.PREFIX + "Unsoftmuted player.");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}

}
