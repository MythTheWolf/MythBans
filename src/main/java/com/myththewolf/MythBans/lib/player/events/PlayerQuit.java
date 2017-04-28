package com.myththewolf.MythBans.lib.player.events;

import java.sql.SQLException;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.myththewolf.MythBans.lib.feilds.PlayerDataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.tool.Date;

public class PlayerQuit implements Listener {
	private Date date = new Date();
	private MythPlayer pClass;

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) throws SQLException {
		Player p = e.getPlayer();
		pClass = PlayerDataCache.getInstance(e.getPlayer().getUniqueId().toString());
		pClass.setQuitTime(date.formatDate(date.getNewDate()));
		long tick = p.getStatistic(Statistic.PLAY_ONE_TICK);
		pClass.setPlayTime((tick / 20) * 1000);
	}
}
