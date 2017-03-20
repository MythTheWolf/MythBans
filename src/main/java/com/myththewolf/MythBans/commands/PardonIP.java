package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.IP;
import com.myththewolf.MythBans.lib.player.PlayerCache;

import net.md_5.bungee.api.ChatColor;

public class PardonIP implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private IP ipClass = new IP();
	private String[] packet;
	private JavaPlugin MythPlugin;
	private String byUUID;
	private com.myththewolf.MythBans.lib.player.Player playerClass = new com.myththewolf.MythBans.lib.player.Player();

	public PardonIP(JavaPlugin pl) {
		MythPlugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (!sender.hasPermission(ConfigProperties.PARDON_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You don't have permission to execute this command.");
				return true;
			}
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /pardonip <user|ip>");
				return true;
			}
			if (args[0].charAt(0) == '/' && pCache.ipExist(args[0]) == false) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "IP Not found.");
				return true;
			}
			if (args[0].charAt(0) != '/' && pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player Not found.");
				return true;
			}
			if (args[0].charAt(0) != '/') {
				packet = ipClass.getIPPack(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString());
				String IPs = Arrays.toString(packet);
				if (ConfigProperties.DEBUG)
					MythPlugin.getLogger().info("Handeling IP Packet--> " + IPs);

				List<String> list = new ArrayList<String>();
				List<String> users = new ArrayList<String>();
				for (String IP : packet) {
					if (!list.contains(IP)) {
						list.add(IP);
					}

					if (sender instanceof ConsoleCommandSender) {
						dbc.pardonIP(IP);
						dbc.cleanIP(IP);
						byUUID = "CONSOLE";
					} else {
						Player p = (Player) sender;
						dbc.pardonIP(IP);
						dbc.cleanIP(IP);
						byUUID = p.getUniqueId().toString();
					}
				}
				// Dumping users
				for (String IP : list) {
					for (String UUID : ipClass.getUUIDPack(IP)) {
						if (!list.contains(pCache.getName(UUID))) {
							users.add(pCache.getName(UUID));
						}
					}
				}
				for (Player i : Bukkit.getOnlinePlayers()) {
					if (i.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						String dump = this.formatMessage(packet[0], ConfigProperties.SERVER_IPUNBAN_FORMAT, byUUID);
						dump = dump.replaceAll("\\{culprit\\}",
								Arrays.toString(users.toArray(new String[users.size()])));
						dump = dump.replaceAll("\\{IP\\}", IPs);
						i.sendMessage(dump);
					} else {
						continue;
					}
				}
			} else {
				/*** Its a IP String we are banning now ***/
				String IP = args[0];
				System.out.println("IP::::" + IP);
				if (sender instanceof ConsoleCommandSender) {
					dbc.pardonIP(IP);
					dbc.cleanIP(IP);
					byUUID = "CONSOLE";
				} else {
					Player p = (Player) sender;
					dbc.pardonIP(IP);
					dbc.cleanIP(IP);
					byUUID = p.getUniqueId().toString();
				}

				for (Player i : Bukkit.getOnlinePlayers()) {

					if (i.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						String dump = this.formatMessage(IP, ConfigProperties.SERVER_IPBAN_FORMAT, byUUID);
						dump = dump.replaceAll("\\{culprit\\}", IP);
						i.sendMessage(dump);
					} else {
						continue;
					}
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	private String formatMessage(String IP, String format, String byUUID) throws SQLException {
		String toFormat = format;

		if (byUUID.equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{staffMember\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(byUUID)).getName());
		}
		toFormat = toFormat.replaceAll("\\{reason\\}", ipClass.getReason(IP));

		return toFormat;

	}
}
