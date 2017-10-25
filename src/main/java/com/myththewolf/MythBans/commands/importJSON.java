package com.myththewolf.MythBans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.player.PlayerLanguage;
import com.myththewolf.MythBans.tasks.ImportJSON;

public class importJSON implements CommandExecutor {
	private JavaPlugin pl;
	
	
	public importJSON(JavaPlugin i )
	{
		pl = i;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		PlayerLanguage PL = new PlayerLanguage(sender);
		sender.sendMessage(PL.languageList.get("COMMAND_IMPORTJSON_TASKSTART"));
		new ImportJSON(pl,sender).runTaskAsynchronously(pl);
        return true;
	}

}
