package com.myththewolf.MythBans.lib.player.events;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.AbstractMaps;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.MythPlayerIP;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.PlayerLanguage;
import com.myththewolf.MythBans.lib.tool.MythDate;

import net.md_5.bungee.api.ChatColor;

public class PlayerJoin implements Listener {
	private PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private MythPlayer PlayerClass;
	private final com.myththewolf.MythBans.lib.tool.MythDate d = new MythDate();
	private DatabaseCommands dbc = new DatabaseCommands();
	private MythPlayerIP ipClass = new MythPlayerIP();
	private JavaPlugin thePlugin;

	private PlayerLanguage lang;

	public PlayerJoin(JavaPlugin pl) {
		thePlugin = pl;
	
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {
		if (AbstractMaps.serverIsUpgrading()) {
			e.getPlayer().kickPlayer("Database is upgrading. Check back later!");
			return;
		}
		try {

			e.getPlayer().setInvulnerable(false);
			e.getPlayer().removeMetadata("is_potato", thePlugin);
			System.out.println("IMBOUND---->" + e.getPlayer().getName());

			String message;
			lang = new PlayerLanguage(e.getPlayer());
			if (!pc.ipExist(e.getPlayer().getAddress().getAddress().toString())) {
				pc.addIP(e.getPlayer().getUniqueId().toString(), e.getPlayer().getAddress().getAddress().toString());
			}
			if (!ipClass.mappedIpExist(e.getPlayer().getUniqueId().toString(),
					e.getPlayer().getAddress().getAddress().toString())) {
				pc.addIP(e.getPlayer().getUniqueId().toString(), e.getPlayer().getAddress().getAddress().toString());
			}
			if (pc.getPlayerExact(e.getPlayer().getName()) == null) {
				MythPlayer.processNewUser(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName());
				MythPlayer.setSession(e.getPlayer().getUniqueId().toString(), d.formatDate(d.getNewDate()));
			}
			PlayerClass = DataCache.getPlayerInstance(e.getPlayer().getUniqueId().toString());
			switch (PlayerClass.getStatus()) {
			case "OK":

				dbc.cleanUser(e.getPlayer().getUniqueId().toString());
				break;
			case "banned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(),
						lang.languageList.get("PUNISHMENT_BAN_KICK"), false);

				e.getPlayer().kickPlayer(message);
				return;
			case "tempBanned":
				message = this.formatMessage(e.getPlayer().getUniqueId().toString(),
						lang.languageList.get("PUNISHMENT_TEMPBAN_KICK"), false);
				e.getPlayer().getName();
				if (d.getNewDate().before(PlayerClass.getExpireDate())) {
					e.getPlayer().kickPlayer(message);
					return;
				} else if (d.getNewDate().after(PlayerClass.getExpireDate())) {
					PlayerClass.clearExpire();
				}
				break;
			default:
				break;
			}
			PlayerClass.setName(e.getPlayer().getName());
			for (String IP : ipClass.getIPPack(e.getPlayer().getUniqueId().toString())) {
				switch (dbc.getIPStatus(IP)) {
				case "banned":
					message = this.formatMessage(e.getPlayer().getAddress().getAddress().toString(),
							lang.languageList.get("PUNISHMENT_IPBAN_KICK"), true);
					e.getPlayer().kickPlayer(message);
					return;
				case "tempBanned":
					message = this.formatMessage(e.getPlayer().getUniqueId().toString(),
							ConfigProperties.USER_IPTEMPBAN_FORMAT, true);
					e.getPlayer().getName();
					if (d.getNewDate().before(PlayerClass.getExpireDate())) {
						e.getPlayer().kickPlayer(message);
						return;
					} else if (d.getNewDate().after(PlayerClass.getExpireDate())) {
						PlayerClass.clearExpire();
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

		} catch (Exception EE) {
			e.getPlayer().kickPlayer(ChatColor.DARK_RED + "A INTERNAL ERROR HAS OCCURRED" + ChatColor.GOLD
					+ "\nPlease message one of the owners about this!");
			EE.printStackTrace();
		}
	}

	private String formatMessage(String UUID2, String format, boolean IP) throws SQLException {
		String toFormat = format;
		if (IP) {
			toFormat = toFormat.replaceAll("\\{1\\}", UUID2);

			if (ipClass.getWhoBanned(UUID2).equals("CONSOLE")) {
				toFormat = toFormat.replaceAll("\\{0\\}", "CONSOLE");
			} else {
				toFormat = toFormat.replaceAll("\\{0\\}", pc.getName(ipClass.getWhoBanned(UUID2)));
			}
			toFormat = toFormat.replaceAll("\\{3\\}", ipClass.getReason(UUID2));
		} else {

			toFormat = toFormat.replaceAll("\\{1\\}", pc.getName(UUID2));

			if (PlayerClass.getWhoBanned().equals("CONSOLE")) {
				toFormat = toFormat.replaceAll("\\{0\\}", "CONSOLE");
			} else {
				toFormat = toFormat.replaceAll("\\{0\\}", pc.getName(PlayerClass.getWhoBanned()));
			}

			toFormat = toFormat.replaceAll("\\{2\\}", PlayerClass.getReason());

			MythDate MythDate = new MythDate();
			String PD = "undefined";
			if (DataCache.getPlayerInstance(UUID2).getStatus().equals("tempbanned")) {
				if (MythDate.getNewDate().before(PlayerClass.getExpireDate())) {
					long mili = MythDate.getTimeDifference(MythDate.getNewDate(), PlayerClass.getExpireDate());
					PD = MythDate.convertToPd(mili);
				}

				toFormat = toFormat.replaceAll("\\{3\\}", PD);
			}
		}
		return toFormat;
	}
}