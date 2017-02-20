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

public class IPKick implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private IP ipClass = new IP();
	private String[] packet;
	private JavaPlugin MythPlugin;
	private String byUUID;

	public IPKick(JavaPlugin pl) {
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
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /kickip <user|ip> [reason]");
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

				if (sender instanceof ConsoleCommandSender) {
					byUUID = "CONSOLE";
				} else {
					byUUID = ((Player) sender).getUniqueId().toString();
				}
				packet = pCache.getIPbyUUID(pCache.getUUID(args[0]));
				String IPs = Arrays.toString(packet);
				if (packet.length < 1) {
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "No users currently on with that IP");
					return true;
				}
				if (ConfigProperties.DEBUG) {
					MythPlugin.getLogger().info("Handeling IP Packet--> " + IPs);
				}
				List<String> list = new ArrayList<String>();

				for (String IP : packet) {
					list.add(IP);

				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (list.contains(p.getAddress().getAddress().toString())) {
						p.kickPlayer(this.formatMessage(p.getAddress().getAddress().toString(),
								ConfigProperties.USER_IPKICK_FORMAT, byUUID, Utils.makeString(args, 1)));
					} else if (p.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						String dump = this.formatMessage(list.get(0), ConfigProperties.SERVER_IPKICK_FORMAT, byUUID,
								Utils.makeString(args, 1));
						dump = dump.replaceAll("\\{culprit\\}", IPs);
						p.kickPlayer(dump);
					} else {
						continue;
					}
				}
				return true;
			} else {
				String IP = args[0];
				if (sender instanceof ConsoleCommandSender) {
					byUUID = "CONSOLE";
				} else {
					byUUID = ((Player) sender).getUniqueId().toString();
				}
				String[] userPacket = pCache.getUUIDbyIP(IP);
				String userPack = Arrays.toString(userPacket);
				List<String> list = new ArrayList<String>();
				for (String UUID : userPacket) {
					list.add(UUID);
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (list.contains(p.getAddress().getAddress().toString())) {
						p.kickPlayer(this.formatMessage(p.getAddress().getAddress().toString(),
								ConfigProperties.USER_IPKICK_FORMAT, byUUID, Utils.makeString(args, 1)));
					} else if (p.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						String dump = this.formatMessage(p.getAddress().getAddress().toString(),
								ConfigProperties.SERVER_IPKICK_FORMAT, byUUID, Utils.makeString(args, 1));
						dump = dump.replaceAll("\\{culprit\\}", userPack);
						dump = dump.replaceAll("\\{IP\\}", IP);
						p.sendMessage(dump);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	private String formatMessage(String IP, String format, String byUUID, String reason) throws SQLException {
		String toFormat = format;

		if (byUUID.equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{staffMember\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(byUUID)).getName());
		}
		toFormat = toFormat.replaceAll("\\{reason\\}", reason);

		return toFormat;

	}
}
