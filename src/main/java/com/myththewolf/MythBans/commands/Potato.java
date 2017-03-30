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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

public class Potato implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private JavaPlugin PL;

	public Potato(JavaPlugin pl) {
		PL = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /potato <user> [reason]");
				return true;
			}
			if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not been on this server.");
				return true;
			}
			if (!sender.hasPermission(ConfigProperties.POTATO_PERM)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			} else {
				if (!pCache.getOfflinePlayerExact(args[0]).isOnline()) {
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player must be online");
				} else {
					Location l = new Location(Bukkit.getServer().getWorld("world"), -9999.0, -5000.0, -9999.0);
					Player thePlayer = pCache.getOfflinePlayerExact(args[0]).getPlayer();
					thePlayer.setInvulnerable(false);
					Location ORG = thePlayer.getLocation();

					thePlayer.sendMessage(ConfigProperties.PREFIX + "To clarify, you have been turned into a potato.");
					if (sender instanceof ConsoleCommandSender) {
						sender.sendMessage("You must be a player to use this command");
						return true;
					} else {
						ItemStack pot = new ItemStack(Material.POTATO_ITEM);
						ItemMeta m = pot.getItemMeta();
						List<String> lore = new ArrayList<String>();
						lore.add("Edible " + thePlayer.getName());
						lore.add("PotatoPlayer");
						lore.add(thePlayer.getUniqueId().toString());
						lore.add(Utils.serializeLocation(ORG));
						m.setDisplayName(thePlayer.getName());
						m.setLore(lore);
						pot.setItemMeta(m);
						thePlayer.teleport(l);
						thePlayer.setMetadata("is_potato", new FixedMetadataValue(PL, 0));
						Player send = (Player) sender;
						send.setGameMode(GameMode.SURVIVAL);
						send.setFoodLevel(10);
						send.getInventory().addItem(pot);
						send.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "Hint: Eat the potato");

					}

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
