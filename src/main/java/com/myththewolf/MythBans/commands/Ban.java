package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.PlayerLanguage;
import com.myththewolf.MythBans.lib.tool.Utils;

public class Ban implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();
	private OfflinePlayer toBan;
	private String toUUID;
	private MythPlayer PlayerClass;
	private PlayerLanguage PL;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		try {
			PL = new PlayerLanguage(sender);
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.getList().get("COMMAND_BAN_USAGE"));
				return true;
			} else if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.getList().get("ERR_NULL_PLAYER"));
				return true;
			} else if (!sender.hasPermission(ConfigProperties.BAN_PERMISSION)) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.getList().get("ERR_NO_PERMISSION"));
				return true;
			} else {
				toBan = pCache.getOfflinePlayerExact(args[0]);

				PlayerClass = DataCache.getPlayerInstance(toBan.getUniqueId().toString());
				if (sender instanceof ConsoleCommandSender) {
					String reason = Utils.makeString(args, 1);
					dbc.banUser(pCache.getUUID(args[0]), "CONSOLE", reason);
					DataCache.rebuildUser(pCache.getUUID(args[0]));
					toUUID = pCache.getUUID(args[0]);
				} else {
					String reason = Utils.makeString(args, 1);
					org.bukkit.entity.Player by = (org.bukkit.entity.Player) sender;
					dbc.banUser(pCache.getUUID(args[0]), by.getUniqueId().toString(), reason);
					DataCache.rebuildUser(pCache.getUUID(args[0]));
					toUUID = pCache.getUUID(args[0]);
				}
			}
			PlayerClass = DataCache.getPlayerInstance(toUUID);
			for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
				PL = new PlayerLanguage(player);
				if (player.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&',
							this.formatMessage(toUUID, PL.getList().get("PUNISHMENT_BAN_INFORM"))));
				}
			}
			if (toBan.isOnline()) {
				PL = new PlayerLanguage(toBan);
				toBan.getPlayer().kickPlayer(this.formatMessage(toUUID, PL.getList().get("PUNISHMENT_BAN_KICK")));
			}
		} catch (

		SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private String formatMessage(String UUID2, String format) throws SQLException {
		String toFormat = format;
		if (PlayerClass.getWhoBanned().equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{0\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{0\\}", pCache.getName(PlayerClass.getWhoBanned()));
		}

		toFormat = toFormat.replaceAll("\\{1\\}", pCache.getName(UUID2));
		toFormat = toFormat.replaceAll("\\{2\\}", PlayerClass.getReason());

		return toFormat;
	}
}
