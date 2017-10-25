package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.PlayerLanguage;

public class PardonUser implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private String toUUID = "";
	private MythPlayer PlayerClass;
	private String byUUID;
	private PlayerLanguage PL;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			PL = new PlayerLanguage(sender);
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("COMMAND_PARDON_USAGE"));
				return true;
			} else if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NULL_PLAYER"));
				return true;
			} else if (!sender.hasPermission(ConfigProperties.PARDON_PERMISSION)) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NO_PERMISSION"));
				return true;
			} else {
				toUUID = pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString();
				PlayerClass = DataCache.getPlayerInstance(toUUID);
				if (sender instanceof ConsoleCommandSender) {
					dbc.pardonUser(toUUID, "CONSOLE");
					byUUID = "CONSOLE";
				} else {
					dbc.pardonUser(toUUID, ((Player) sender).getUniqueId().toString());
					byUUID = ((Player) sender).getUniqueId().toString();
				}
				for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
					PL = new PlayerLanguage(player);
					if (player.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&',
								this.formatMessage(toUUID, PL.languageList.get("PUNISHMENT_USERPARDON"))));
					}
				}
				dbc.cleanUser(toUUID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	private String formatMessage(String UUID2, String format) throws SQLException {
		String toFormat = format;
		if (PlayerClass.getWhoBanned().equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{0\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{0\\}", pCache.getName(byUUID));
		}

		toFormat = toFormat.replaceAll("\\{1\\}", pCache.getName(UUID2));
		return toFormat;
	}
}
