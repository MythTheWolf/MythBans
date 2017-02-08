package com.myththewolf.MythBans.commands;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Group;

import net.md_5.bungee.api.ChatColor;

public class createUI implements CommandExecutor {
	private Group group = new Group();
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /createUI <player>");
		}
		if(!sender.hasPermission(ConfigProperties.CREATE_UI_PERMISSION))
		{
			sender.sendMessage(ConfigProperties.PREFIX + "You do not have permission for that command.");
		}
		
		return true;
	}
	private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}


