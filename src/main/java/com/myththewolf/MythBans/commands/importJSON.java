package com.myththewolf.MythBans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.player.Player;
import com.myththewolf.MythBans.threads.ImportJSON;

public class importJSON implements CommandExecutor {
	private DatabaseCommands dbc = new DatabaseCommands();
	private com.myththewolf.MythBans.lib.player.Player pp = new Player();
	private JavaPlugin pl;
	
	
	public importJSON(JavaPlugin i )
	{
		pl = i;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		sender.sendMessage("Schedualing task..");
		BukkitTask task = new ImportJSON(pl,sender).runTaskAsynchronously(pl);
        return true;
	}

}
