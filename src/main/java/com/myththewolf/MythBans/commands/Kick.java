package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

import net.md_5.bungee.api.ChatColor;

public class Kick implements CommandExecutor {
	private final PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private final DatabaseCommands dbc = new DatabaseCommands();
	private OfflinePlayer toKick;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (pc.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not played on this server.");
				return true;
			}else{
				toKick = pc.getOfflinePlayerExact(args[0]);
			}
			if (sender instanceof ConsoleCommandSender) {
				dbc.kickUser(toKick.getUniqueId().toString(), "CONSOLE", Utils.makeString(args, 1));
				return true;
			}else{
				Player p = (Player) sender;
				dbc.kickUser(toKick.getUniqueId().toString(), p.getUniqueId().toString(), Utils.makeString(args, 1));
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		
	}

}
