package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.discord.MythDiscordBot;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.PlayerLanguage;

public class Link implements CommandExecutor {
	private PlayerCache PC = new PlayerCache(MythSQLConnect.getConnection());
	private PlayerLanguage PL;
	private MythDiscordBot MDB;

	public Link(MythDiscordBot i) {
		MDB = i;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		try {
			if (sender instanceof ConsoleCommandSender) {
				PL = new PlayerLanguage();
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NON_PLAYER"));
				return true;
			} else {
				PL = new PlayerLanguage(sender);
			}
			if (!sender.hasPermission("mythbans.discordlink")) {
				sender.sendMessage(ConfigProperties.PREFIX + PL.languageList.get("ERR_NO_PERMISSION"));
				return true;
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
				System.out.println(MDB.getServerID());
				MDB.linkUser(PC.getDiscordID(UUID));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
