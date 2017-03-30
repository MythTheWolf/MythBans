package com.myththewolf.MythBans.lib.events.player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
	private JavaPlugin thePlugin;

	public PlayerJoin(JavaPlugin pl) {
		thePlugin = pl;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {

		e.getPlayer().setInvulnerable(false);
		e.getPlayer().removeMetadata("is_potato", thePlugin);
		System.out.println("IMBOUND---->" + e.getPlayer().getName());
		String message;
		if (!pc.ipExist(e.getPlayer().getAddress().getAddress().toString())) {
			pc.addIP(e.getPlayer().getUniqueId().toString(), e.getPlayer().getAddress().getAddress().toString());
		}
		if (!ipClass.mappedIpExist(e.getPlayer().getUniqueId().toString(),
				e.getPlayer().getAddress().getAddress().toString())) {
			pc.addIP(e.getPlayer().getUniqueId().toString(), e.getPlayer().getAddress().getAddress().toString());
		}
		if (pc.getPlayerExact(e.getPlayer().getName()) == null) {
			PlayerClass.processNewUser(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName());
			PlayerClass.setSession(e.getPlayer().getUniqueId().toString(), d.formatDate(d.getNewDate()));
		}

		switch (PlayerClass.getStatus(e.getPlayer().getUniqueId().toString())) {
		case "OK":
			PlayerClass.setSession(e.getPlayer().getUniqueId().toString(), d.formatDate(d.getNewDate()));
			dbc.cleanUser(e.getPlayer().getUniqueId().toString());
			break;
		case "banned":
			message = this.formatMessage(e.getPlayer().getUniqueId().toString(), ConfigProperties.USER_BAN_FORMAT,
					false);

			e.getPlayer().kickPlayer(message);
			return;
		case "tempBanned":
			message = this.formatMessage(e.getPlayer().getUniqueId().toString(), ConfigProperties.USER_TEMPBAN_FORMAT,
					false);
			e.getPlayer().getName();
			if (d.getNewDate().before(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
				e.getPlayer().kickPlayer(message);
				return;
			} else if (d.getNewDate().after(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
				PlayerClass.clearExpire(e.getPlayer().getUniqueId().toString());
			}
			break;
		default:
			break;
		}
		for (String IP : ipClass.getIPPack(e.getPlayer().getUniqueId().toString())) {
			switch (dbc.getIPStatus(IP)) {
			case "banned":
				message = this.formatMessage(e.getPlayer().getAddress().getAddress().toString(),
						ConfigProperties.USER_IPBAN_FORMAT, true);
				e.getPlayer().kickPlayer(message);
				return;
			case "tempBanned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(),
						ConfigProperties.USER_IPTEMPBAN_FORMAT, true);
				e.getPlayer().getName();
				if (d.getNewDate().before(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
					e.getPlayer().kickPlayer(message);
					return;
				} else if (d.getNewDate().after(PlayerClass.getExpireDate(e.getPlayer().getUniqueId().toString()))) {
					PlayerClass.clearExpire(e.getPlayer().getUniqueId().toString());
				}
				break;
			default:
				break;

			}
		}
		List<String> commonUsers = new ArrayList<String>();
		String theID = e.getPlayer().getUniqueId().toString();
		String[] IPs = ipClass.getIPPack(theID);

		for (String IP : IPs) {
			if (ipClass.getTheFam(IP, theID) != null) {
				for (String singleUser : ipClass.getTheFam(IP, theID)) {
					if (!commonUsers.contains(singleUser)) {
						commonUsers.add(singleUser);
					}
				}
			}
		}
		String[] arr = new String[commonUsers.size()];
		arr = commonUsers.toArray(new String[commonUsers.size()]);
		if (arr.length > 0) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission(ConfigProperties.VIEW_PROBATION_PERMISSION)) {
					p.sendMessage(ConfigProperties.PREFIX + ChatColor.DARK_RED + "WARNING:" + ChatColor.GOLD
							+ e.getPlayer().getName() + " has the same IP(s) as " + Arrays.toString(arr));
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
