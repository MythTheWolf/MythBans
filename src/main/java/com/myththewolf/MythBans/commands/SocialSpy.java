package com.myththewolf.MythBans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.MythPlayerMetaData;

import net.md_5.bungee.api.ChatColor;

public class SocialSpy implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "You gotta be a player.");
			return true;
		}
		if (!sender.hasPermission(ConfigProperties.SOCIALSPY_PERMISSION)) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "No permission");
			return true;
		}

		try {
			MythPlayerMetaData MPM = new MythPlayerMetaData(((Player) sender).getUniqueId().toString());
			if (MPM.isSpying()) {
				MPM.setSpy(false);
				sender.sendMessage(ConfigProperties.PREFIX + "No longer spying.");
				return true;
			} else {
				sender.sendMessage(ConfigProperties.PREFIX + "Now spying.");
				MPM.setSpy(true);
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
