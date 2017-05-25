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
import com.myththewolf.MythBans.lib.tool.Utils;

public class IPBan implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private MythPlayerIP ipClass = new MythPlayerIP();
	private String[] packet;
	private PlayerLanguage PL;
	private String REASON;

	public IPBan(JavaPlugin pl) {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		PL = new PlayerLanguage(sender);
		boolean recursive = false;
		boolean silent = false;
		try {
			if (!sender.hasPermission(ConfigProperties.BANIP_PERMISSION)) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NO_PERMISSION"));
				return true;
			}
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("COMMAND_IPBAN_USAGE"));
				return true;
			}
			if (args[0].charAt(0) == '/' && pCache.ipExist(args[0]) == false) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NULL_IP"));
				return true;
			}
			if (args[0].charAt(0) != '/' && pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NULL_PLAYER"));
				return true;

			}
			REASON = Utils.makeString(args, 1);
			if (args[0].charAt(0) != '/') {
				
				packet = ipClass.getIPPack(pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString());
				Arrays.toString(packet);
				
				for (String I : args) {
				
					if (I.toLowerCase().indexOf("--r") > -1) {
						sender.sendMessage(ConfigProperties.PREFIX
								+ " Using recursive mode. All users matching IP will be banned.");
						REASON = REASON.replaceAll("--r", "");
						REASON = REASON.replaceAll("--R", "");
						recursive = true;

					} else if (I.toLowerCase().indexOf("--s") > -1) {
						silent = true;
						REASON = REASON.replaceAll("--s", "");
						REASON = REASON.replaceAll("--S", "");
						sender.sendMessage(ConfigProperties.PREFIX + " Using silent mode. No message will be given.");
					}
				}
				List<String> list = new ArrayList<String>();
				List<String> userPack = new ArrayList<String>();
				
				for (String IP : packet) {
					for (String UUID : pCache.getUUIDbyIP(IP)) {
						if (recursive && !userPack.contains(pCache.getName(UUID))) {
							System.out.println("Banning user--->" + pCache.getName(UUID));
							if (sender instanceof ConsoleCommandSender) {
								dbc.banUser(UUID, "CONSOLE", REASON);
							} else {
								dbc.banUser(UUID, ((Player) sender).getUniqueId().toString(),
										REASON);
							}
						}
						if (!userPack.contains(pCache.getName(UUID))) {
							userPack.add(pCache.getName(UUID));
						}
					}
					if (!list.contains(IP)) {
						list.add(IP);
					}
					if (sender instanceof ConsoleCommandSender) {
						dbc.banIP(IP, "CONSOLE", REASON);
					} else {
						Player p = (Player) sender;
						dbc.banIP(IP, p.getUniqueId().toString(), REASON);
					}
				}
				String[] dumpUsers = new String[userPack.size()];
				dumpUsers = userPack.toArray(new String[userPack.size()]);
				String users = Arrays.toString(dumpUsers);
				for (Player i : Bukkit.getOnlinePlayers()) {
					PL = new PlayerLanguage(i);

					if (list.contains(i.getAddress().getAddress().toString())) {

						i.kickPlayer(this.formatMessage(packet[0], PL.languageList.get("PUNISHMENT_IPBAN_KICK")));
					} else if (i.hasPermission(ConfigProperties.VIEWMSG_PERM) && !silent) {

						String dump = this.formatMessage(packet[0], PL.languageList.get("PUNISHMENT_IPBAN_INFORM"));
						dump = dump.replaceAll("\\{1\\}", users);
						dump = dump.replaceAll("\\{2\\}", Arrays.toString(packet));
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
				String users = Arrays.toString(pCache.getUUIDbyIP(IP));
				List<String> NEED = Arrays.asList(ipClass.getUUIDPack(IP));
				for (Player i : Bukkit.getOnlinePlayers()) {
					PL = new PlayerLanguage(i);
					if (recursive && !NEED.contains(i.getUniqueId().toString())) {
						dbc.banUser(i.getUniqueId().toString(), ipClass.getWhoBanned(IP), ipClass.getReason(IP));
						NEED.add(i.getUniqueId().toString());
					}
					if (i.getAddress().getAddress().toString().equals(IP)) {
						i.kickPlayer(this.formatMessage(i.getAddress().getAddress().toString(),
								PL.languageList.get("PUNISHMENT_IPBAN_KICK")));
					} else if (i.hasPermission(ConfigProperties.VIEWMSG_PERM) && !silent) {
						String dump = this.formatMessage(IP, PL.languageList.get("PUNISHMENT_IPBAN_INFORM"));
						dump = dump.replaceAll("\\{1\\}", users);
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

	private String formatMessage(String IP, String format) throws SQLException {
		String toFormat = format;

		if (ipClass.getWhoBanned(IP).equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{0\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{0\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(ipClass.getWhoBanned(IP))).getName());
		}
		toFormat = toFormat.replaceAll("\\{3\\}", ipClass.getReason(IP));

		return toFormat;

	}
}
