package com.myththewolf.MythBans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

import net.md_5.bungee.api.ChatColor;

public class createUI implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (args.length < 0) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /createUI <player>");
		}
		return true;
	}

}
