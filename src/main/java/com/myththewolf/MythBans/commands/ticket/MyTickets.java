package com.myththewolf.MythBans.commands.ticket;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerTicket;

public class MyTickets implements CommandExecutor {
	private int max = 5;
	private String label = ConfigProperties.PREFIX;
	private PlayerTicket PT = new PlayerTicket();

	public boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] args) {
		if (args.length < 1) {
			try {
				PT.getMyTickets(arg0, 1, max);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				if (this.isInt(args[0])) {
					PT.getMyTickets((Player) arg0, Integer.parseInt(args[0]), max);
					return true;
				} else {
					arg0.sendMessage(label + ChatColor.RED + "Not a integer");
					return false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
}
