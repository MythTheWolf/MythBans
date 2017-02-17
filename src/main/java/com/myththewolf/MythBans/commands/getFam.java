package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.IP;
import com.myththewolf.MythBans.lib.player.PlayerCache;

import net.md_5.bungee.api.ChatColor;

public class getFam implements CommandExecutor{
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private IP ipClass = new IP();
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (!sender.hasPermission(ConfigProperties.BANIP_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You don't have permission to execute this command.");
				return true;
			}
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /getfam <user|ip>");
				return true;
			}
			if (args[0].charAt(0) == '/' && pCache.ipExist(args[0]) == false) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "IP Not found.");
				return true;
			}
			if (args[0].charAt(0) != '/' && pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player Not found.");
				return true;
			}
			if (args[0].charAt(0) != '/') {
				if(!pCache.getOfflinePlayerExact(args[0]).isOnline()){
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player not online");
					return true;
				}else{
					Player p = Bukkit.getPlayer(UUID.fromString(pCache.getUUID(args[0])));
					String[] fam = ipClass.getTheFam(p.getAddress().getAddress().toString(),"BOOOOOOOOOOOOOOOOOOOOOOGUUUUUUUSSSSS");
					if(fam == null){
						sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "No users sharing this ip.");
						return true;
					}
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + " Users sharing this ip: " + Arrays.toString(fam));
					return true;
				}
			}else{
				String[] fam = ipClass.getTheFam(args[0],"BOOOOOOOOOOOOOOOOOOOOOOGUUUUUUUSSSSS");
				if(fam == null){
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "No users sharing this ip.");
					return true;
				}
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + " Users sharing this ip: " + Arrays.toString(fam));
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return true;
	}

}
