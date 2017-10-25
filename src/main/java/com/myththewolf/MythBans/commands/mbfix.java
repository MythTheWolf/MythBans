package com.myththewolf.MythBans.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.myththewolf.MythBans.lib.MythBans;

public class mbfix implements CommandExecutor {
	private MythBans MB;

	public mbfix(MythBans instance) {
		MB = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage("Must be run by console");
			return true;
		}
		System.out.println("**** Attempting AutoFix ****");
		if(!MB.getLangGoverner().fixDefaults()){
			System.out.println("**** AutoFix Failed. You will need to fix manually. ****");
			MB.shutdown();
			MB.disableSelf();
		}else{
			System.out.println("**** AutoFix Succeeded ****");
			Bukkit.getScheduler().cancelTask(MB.getDisableTask().getTaskId());
			Bukkit.getServer().getPluginManager().disablePlugin(MB.getJavaPlugin());
			Bukkit.getServer().getPluginManager().enablePlugin(MB.getJavaPlugin());
		}
		return true;
	}
	

}
