package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerDataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.PlayerLanguage;
import com.myththewolf.MythBans.lib.tool.Date;

public class PlayerTime implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private PlayerLanguage PL;
	private Date mythDate = new Date();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			PL = new PlayerLanguage(sender);
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("COMMAND_PLAYERTIME_USAGE"));
				return true;
			}
			if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NULL_PLAYER"));
				return true;
			}
			OfflinePlayer p = pCache.getOfflinePlayerExact(args[0]);
			MythPlayer MP = PlayerDataCache.getInstance(p.getUniqueId().toString());
			long init;
			String PD = "ERROR";
			if (!p.isOnline()) {
				init = MP.getPlayTime();
				PD = mythDate.convertToPd(init);
			} else {
				init = p.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK);
				PD = mythDate.convertToPd((init / 20) * 1000);
			}
			
			sender.sendMessage(ConfigProperties.PREFIX + "User has play time of " + PD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
