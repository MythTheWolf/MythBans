/**

 * 
 */
package com.myththewolf.MythBans.commands;

import java.sql.SQLException;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Player;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Date;
import com.myththewolf.MythBans.lib.tool.Utils;

import net.md_5.bungee.api.ChatColor;

public class TempBan implements CommandExecutor {
	private DatabaseCommands dbc = new DatabaseCommands();
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private com.myththewolf.MythBans.lib.tool.Date date = new Date();
	private String toFormat = "";
	private com.myththewolf.MythBans.lib.player.Player PlayerClass = new Player();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has never played on this server.");
				return true;
			} else if(args.length >= 2){
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /tempban <user> <time> [reason]");
				return true;
			}else if(!sender.hasPermission(ConfigProperties.TEMPBAN_PERMISSION)){
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			}else{
				final PeriodFormatter format = new PeriodFormatterBuilder().appendDays().appendSuffix("d").appendWeeks()
						.appendSuffix("w").appendMonths().appendSuffix("mon").appendYears().appendSuffix("y")
						.appendMinutes().appendSuffix("m").appendSeconds().appendSuffix("s").appendHours()
						.appendSuffix("h").toFormatter();
				try {
					long milli = format.parsePeriod(args[1]).toStandardDuration().getMillis();
					java.util.Date finalDate = new java.util.Date(System.currentTimeMillis() + milli);
					String dateStr = date.formatDate(finalDate);
					String reason = Utils.makeString(args, 3);
					String UUID = pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString();
					if (sender instanceof org.bukkit.entity.Player) {
						String byUUID = ((org.bukkit.entity.Player) sender).getUniqueId().toString();
						dbc.tmpBanUser(UUID, byUUID, reason, dateStr);
						pCache.getOfflinePlayerExact(args[0]).getPlayer().kickPlayer(this.formatMessage(UUID,
								"tempBanned", pCache.getOfflinePlayerExact(args[0]).getName()));
						return true;
					} else {
						dbc.tmpBanUser(UUID, "CONSOLE", reason, dateStr);
						pCache.getOfflinePlayerExact(args[0]).getPlayer().kickPlayer(this.formatMessage(UUID,
								"tempBanned", pCache.getOfflinePlayerExact(args[0]).getName()));
						return true;
					}
				} catch (Exception e) {
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Invalid date string: " + args[1]);
					e.printStackTrace();
					return true;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

	private String formatMessage(String UUID, String key, String name) throws SQLException {

		switch (key) {
		case "ban":
			toFormat = ConfigProperties.USER_BAN_FORMAT;
			break;
		case "tempBanned":
			toFormat = ConfigProperties.USER_TEMPBAN_FORMAT;
			toFormat = toFormat.replaceAll("\\{expire\\}", PlayerClass.getExpireDate(UUID).toString());
			break;
		default:
			break;
		}
		toFormat = toFormat.replaceAll("\\{staffMember\\}", PlayerClass.getWhoBanned(UUID));
		toFormat = toFormat.replaceAll("\\{culprit\\}", name);
		toFormat = toFormat.replaceAll("\\{reason\\}", PlayerClass.getReason(UUID));

		return toFormat;
	}
}
