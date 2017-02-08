package com.myththewolf.MythBans.commands;

import org.bukkit.command.Command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import com.myththewolf.MythBans.threads.ImportJSON;

public class importJSON implements CommandExecutor {
	private JavaPlugin pl;
	
	
	public importJSON(JavaPlugin i )
	{
		pl = i;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		sender.sendMessage("Schedualing task..");
		new ImportJSON(pl,sender).runTaskAsynchronously(pl);
        return true;
	}

}
