package com.myththewolf.MythBans.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class Potato implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /potato <user> [reason]");
				return true;
			} else if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not been on this server.");
				return true;
			} else if (!sender.hasPermission(ConfigProperties.BAN_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			} else {
				if(!pCache.getOfflinePlayerExact(args[0]).isOnline()){
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player must be online");
				}else{
					Location l = new Location(Bukkit.getServer()
							.getWorld("world"), -9999.0, -9999.0, -9999.0);
					Player thePlayer = pCache.getOfflinePlayerExact(args[0]).getPlayer();
					thePlayer.setInvulnerable(true);
					Location ORG = thePlayer.getLocation();
					ItemStack pot = new ItemStack(Material.POTATO_ITEM);
					ItemMeta m = pot.getItemMeta();
					List<String> lore= new ArrayList<String>();
					lore.add("Edible " + thePlayer.getName());
					lore.add("PotatoPlayer");
					lore.add(thePlayer.getUniqueId().toString());
					m.setDisplayName(thePlayer.getName());
					m.setLore(lore);
					pot.setItemMeta(m);
					thePlayer.getWorld().dropItem(ORG, pot);
		
					thePlayer.sendMessage(ConfigProperties.PREFIX+ "To clarify, you have been turned into a potato.");
					Player send = (Player) sender;
					send.setGameMode(GameMode.SURVIVAL);
					send.setExhaustion(3);
					send.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "Hint: Eat the potato");
					thePlayer.teleport(l);
					thePlayer.setInvulnerable(false);
					
					
				}
			}
				
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
