package com.myththewolf.MythBans.commands;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerDataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.PlayerLanguage;
import com.myththewolf.MythBans.lib.tool.Utils;

public class Kick implements CommandExecutor {
	private final PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private final DatabaseCommands dbc = new DatabaseCommands();
	private MythPlayer PlayerClass;
	private Player toKick;
	private PlayerLanguage PL;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		PL = new PlayerLanguage(sender);
		try {

			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("COMMAND_KICK_USAGE"));
				return true;
			} else if (pc.getPlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_OFFLINE_PLAYER"));
				return true;
			} else {
				toKick = pc.getPlayerExact(args[0]);
				PlayerClass = PlayerDataCache.getInstance(toKick.getUniqueId().toString());
			}
			PL = new PlayerLanguage(pc.getUUID(args[0]));
			if (sender instanceof ConsoleCommandSender) {
				dbc.kickUser(toKick.getUniqueId().toString(), "CONSOLE", Utils.makeString(args, 1));
				pc.getPlayerExact(args[0]).kickPlayer(
						this.formatMessage(toKick.getUniqueId().toString(), PL.languageList.get("PUNISHMENT-KICK")));
				return true;
			} else if (!sender.hasPermission(ConfigProperties.KICK_PERMISSION)) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NO_PERMISSION"));
				return true;
			} else {
				if (sender instanceof ConsoleCommandSender) {
					dbc.kickUser(toKick.getUniqueId().toString(), "CONSOLE", Utils.makeString(args, 1));
				} else {
					Player p = (Player) sender;
					dbc.kickUser(toKick.getUniqueId().toString(), p.getUniqueId().toString(),
							Utils.makeString(args, 1));

				}
			}
			pc.getPlayerExact(args[0]).kickPlayer(
					this.formatMessage(toKick.getUniqueId().toString(), PL.languageList.get("PUNISHMENT-KICK")));
			for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
				PL = new PlayerLanguage(player);
				if (player.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
					player.sendMessage(this.formatMessage(toKick.getUniqueId().toString(),
							PL.languageList.get("PUNISHMENT_KICK")));
				}
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}

	}

	private String formatMessage(String UUID2, String format) throws SQLException {
		String toFormat = format;
		if (PlayerClass.getWhoBanned().equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{0\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{0\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(PlayerClass.getWhoBanned())).getName());
		}
		toFormat = toFormat.replaceAll("\\{1\\}", Bukkit.getOfflinePlayer(UUID.fromString(UUID2)).getName());
		toFormat = toFormat.replaceAll("\\{2\\}", PlayerClass.getReason());
		return toFormat;
	}
}
