package com.myththewolf.MythBans.commands.ticket;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
		if (!sender.hasPermission(ConfigProperties.CLOSETICKET_PERMISSION)) {
			sender.sendMessage(ConfigProperties.PREFIX + "You don't have permission to execute this command.");
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
			PT.closeTicket(args[0], ((Player) sender).getUniqueId().toString(), text);
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GREEN + "Closed ticket!");
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getUniqueId().toString().equals(PT.getSender(args[0]))) {
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + ((Player) sender).getDisplayName()
							+ " has closed your ticket, #" + args[0]);
				} else if (p.hasPermission(ConfigProperties.TICKETS_OTHER_PERMISSION)) {
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + ((Player) sender).getDisplayName()
							+ " has closed ticket, #" + args[0]);
				}
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
