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
import com.myththewolf.MythBans.lib.tool.Utils;

public class Ban implements CommandExecutor{
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands(); 
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
	{
		try
		{
			if (pCache.getOfflinePlayerExact(args[0]) == null)
			{
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not been on this server.");
				return true;
			} else
			{
				if (sender instanceof ConsoleCommandSender)
				{
					String reason = Utils.makeString(args, 1);
					dbc.banUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(),
							ConfigProperties.CONSOLE_UUID,reason);
				} else
				{
					String reason = Utils.makeString(args, 1);
					org.bukkit.entity.Player p = (org.bukkit.entity.Player) sender;
					dbc.banUser(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString(),
							p.getUniqueId().toString(),reason);
				}
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
}
