package com.myththewolf.MythBans.commands.ticket;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerTicket;
import com.myththewolf.MythBans.lib.tool.Utils;

import net.md_5.bungee.api.ChatColor;

public class CloseTicket implements CommandExecutor {
	private PlayerTicket PT = new PlayerTicket();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "You can't use this command in the console!");
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /close <ticket id> <response>");
			return true;
		}
		try {
			if (!PT.exists(args[0])) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Ticket not found.");
				return true;
			}
			String text = Utils.makeString(args, 1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
