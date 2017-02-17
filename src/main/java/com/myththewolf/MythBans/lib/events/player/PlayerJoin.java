package com.myththewolf.MythBans.lib.events.player;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.IP;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Date;

import net.md_5.bungee.api.ChatColor;

public class PlayerJoin implements Listener {
	private PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private com.myththewolf.MythBans.lib.player.Player PlayerClass = new com.myththewolf.MythBans.lib.player.Player();
	private final com.myththewolf.MythBans.lib.tool.Date d = new Date();
	private DatabaseCommands dbc = new DatabaseCommands();
	private IP ipClass = new IP();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {
		System.out.println("IMBOUND---->" + e.getPlayer().getName());
		String message;
		if (!pc.ipExist(e.getPlayer().getAddress().getAddress().toString())) {
			pc.addIP(e.getPlayer().getUniqueId().toString(), e.getPlayer().getAddress().getAddress().toString());
		}
		if (pc.getPlayerExact(e.getPlayer().getName()) == null) {
			PlayerClass.processNewUser(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName());
		} else {
			switch (PlayerClass.getStatus(e.getPlayer().getUniqueId().toString())) {
			case "OK":
				dbc.cleanUser(e.getPlayer().getUniqueId().toString());
				break;
			case "banned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(), ConfigProperties.USER_BAN_FORMAT,
						false);

				e.getPlayer().kickPlayer(message);
				break;
			case "tempBanned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(),
						ConfigProperties.USER_TEMPBAN_FORMAT, false);
				e.getPlayer().getName();
				if (d.getNewDate().before(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
					e.getPlayer().kickPlayer(message);
				} else if (d.getNewDate().after(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
					PlayerClass.clearExpire(e.getPlayer().getUniqueId().toString());
				}
				break;
			default:
				break;
			}
			switch (dbc.getIPStatus(e.getPlayer().getAddress().getAddress().toString())) {
			case "banned":
				message = this.formatMessage(e.getPlayer().getAddress().getAddress().toString(),
						ConfigProperties.USER_IPBAN_FORMAT, true);
				e.getPlayer().kickPlayer(message);
				break;
			case "tempBanned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(),
						ConfigProperties.USER_IPTEMPBAN_FORMAT, true);
				e.getPlayer().getName();
				if (d.getNewDate().before(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
					e.getPlayer().kickPlayer(message);
				} else if (d.getNewDate().after(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
					PlayerClass.clearExpire(e.getPlayer().getUniqueId().toString());
				}
				break;
			default:
				break;

			}
			if (ipClass.getTheFam(e.getPlayer().getAddress().getAddress().toString(),e.getPlayer().getUniqueId().toString()) != null) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.hasPermission(ConfigProperties.VIEW_PROBATION_PERMISSION)) {
						p.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "WARNING: " + ChatColor.GOLD
								+ e.getPlayer().getName() + " shares the same IPs as "
								+ Arrays.toString(ipClass.getTheFam(e.getPlayer().getAddress().getAddress().toString(),e.getPlayer().getUniqueId().toString())) + ChatColor.YELLOW + " IP : " + e.getPlayer().getAddress().getAddress().toString());
					}
				}
			}
		}
	}

	private String formatMessage(String UUID2, String format, boolean IP) throws SQLException {
		String toFormat = format;
		if (IP) {
			toFormat = toFormat.replaceAll("\\{culprit\\}", UUID2);

			if (ipClass.getWhoBanned(UUID2).equals("CONSOLE")) {
				toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
			} else {
				toFormat = toFormat.replaceAll("\\{staffMember\\}",
						Bukkit.getOfflinePlayer(UUID.fromString(ipClass.getWhoBanned(UUID2))).getName());
			}
			toFormat = toFormat.replaceAll("\\{reason\\}", ipClass.getReason(UUID2));
		} else {

			toFormat = toFormat.replaceAll("\\{culprit\\}", Bukkit.getOfflinePlayer(UUID.fromString(UUID2)).getName());

			if (PlayerClass.getWhoBanned(UUID2).equals("CONSOLE")) {
				toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
			} else {
				toFormat = toFormat.replaceAll("\\{staffMember\\}",
						Bukkit.getOfflinePlayer(UUID.fromString(PlayerClass.getWhoBanned(UUID2))).getName());
			}

			toFormat = toFormat.replaceAll("\\{reason\\}", PlayerClass.getReason(UUID2));
		}
		return toFormat;
	}
}
