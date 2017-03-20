package com.myththewolf.MythBans.lib.events.player;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.tool.Utils;

public class PlayerEatEvent implements Listener {
	public JavaPlugin thePlugin;
	public PlayerEatEvent(JavaPlugin mythPlugin) {
		thePlugin = mythPlugin;
	}

	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if (e.getItem().getType().equals(Material.POTATO_ITEM)) {
			ItemStack theItem = e.getItem();
			ItemMeta theMeta = theItem.getItemMeta();
			List<String> theLore = theMeta.getLore();
			if (theLore.contains("PotatoPlayer")) {
				String UUID2 = theLore.get(2);
				Bukkit.getPlayer(UUID.fromString(UUID2)).teleport(Utils.parseLocation(theLore.get(3)));
				Bukkit.getPlayer(UUID.fromString(UUID2)).setGameMode(GameMode.SURVIVAL);
				Bukkit.getPlayer(UUID.fromString(UUID2)).removeMetadata("is_potato", thePlugin);
			}
		}
	}
}
