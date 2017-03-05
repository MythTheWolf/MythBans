package com.myththewolf.MythBans.commands.ticket;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerTicket;
import com.myththewolf.MythBans.lib.tool.Utils;

import net.md_5.bungee.api.ChatColor;

public class tickettp implements CommandExecutor {
	private PlayerTicket PT = new PlayerTicket();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(args.length < 1)
		{
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /tickettp <id>");
			return true;
		}
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(ConfigProperties.PREFIX + "*Magically teleports your server box*");
			return true;
		}
		try {
			if (!PT.exists(args[0])) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Ticket ID not found");
				return true;
			}
			((Player) sender).teleport(Utils.parseLocation(PT.getLocation(args[0])));
			sender.sendMessage("Teleported to location!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
