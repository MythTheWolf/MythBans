package com.myththewolf.MythBans.lib.events.player;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Player;

public class PlayerChat implements Listener {

	private DatabaseCommands dbc = new DatabaseCommands();

	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent e) throws SQLException {
		org.bukkit.entity.Player p = e.getPlayer();
		String UUID = p.getUniqueId().toString();
		Player playerClass = new Player();
		if (playerClass.getStatus(UUID).equals("muted")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigProperties.PREFIX)
					+ "Your voice has been silenced!");
			e.setCancelled(true);
		} else if (dbc.getIPStatus(dbc.getStoredIP(e.getPlayer().getUniqueId().toString())).equals("muted")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigProperties.PREFIX)
					+ "Your voice has been silenced!");
			e.setCancelled(true);
			return;
		} else if (playerClass.getStatus(e.getPlayer().getUniqueId().toString()).equals("trial")) {
			e.setCancelled(true);
			String message = e.getMessage();
			for (org.bukkit.entity.Player i : Bukkit.getOnlinePlayers()) {
				String who = ChatColor.translateAlternateColorCodes('&', e.getPlayer().getDisplayName());
				if (i.hasPermission(ConfigProperties.VIEW_PROBATION_PERMISSION)) {
					if (i.hasPermission("essentials.chat.color")) {

						i.sendMessage(ChatColor.WHITE + "< " + ChatColor.RED + "* " + who + ChatColor.WHITE + "> "
								+ ChatColor.translateAlternateColorCodes('&', message));
					} else {
						i.sendMessage(ChatColor.WHITE + "< " + ChatColor.RED + "* " + who + ChatColor.WHITE + " > "
								+ message);
					}
				} else {
					if (i.hasPermission("essentials.chat.color")) {
						i.sendMessage(ChatColor.WHITE + "< " + who + ChatColor.WHITE + "> "
								+ ChatColor.translateAlternateColorCodes('&', message));
					} else {
						i.sendMessage(ChatColor.WHITE + "< " + who + ChatColor.WHITE + "> " + message);
					}
				}
			}
		}
		return;
	}
}
