package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Player;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

public class Probate implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private OfflinePlayer p;
	private DatabaseCommands dbc = new DatabaseCommands();
	private Player playerClass = new Player();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /probate <user> [reason]");
				return true;
			} else if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not been on this server.");
				return true;
			} else if (!sender.hasPermission(ConfigProperties.PROBATION_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			} else {
				String stat = playerClass
						.getStatus(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString());
				if (!stat.equals("OK") && !stat.equals("trial")) {
					sender.sendMessage(stat);
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED
							+ "Can't override status; User is not currently set to \"OK\"");
					return true;
					
				}else{
					
				}
				p = pCache.getOfflinePlayerExact(args[0]);
				if (playerClass.getStatus(p.getUniqueId().toString()).equals("trial")) {
					if (sender instanceof ConsoleCommandSender) {
						dbc.unProbate(p.getUniqueId().toString(), "CONSOLE");
						sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "Unprobated " + p.getName());
						return true;
					} else {
						dbc.unProbate(p.getUniqueId().toString(),
								((org.bukkit.entity.Player) sender).getUniqueId().toString());
						sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "Unprobated " + p.getName());
						return true;
					}
				} else {
					if (sender instanceof ConsoleCommandSender) {
						dbc.setProbation(p.getUniqueId().toString(), "CONSOLE", Utils.makeString(args, 2));
						sender.sendMessage(
								ConfigProperties.PREFIX + ChatColor.GOLD + "Set " + p.getName() + " on probation.");
						return true;
					} else {
						org.bukkit.entity.Player pp = (org.bukkit.entity.Player) sender;
						dbc.setProbation(p.getUniqueId().toString(), pp.getUniqueId().toString(),
								Utils.makeString(args, 2));
						sender.sendMessage(
								ConfigProperties.PREFIX + ChatColor.GOLD + "Set " + p.getName() + " on probation.");
						return true;
					}

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
