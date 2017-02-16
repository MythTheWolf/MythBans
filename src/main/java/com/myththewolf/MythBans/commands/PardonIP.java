package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.IP;
import com.myththewolf.MythBans.lib.player.PlayerCache;

import net.md_5.bungee.api.ChatColor;

public class PardonIP implements CommandExecutor {
	private DatabaseCommands dbc = new DatabaseCommands();
	private IP ip = new IP();
	private PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private String toIP;
	private String byUUID;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		return true;
	}
}
