package com.myththewolf.MythBans.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

import net.md_5.bungee.api.ChatColor;

public class ReloadMythBans implements CommandExecutor {
	private JavaPlugin myth;

	public ReloadMythBans(JavaPlugin pl) {
		myth = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!sender.hasPermission(ConfigProperties.RELOAD)) {
			sender.sendMessage(
					ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission to use this command.");
			return true;
		} else {
			
			Bukkit.getServer().getPluginManager().disablePlugin(myth);
			Bukkit.getServer().getPluginManager().enablePlugin(myth);
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GREEN + "MythBans reloaded!");
			return true;
		}
	}

}
