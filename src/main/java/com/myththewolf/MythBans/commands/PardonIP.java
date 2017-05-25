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
import com.myththewolf.MythBans.lib.player.MythPlayerIP;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.PlayerLanguage;

public class PardonIP implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private MythPlayerIP ipClass = new MythPlayerIP();
	private String[] packet;
	private JavaPlugin MythPlugin;
	private String byUUID;
	private PlayerLanguage PL;
	public PardonIP(JavaPlugin pl) {
		MythPlugin = pl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		PL = new PlayerLanguage(sender);
		try {
			if (!sender.hasPermission(ConfigProperties.PARDON_PERMISSION)) {
				sender.sendMessage(PL.languageList.get("ERR_NO_PERMISSION"));
				return true;
			}
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("COMMAND-IPPARDON-USAGE"));
				return true;
			}
			if (args[0].charAt(0) == '/' && pCache.ipExist(args[0]) == false) {
				sender.sendMessage(PL.languageList.get("ERR_NULL_IP"));
				return true;
			}
			if (args[0].charAt(0) != '/' && pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(PL.languageList.get("ERR_NO_PERMISSION"));
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
					PL = new PlayerLanguage(i);
					if (i.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						PL = new PlayerLanguage(i);
						String dump = this.formatMessage(packet[0], PL.languageList.get("PUNISHMENT_IPPARDON"), byUUID);
						dump = dump.replaceAll("\\{1\\}",
								Arrays.toString(users.toArray(new String[users.size()])));
						dump = dump.replaceAll("\\{2\\}", IPs);
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
					PL = new PlayerLanguage(i);
					if (i.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						String dump = this.formatMessage(IP, PL.languageList.get("PUNISHMENT_IPPARDON"), byUUID);
						dump = dump.replaceAll("\\{2\\}", IP);
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
			toFormat = toFormat.replaceAll("\\{0\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{0\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(byUUID)).getName());
		}
		return toFormat;

	}
}
