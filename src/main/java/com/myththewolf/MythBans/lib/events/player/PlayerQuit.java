package com.myththewolf.MythBans.lib.events.player;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.myththewolf.MythBans.lib.tool.Date;

public class PlayerQuit implements Listener {
	private Date date = new Date();
	private com.myththewolf.MythBans.lib.player.Player pClass = new com.myththewolf.MythBans.lib.player.Player();
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) throws SQLException{
		Player p = e.getPlayer();
		String UUID = p.getUniqueId().toString();
		pClass.setQuitTime(date.formatDate(date.getNewDate()), UUID);
		pClass.setPlayTime(UUID,date.getTimeDifference(pClass.getSessionJoinDate(UUID), date.getNewDate()) + pClass.getPlayTime(UUID));
	}
}
