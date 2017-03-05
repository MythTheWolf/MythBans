package com.myththewolf.MythBans.commands.ticket;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.PlayerTicket;

import net.md_5.bungee.api.ChatColor;

public class Ticket implements CommandExecutor {
	private PlayerTicket PT = new PlayerTicket();
	private PlayerCache PC = new PlayerCache(MythSQLConnect.getConnection());

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!sender.hasPermission(ConfigProperties.CLOSETICKET_PERMISSION)) {
			sender.sendMessage(
					ConfigProperties.PREFIX + ChatColor.RED + "You don't have permission to execute this command.");
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "/ticket <id>");
			return true;
		}
		try {
			if (!PT.exists(args[0])) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Ticket with that ID was not found.");
				return true;
			}
		
			sender.sendMessage(ChatColor.YELLOW + "-----------------------------------");
			sender.sendMessage("ID: " + args[0]);
			sender.sendMessage("Status: " + PT.getStatus(args[0]));
			sender.sendMessage("Reported by: " + PC.getName(PT.getSender(args[0])));
			sender.sendMessage("Location: " + PT.getLocation(args[0]));
			sender.sendMessage("Message: " + PT.getMessage(args[0]));
			if (PT.getStatus(args[0]).equals("CLOSED")) {
				sender.sendMessage("Closed by: " + PC.getName(PT.getHandler(args[0])));
				sender.sendMessage("Close message: " + PT.getClose(args[0]));
			}
			sender.sendMessage(ChatColor.YELLOW + "-----------------------------------");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
