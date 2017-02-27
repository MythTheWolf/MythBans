package com.myththewolf.MythBans.threads;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerTicket;

import net.md_5.bungee.api.ChatColor;

public class WarnUnsolvedTickets implements Runnable {
	private PlayerTicket PT = new PlayerTicket();
	private JavaPlugin pl;

	public WarnUnsolvedTickets(JavaPlugin mythPlugin) {
		pl = mythPlugin;
	}

	@Override
	public void run() {
		if (ConfigProperties.DEBUG) {
			pl.getLogger().info("Checking for unsolved tickets...");
		}
		int num_unclosed = 0;
		/* Message all admins about unclosed tickets */
		try {
			num_unclosed = PT.getUnclosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!(num_unclosed < 1)) {
			for (Player p : Bukkit.getOnlinePlayers()) {

				if (p.hasPermission(ConfigProperties.VIEW_PROBATION_PERMISSION)) {
					p.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "WARNING!" + ChatColor.YELLOW + "]"
							+ ChatColor.GOLD + "There are " + Integer.toString(num_unclosed)
							+ " unclosed tickets. Please review them now.");
				}
			}
		}
	}

}
