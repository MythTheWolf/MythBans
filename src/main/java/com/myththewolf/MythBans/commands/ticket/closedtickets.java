package com.myththewolf.MythBans.commands.ticket;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerTicket;

public class closedtickets implements CommandExecutor {
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
		if(!arg0.hasPermission(ConfigProperties.CLOSETICKET_PERMISSION))
		{
			arg0.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "You don't have permission to execute this command.");
			return true;
		}
		if (args.length < 1) {
			try {
				if(arg0.hasPermission(ConfigProperties.TICKETS_OTHER_PERMISSION)){
				PT.getClosedTickets(arg0, 1, max);
				}else{
					PT.getMyClosedTickets(arg0, 1, max);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				if (this.isInt(args[0])) {
					if(arg0.hasPermission(ConfigProperties.TICKETS_OTHER_PERMISSION))
					{
						PT.getClosedTickets(arg0, Integer.parseInt(args[0]), max);
					}else{
						PT.getMyClosedTickets(arg0, Integer.parseInt(args[0]), max);
					}
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
