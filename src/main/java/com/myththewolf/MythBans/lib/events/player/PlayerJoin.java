package com.myththewolf.MythBans.lib.events.player;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
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
	private DatabaseCommands dbc = new DatabaseCommands();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {
		String message;
		name = e.getPlayer().getName();
		if (!pc.ipExist(e.getPlayer().getAddress().getAddress().toString())) {
			pc.addIP(e.getPlayer().getUniqueId().toString(), e.getPlayer().getAddress().getAddress().toString());
		} else if (!e.getPlayer().getUniqueId().toString()
				.equals(pc.getUUIDbyIP(e.getPlayer().getAddress().getAddress().toString()))) {
			pc.addIP(e.getPlayer().getUniqueId().toString(), e.getPlayer().getAddress().getAddress().toString());
		}
		if (pc.getPlayerExact(e.getPlayer().getName()) == null) {
			PlayerClass.processNewUser(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName());
		} else {
			switch (PlayerClass.getStatus(e.getPlayer().getUniqueId().toString())) {
			case "banned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(), ConfigProperties.USER_BAN_FORMAT);

				e.getPlayer().kickPlayer(message);
				break;
			case "tempBanned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(), ConfigProperties.USER_TEMPBAN_FORMAT);
				name = e.getPlayer().getName();
				if (d.getNewDate().before(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
					e.getPlayer().kickPlayer(message);
				} else if (d.getNewDate().after(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
					PlayerClass.clearExpire(e.getPlayer().getUniqueId().toString());
				}
				break;
			default:
				break;
			}
			switch (dbc.getIPStatus(dbc.getStoredIP(e.getPlayer().getUniqueId().toString()))) {
			case "banned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(), ConfigProperties.USER_IPBAN_FORMAT);

				e.getPlayer().kickPlayer(message);
				break;
			case "tempBanned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(), ConfigProperties.USER_IPTEMPBAN_FORMAT);
				name = e.getPlayer().getName();
				if (d.getNewDate().before(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
					e.getPlayer().kickPlayer(message);
				} else if (d.getNewDate().after(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
					PlayerClass.clearExpire(e.getPlayer().getUniqueId().toString());
				}
				break;
			default:
				break;

			}

		}
	}

	private String formatMessage(String UUID2, String format) throws SQLException {
		String toFormat = format;
		if (PlayerClass.getWhoBanned(UUID2).equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{staffMember\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(PlayerClass.getWhoBanned(UUID2))).getName());
		}

		toFormat = toFormat.replaceAll("\\{culprit\\}", Bukkit.getOfflinePlayer(UUID.fromString(UUID2)).getName());
		toFormat = toFormat.replaceAll("\\{reason\\}", PlayerClass.getReason(UUID2));

		return toFormat;
	}
}
