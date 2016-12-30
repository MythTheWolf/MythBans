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
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Date;
import com.myththewolf.MythBans.lib.tool.Utils;

import net.md_5.bungee.api.ChatColor;

public class TempBan implements CommandExecutor {
	private DatabaseCommands dbc = new DatabaseCommands();
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private com.myththewolf.MythBans.lib.tool.Date date = new Date();
	private java.util.Date now;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
	{
		try
		{
			if (pCache.getOfflinePlayerExact(args[0]) == null)
			{
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has never played on this server.");
				return true;
			} else
			{
				final PeriodFormatter format = new PeriodFormatterBuilder().appendDays().appendSuffix("d").appendWeeks()
						.appendSuffix("w").appendMonths().appendSuffix("mon").appendYears().appendSuffix("y")
						.appendMinutes().appendSuffix("m").appendSeconds().appendSuffix("s").appendHours()
						.appendSuffix("h").toFormatter();
				try
				{
					long milli = format.parsePeriod(args[1]).toStandardDuration().getMillis();
					java.util.Date finalDate = new java.util.Date(now.getTime() + milli);
					String dateStr = date.formatDate(finalDate);
					String reason = Utils.makeString(args, 3);
					String UUID = pCache.getOfflinePlayerExact(args[0]).getUniqueId().toString();
					if(sender instanceof org.bukkit.entity.Player)
					{
						String byUUID = ((org.bukkit.entity.Player) sender).getUniqueId().toString();
						dbc.tmpBanUser(UUID, byUUID, reason, dateStr);
						return true;
					}else{
						dbc.tmpBanUser(UUID, ConfigProperties.CONSOLE_UUID, reason, dateStr);
						return true;
					}
				} catch (Exception e)
				{
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED +"Invalid date string: " + args[1]);
					e.printStackTrace();
					return true;
				}
			}
		
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

}
