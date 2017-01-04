package com.myththewolf.MythBans.lib.events.player;

import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Date;

public class PlayerJoin implements Listener {
	private PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private String name;
	private com.myththewolf.MythBans.lib.player.Player PlayerClass = new com.myththewolf.MythBans.lib.player.Player();
	private final com.myththewolf.MythBans.lib.tool.Date d = new Date();
	private String toFormat = "";
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) throws SQLException
	{
		String message;
		name = e.getPlayer().getName();
		if (pc.getPlayerExact(e.getPlayer().getName()) == null)
		{
			PlayerClass.processNewUser(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName());
		} else
		{
			switch (PlayerClass.getStatus(e.getPlayer().getUniqueId().toString()))
			{
			case "banned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(), "ban");
			
				e.getPlayer().kickPlayer(message);
				break;
			case "tempBanned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(), "tempBanned");
				name = e.getPlayer().getName();
				if (d.getNewDate().before(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString())))
				{
					e.getPlayer().kickPlayer(message);
				} else if (d.getNewDate().after(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString())))
				{
					PlayerClass.clearExpire(e.getPlayer().getUniqueId().toString());
				}
				break;
			default:
				break;
			}

		}
	}

	private String formatMessage(String UUID,String key) throws SQLException
	{
		
		switch(key)
		{
		case "ban":
			toFormat = ConfigProperties.USER_BAN_FORMAT;
			break;
		case "tempBanned":
			toFormat = ConfigProperties.USER_TEMPBAN_FORMAT;
			toFormat = toFormat.replaceAll("\\{expire\\}", PlayerClass.getExpireDate(UUID).toString());
			break;
		default: break;
		}
		toFormat = toFormat.replaceAll("\\{staffMember\\}", PlayerClass.getWhoBanned(UUID));
		toFormat = toFormat.replaceAll("\\{culprit\\}", name);
		toFormat = toFormat.replaceAll("\\{reason\\}", PlayerClass.getReason(UUID));
		
		return toFormat;
	}

}
