package com.myththewolf.MythBans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

import net.md_5.bungee.api.ChatColor;

public class SocialSpy implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender instanceof ConsoleCommandSender){
			sender.sendMessage(ConfigProperties.PREFIX+ChatColor.RED+"");
		}
		FileConfiguration cfg;
		return true;
	}

}
