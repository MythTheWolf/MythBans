/**

 * 
 */
package com.myththewolf.MythBans.commands;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerDataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Date;
import com.myththewolf.MythBans.lib.tool.Utils;

import net.md_5.bungee.api.ChatColor;

public class TempBan implements CommandExecutor {
	private DatabaseCommands dbc = new DatabaseCommands();
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private com.myththewolf.MythBans.lib.tool.Date date = new Date();
	private MythPlayer PlayerClass;
	private OfflinePlayer p;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 2) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /tempban <user> <time> [reason]");
				return true;
			} else if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has never played on this server.");
				return true;

			} else if (!sender.hasPermission(ConfigProperties.TEMPBAN_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			} else {
				PlayerClass = PlayerDataCache.getInstance(pCache.getUUID(args[0]));
				final PeriodFormatter format = new PeriodFormatterBuilder().appendDays().appendSuffix("d").appendWeeks()
						.appendSuffix("w").appendMonths().appendSuffix("mon").appendYears().appendSuffix("y")
						.appendMinutes().appendSuffix("m").appendSeconds().appendSuffix("s").appendHours()
						.appendSuffix("h").toFormatter();
				try {
					long milli = format.parsePeriod(args[1]).toStandardDuration().getMillis();
					java.util.Date finalDate = new java.util.Date(System.currentTimeMillis() + milli);
					String dateStr = date.formatDate(finalDate);
					String reason = Utils.makeString(args, 2);
					String UUID = pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString();
					if (sender instanceof org.bukkit.entity.Player) {
						String byUUID = ((org.bukkit.entity.Player) sender).getUniqueId().toString();
						dbc.tmpBanUser(UUID, byUUID, reason, dateStr);
						p = pCache.getOfflinePlayerExact(args[0]);
					} else {
						dbc.tmpBanUser(UUID, "CONSOLE", reason, dateStr);
						p = pCache.getOfflinePlayerExact(args[0]);
					}
					for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
						if (player.hasPermission(ConfigProperties.VIEWMSG_PERM)) {
							player.sendMessage(this.formatMessage(p.getUniqueId().toString(),
									ConfigProperties.SERVER_TEMPBAN_FORMAT));
						}
					}
					if (p.isOnline()) {
						p.getPlayer().kickPlayer(
								this.formatMessage(p.getUniqueId().toString(), ConfigProperties.USER_TEMPBAN_FORMAT));
					}
					return true;
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

	private String formatMessage(String UUID2, String format) throws SQLException {
		String toFormat = format;
		if (PlayerClass.getWhoBanned().equals("CONSOLE")) {
			toFormat = toFormat.replaceAll("\\{staffMember\\}", "CONSOLE");
		} else {
			toFormat = toFormat.replaceAll("\\{staffMember\\}",
					Bukkit.getOfflinePlayer(UUID.fromString(PlayerClass.getWhoBanned())).getName());
		}

		toFormat = toFormat.replaceAll("\\{culprit\\}", Bukkit.getOfflinePlayer(UUID.fromString(UUID2)).getName());
		toFormat = toFormat.replaceAll("\\{reason\\}", PlayerClass.getReason());
		toFormat = toFormat.replaceAll("\\{expire\\}", PlayerClass.getExpireDate().toString());
		return toFormat;
	}
}
