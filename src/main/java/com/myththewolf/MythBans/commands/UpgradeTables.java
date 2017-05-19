package com.myththewolf.MythBans.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.MythBans;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.tasks.UpgradeTablesTask;

public class UpgradeTables implements CommandExecutor{
	public PreparedStatement ps;
	public Connection con;
	public JavaPlugin myth;
	private MythBans srv;
	public UpgradeTables(JavaPlugin I,MythBans INST) {
		con = MythSQLConnect.getConnection();
		myth = I;
		srv = INST;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(!(sender instanceof ConsoleCommandSender)){
			sender.sendMessage("You must be console to run this.");
			return true;
		}else{
			Bukkit.getScheduler().runTaskAsynchronously(myth, new UpgradeTablesTask(this, srv));
		}
		return true;
	}

}
