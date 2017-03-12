package com.myththewolf.MythBans.lib.events.player;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerEatEvent implements Listener {

	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if (e.getItem().getType().equals(Material.POTATO_ITEM)) {
			ItemStack theItem = e.getItem();
			ItemMeta theMeta = theItem.getItemMeta();
			List<String> theLore = theMeta.getLore();
			if (theLore.contains("PotatoPlayer")) {
				String UUID = theLore.get(2);
				System.out.println(UUID);
			}
		}
	}
}
