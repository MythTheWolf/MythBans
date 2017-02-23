package com.myththewolf.MythBans.commands.ticket;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.PlayerTicket;
import com.myththewolf.MythBans.lib.tool.Date;
import com.myththewolf.MythBans.lib.tool.Utils;

import net.md_5.bungee.api.ChatColor;

public class ReportGrief implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private Date mythDate = new Date();
	private PlayerTicket PT = new PlayerTicket();
	@Override	
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		String UUID = "";
		try {
			if(sender instanceof ConsoleCommandSender)
			{
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Must be a player!");
				return true;
			}else{
				UUID =((Player) sender).getUniqueId().toString();
			}
			if (args.length < 2) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /reportgreif <user> <message>");
				return true;
			}
			if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player not found");
				return true;
			}
			String seralizedLocation = Utils.serializeLocation(((Player) sender).getLocation());
			String timestamp = mythDate.formatDate(mythDate.getNewDate());
			String text = Utils.makeString(args, 1);
			PT.recordGreif(UUID, timestamp,  seralizedLocation, text);
			sender.sendMessage(ConfigProperties.PREFIX + "Reported Greif!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
