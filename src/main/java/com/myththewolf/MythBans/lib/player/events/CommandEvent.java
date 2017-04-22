package com.myththewolf.MythBans.lib.player.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.MythPlayerMetaData;

import net.md_5.bungee.api.ChatColor;

public class CommandEvent implements Listener {
	public CommandEvent(JavaPlugin pl) {

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(PlayerCommandPreprocessEvent e) throws Exception {
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
		/* ALL OF THE NOPE */
		if (!(e.getMessage().indexOf("cs_trigger") > 0)) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				MythPlayerMetaData MPM = new MythPlayerMetaData(p.getUniqueId().toString());
				if (MPM.isSpying()) {
					p.sendMessage(e.getPlayer().getDisplayName()+":" + e.getMessage());
				}
			}
		}
	}
}
