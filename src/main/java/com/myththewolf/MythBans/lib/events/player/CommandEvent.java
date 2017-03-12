package com.myththewolf.MythBans.lib.events.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

import net.md_5.bungee.api.ChatColor;

public class CommandEvent implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if (ConfigProperties.DEBUG) {
			System.out.println("[MythBans]Captured command event!");
		}
		if (e.getPlayer().hasMetadata("is_potato")) {
			if (ConfigProperties.DEBUG) {
				System.out.println("[MythBans]User is potato, canceling..");
			}
			e.getPlayer().sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Potatoes can't execute commands!");
			e.setCancelled(true);
		}
	}
}
