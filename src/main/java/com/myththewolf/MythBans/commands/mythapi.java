package com.myththewolf.MythBans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class mythapi implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		String pre = ConfigProperties.PREFIX;
		sender.sendMessage(pre + "Using MythBans v"+ ConfigProperties.VERSION + " (Implementing MythAPI v6.0)");
		sender.sendMessage(pre + "Copyright 2017-2018 MythTheWolf");
		sender.sendMessage(pre + "http://www.github.com/MythTheWolf/");
		return true;
	}

}
