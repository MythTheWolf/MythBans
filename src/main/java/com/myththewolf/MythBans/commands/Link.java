package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerLanguage;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class Link implements CommandExecutor {
	private PlayerCache PC = new PlayerCache(MythSQLConnect.getConnection());
	private PlayerLanguage PL;
	private com.myththewolf.MythBans.lib.player.Player pClass = new com.myththewolf.MythBans.lib.player.Player();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		try {
			if (sender instanceof ConsoleCommandSender) {
				PL = new PlayerLanguage();
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NON_PLAYER"));
				return true;
			} else {
				PL = new PlayerLanguage(pClass.getLang(((Player) sender).getUniqueId().toString()));
			}
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("COMMAND_LINK_USAGE"));
				return true;
			}
			
			if (!PC.secretExists(args[0])) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_INVALID_KEY"));
				return true;
			} else {
				String UUID = ((Player) sender).getUniqueId().toString();
				PC.linkDiscord(args[0], UUID);
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("COMMAND_LINK_SUCCESS"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
