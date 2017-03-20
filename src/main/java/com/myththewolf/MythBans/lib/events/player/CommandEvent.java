package com.myththewolf.MythBans.lib.events.player;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

import net.md_5.bungee.api.ChatColor;

public class CommandEvent implements Listener {
	private JavaPlugin thePlugin;
	public CommandEvent(JavaPlugin pl){
		
	}
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
		String[] command = e.getMessage().split(" ");
		System.out.println(Arrays.toString(command));
	}
}
