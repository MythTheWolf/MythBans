package com.myththewolf.MythBans.lib.events.player;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Player;

public class PlayerChat implements Listener {
	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent e) throws SQLException
	{
		org.bukkit.entity.Player p = e.getPlayer();
		String UUID = p.getUniqueId().toString();
		Player playerClass = new Player();
		if(playerClass.getStatus(UUID).equals("muted"))
		{
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigProperties.PREFIX) + "Your voice has been silenced!");
			e.setCancelled(true);
		}else{
			return;
		}
	}
}
