package com.myththewolf.MythBans.lib.player.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.feilds.AbstractMaps;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.MythPlayerMetaData;

import net.md_5.bungee.api.ChatColor;

public class CommandEvent implements Listener {
	public CommandEvent(JavaPlugin pl) {

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(PlayerCommandPreprocessEvent e) throws Exception {
		MythPlayer MP = new MythPlayer(e.getPlayer().getUniqueId().toString());
		if(!MP.isOverride()){
			if(e.getMessage().indexOf("rules") > -1){
				System.out.println("UPDATING_RULES");
				AbstractMaps.read_rules.put(e.getPlayer().getUniqueId().toString(), true);
			}else if(!AbstractMaps.read_rules.containsKey(e.getPlayer().getUniqueId().toString())){
				System.out.println("SETTING_FALSE");
				AbstractMaps.read_rules.put(e.getPlayer().getUniqueId().toString(), false);
			}
		}
		if (ConfigProperties.DEBUG) {
			System.out.println("[MythBans]Captured command event!");
		}

		if (e.getPlayer().hasMetadata("is_potato")) {
			if (ConfigProperties.DEBUG) {
				System.out.println("[MythBans]User is potato, canceling..");
			}
			e.getPlayer().sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Potatoes can't execute commands!");
			e.setCancelled(true);
			return;
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
