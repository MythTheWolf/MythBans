package com.myththewolf.MythBans.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import com.myththewolf.MythBans.lib.tool.Utils;

public class IPBan implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private IP ipClass = new IP();
	private String[] packet;
	private JavaPlugin MythPlugin;

	public IPBan(JavaPlugin pl) {
		MythPlugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (!sender.hasPermission(ConfigProperties.BANIP_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You don't have permission to execute this command.");
				return true;
			}
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /banip <user|ip> [reason]");
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
				packet = pCache.getIPbyUUID(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString());
				String IPs = Arrays.toString(packet);
				if (ConfigProperties.DEBUG)
					MythPlugin.getLogger().info("Handeling IP Packet--> " + IPs);

				List<String> list = new ArrayList<String>();
				for (String IP : packet) {
					list.add(IP);
					if (sender instanceof ConsoleCommandSender) {
						dbc.banIP(IP, "CONSOLE", Utils.makeString(args, 1));
					} else {
						Player p = (Player) sender;
						dbc.banIP(IP, p.getUniqueId().toString(), Utils.makeString(args, 1));
					}
				}

				for (Player i : Bukkit.getOnlinePlayers()) {
					if (list.contains(i.getAddress().getAddress().toString())) {
						i.kickPlayer(this.formatMessage(packet[0], ConfigProperties.USER_IPBAN_FORMAT));
					} else if (i.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						String dump = this.formatMessage(packet[0], ConfigProperties.SERVER_IPBAN_FORMAT);
						dump = dump.replaceAll("\\{culprit\\}", IPs);
						i.sendMessage(dump);
					} else {
						continue;
					}
				}
			} else {
				/*** Its a IP String we are banning now ***/
				String IP = args[0];
				if (sender instanceof ConsoleCommandSender) {
					dbc.banIP(IP, "CONSOLE", Utils.makeString(args, 1));
				} else {
					Player p = (Player) sender;
					dbc.banIP(IP, p.getUniqueId().toString(), Utils.makeString(args, 1));
				}
				for (Player i : Bukkit.getOnlinePlayers()) {
					if (i.getAddress().getAddress().toString().equals(IP)) {
						i.kickPlayer(this.formatMessage(i.getAddress().getAddress().toString(),
								ConfigProperties.USER_IPBAN_FORMAT));
					} else if (i.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						String dump = this.formatMessage(IP, ConfigProperties.SERVER_IPBAN_FORMAT);
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

	private String formatMessage(String IP, String format) throws SQLException {
		String toFormat = format;

		if (ipClass.getWhoBanned(IP).equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{staffMember\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(ipClass.getWhoBanned(IP))).getName());
		}
		toFormat = toFormat.replaceAll("\\{reason\\}", ipClass.getReason(IP));

		return toFormat;

	}
}
