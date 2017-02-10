package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import java.util.UUID;

import org.bukkit.Bukkit;
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

public class Mute implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());

	private DatabaseCommands dbc = new DatabaseCommands();
	private com.myththewolf.MythBans.lib.player.Player PlayerClass = new Player();
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		return true;
	}

}
