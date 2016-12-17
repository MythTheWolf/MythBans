package com.myththewolf.MythBans.lib.events.player;

import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class PlayerJoin implements Listener {
	private PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private String name;
	private com.myththewolf.MythBans.lib.player.Player PlayerClass = new com.myththewolf.MythBans.lib.player.Player();
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) throws SQLException
	{
		if(pc.getPlayerExact(e.getPlayer().getName()) == null)
		{
			PlayerClass.processNewUser(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName());
		}
		switch(PlayerClass.getStatus(e.getPlayer().getUniqueId().toString()))
		{
		case "banned":
			String message = this.formatBan(e.getPlayer().getUniqueId().toString());
			name = e.getPlayer().getName();
			e.getPlayer().kickPlayer(message);
			break;
		}
	}
	private String formatBan(String UUID) throws SQLException
	{
		String toFormat = ConfigProperties.USER_BAN_FORMAT;
		toFormat.replaceAll("{staffMember}", PlayerClass.getWhoBanned(UUID));
		toFormat.replaceAll("{culprit}", name);
		toFormat.replaceAll("{reason}", PlayerClass.getReason(UUID));
		return toFormat;
	}
}
