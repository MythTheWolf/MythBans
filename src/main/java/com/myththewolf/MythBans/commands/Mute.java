package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class Mute implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "player has not been on this server.");
				return true;
			} else {
				if (sender instanceof ConsoleCommandSender) {
					dbc.muteUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(), "CONSOLE");
				}else if(args.length < 1) {
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /mute <user>");
					return true;
				}else if(!sender.hasPermission(ConfigProperties.BAN_PERMISSION)){
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
					return true;
				}else{
					org.bukkit.entity.Player p = (org.bukkit.entity.Player) sender;
					dbc.muteUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(),
							p.getUniqueId().toString());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
