package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import java.util.UUID;

import org.bukkit.Bukkit;
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
	private final com.myththewolf.MythBans.lib.player.Player PlayerClass = new com.myththewolf.MythBans.lib.player.Player();
	private Player toKick;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (pc.getPlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not played on this server.");
				return true;
			}else{
				toKick = pc.getPlayerExact(args[0]);
			}
			if (sender instanceof ConsoleCommandSender) {
				dbc.kickUser(toKick.getUniqueId().toString(), "CONSOLE", Utils.makeString(args, 1));
				pc.getPlayerExact(args[0]).kickPlayer(this.formatMessage(toKick.getUniqueId().toString()));
				return true;
			}else if(args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /kick <user> [reason]");
				return true;
			}else if(!sender.hasPermission(ConfigProperties.KICK_PERMISSION)){
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			}else{
				Player p = (Player) sender;
				dbc.kickUser(toKick.getUniqueId().toString(), p.getUniqueId().toString(), Utils.makeString(args, 1));
				pc.getPlayerExact(args[0]).kickPlayer(this.formatMessage(toKick.getUniqueId().toString()));
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		
	}
	private String formatMessage(String UUID2) throws SQLException
	{
		String toFormat = ConfigProperties.USER_KICK_FORMAT;
		if(PlayerClass.getWhoBanned(UUID2).equals("CONSOLE"))
		{
			toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
		}else{
			toFormat = toFormat.replaceAll("\\{staffMember\\}", Bukkit.getOfflinePlayer(UUID.fromString(PlayerClass.getWhoBanned(UUID2))).getName());
		}
		
		toFormat = toFormat.replaceAll("\\{culprit\\}", Bukkit.getOfflinePlayer(UUID.fromString(UUID2)).getName());
		toFormat = toFormat.replaceAll("\\{reason\\}", PlayerClass.getReason(UUID2));

		return toFormat;
	}
}
