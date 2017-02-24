package com.myththewolf.MythBans.threads;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerTicket;

import net.md_5.bungee.api.ChatColor;

public class WarnUnsolvedTickets implements Runnable {
	private PlayerTicket PT = new PlayerTicket();
	@Override
	public void run() {
		int num_unclosed = 0;
		/* Message all admins about unclosed tickets */
		try {
			num_unclosed  = PT.getUnclosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.sendMessage("FOOEY");
			if(p.hasPermission(ConfigProperties.VIEW_PROBATION_PERMISSION))
			{
				p.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "WARNING!" + ChatColor.YELLOW + "]" + ChatColor.GOLD + "There are " + Integer.toString(num_unclosed) + " unclosed tickets. Please review them now." );
			}
		}
	}

}
