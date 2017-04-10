package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerLanguage;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class ClearChat implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());

	private OfflinePlayer toBan;
	private String toUUID;
	private com.myththewolf.MythBans.lib.player.Player PlayerClass = new com.myththewolf.MythBans.lib.player.Player();
	private PlayerLanguage PL;
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			PL = new PlayerLanguage();
			sender.sendMessage(PL.languageList.get("ERR_NON_PLAYER"));
			return true;
		} else {
			Player tmp = (Player) sender;
			try {
				PL = new PlayerLanguage(PlayerClass.getLang(tmp.getUniqueId().toString()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			if(!sender.hasPermission("mythbans.clearchat")){
				sender.sendMessage(PL.languageList.get("ERR_NO_PERMISSION"));
				return false;
			}
			for(Player P : Bukkit.getOnlinePlayers()){
				for(int i =0; i<100; i++){
					P.sendMessage("");
				}
				P.sendMessage(ConfigProperties.PREFIX+"Chat has been cleared");
			}
		}
		return true;
	}



}
