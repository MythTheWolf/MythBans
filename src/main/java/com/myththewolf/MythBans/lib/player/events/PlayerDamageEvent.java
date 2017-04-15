package com.myththewolf.MythBans.lib.player.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageEvent implements Listener {
	@EventHandler
	public void onDamage(EntityDamageEvent e) {

		if (e.getEntity().hasMetadata("is_potato")) {
			e.setCancelled(true);
		}
	}
}
