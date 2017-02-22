package com.myththewolf.MythBans.commands.ticket;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

import net.md_5.bungee.api.ChatColor;

public class ReportGrief implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 2) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /reportgreif <user> <message>");
				return true;
			}
			if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player not found");
				return true;
			}
			String seralizedLocation = Utils.serializeLocation(((Player) sender).getLocation());
			sender.sendMessage(seralizedLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
