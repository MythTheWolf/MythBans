package com.myththewolf.MythBans.lib.player.events;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.discord.MythDiscordBot;
import com.myththewolf.MythBans.lib.feilds.AbstractMaps;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerDataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;

public class PlayerChat implements Listener {

	private DatabaseCommands dbc = new DatabaseCommands();
	private MythDiscordBot MDB;

	public PlayerChat(MythDiscordBot mBD2) {
		MDB = mBD2;
	}

	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent e)
			throws SQLException, InterruptedException, ExecutionException {

		org.bukkit.entity.Player p = e.getPlayer();

		MythPlayer playerClass = PlayerDataCache.getInstance(p.getUniqueId().toString());

		if (e.getMessage().equalsIgnoreCase(ConfigProperties.SOFTMUTE_RELEASE_COMMAND) && !(playerClass.isOverride())) {
			MythPlayer.setOverride(p.getUniqueId().toString(), true);
			playerClass.setStatus("OK");
			dbc.cleanUser(p.getUniqueId().toString());
			p.sendMessage(ConfigProperties.PREFIX + ChatColor.GREEN + "You may now speak!");
			e.setCancelled(true);
			PlayerDataCache.rebuildUser(p.getUniqueId().toString());
			return;
		}
		if (playerClass.getStatus().equals("muted")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigProperties.PREFIX)
					+ "Your voice has been silenced!");
			e.setCancelled(true);
		} else if (dbc.getIPStatus(e.getPlayer().getAddress().getAddress().toString()).equals("muted")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigProperties.PREFIX)
					+ "Your voice has been silenced!");
			e.setCancelled(true);
			return;
		}
		String m = e.getMessage();

		if (m.charAt(0) == '#' && e.getPlayer().hasPermission(ConfigProperties.STAFF_CHAT_SEND)) {
			for (org.bukkit.entity.Player pp : Bukkit.getOnlinePlayers()) {
				String orig = e.getMessage();
				if (pp.hasPermission(ConfigProperties.STAFF_CHAT_GET)) {
					m = e.getPlayer().getDisplayName() + ": " + orig.substring(1);
					pp.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&8[&4#!STAFF&8]&6" + ChatColor.GOLD + ChatColor.ITALIC + m));
					Location location = pp.getLocation();
					//pp.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
				}
			}
			e.setCancelled(true);
			return;
		}

		if (m.charAt(0) == '!' && e.getPlayer().hasPermission(ConfigProperties.IMPORTANT_SEND)) {
			for (org.bukkit.entity.Player pp : Bukkit.getOnlinePlayers()) {
				String orig = e.getMessage();
				m = e.getPlayer().getDisplayName() + ": " + orig.replaceAll("!", "");
				pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&b#!IMPORTANT&8]&6" + m));
				Location location = pp.getLocation();
				pp.getWorld().playSound(location, Sound.BLOCK_PISTON_EXTEND, 1, 1);
			}
			e.setCancelled(true);
			return;
		}
		if (playerClass.getStatus().equals("softmuted")) {
			org.bukkit.entity.Player PP = e.getPlayer();
			String who = PP.getDisplayName();
			String message = e.getMessage();
			if (!AbstractMaps.new_join_count.containsKey(e.getPlayer().getUniqueId().toString())) {
				AbstractMaps.new_join_count.put(e.getPlayer().getUniqueId().toString(), 0);
			} else if (AbstractMaps.new_join_count.get(e.getPlayer().getUniqueId().toString()) > 2
					&& AbstractMaps.read_rules.get(e.getPlayer().getUniqueId().toString())) {
				e.getPlayer().sendMessage(ConfigProperties.PREFIX + "Buddy! Read /rules, your chat perms are limited!");
			} else if (AbstractMaps.new_join_count.get(e.getPlayer().getUniqueId().toString()) > 20) {
				e.getPlayer().sendMessage(ConfigProperties.PREFIX + "Buddy! Read /rules, your chat perms are limited!");
			} else {
				AbstractMaps.new_join_count.put(e.getPlayer().getUniqueId().toString(),
						AbstractMaps.new_join_count.get(e.getPlayer().getUniqueId().toString()) + 1);
			}
			if (e.getPlayer().hasPermission("essentials.chat.color")) {
				PP.sendMessage(ChatColor.WHITE + "<" + who + ChatColor.WHITE + "> "
						+ ChatColor.translateAlternateColorCodes('&', message));
			} else {
				PP.sendMessage(ChatColor.WHITE + "<" + who + ChatColor.WHITE + "> " + message);
			}
			for (org.bukkit.entity.Player I : Bukkit.getOnlinePlayers()) {
				if (I.hasPermission(ConfigProperties.STAFF_CHAT_GET)) {
					String message1 = ChatColor.stripColor(e.getMessage());
					String who1 = PP.getName();
					I.sendMessage(ChatColor.GRAY + "[SOFTMUTED: " + who1 + "] " + message1);
				}
			}
			e.setCancelled(true);
			return;
		}
		if (playerClass.getProbate()) {
			e.setCancelled(true);
			String message = e.getMessage();
			for (org.bukkit.entity.Player i : Bukkit.getOnlinePlayers()) {
				String who = ChatColor.translateAlternateColorCodes('&', e.getPlayer().getDisplayName());
				if (i.hasPermission(ConfigProperties.VIEW_PROBATION_PERMISSION)) {
					if (e.getPlayer().hasPermission("essentials.chat.color")) {

						i.sendMessage(ChatColor.RED + " * " + ChatColor.WHITE + "<" + who + ChatColor.WHITE + "> "
								+ ChatColor.translateAlternateColorCodes('&', message));
					} else {
						i.sendMessage(ChatColor.RED + " * " + ChatColor.WHITE + "<" + who + ChatColor.WHITE + " > "
								+ message);
					}
				} else {
					if (e.getPlayer().hasPermission("essentials.chat.color")) {
						i.sendMessage(ChatColor.WHITE + "<" + who + ChatColor.WHITE + "> "
								+ ChatColor.translateAlternateColorCodes('&', message));
					} else {
						i.sendMessage(ChatColor.WHITE + "<" + who + ChatColor.WHITE + "> " + message);
					}
				}
			}
		}
		if (ConfigProperties.use_bot && MDB.isSetup()) {
			String message = ChatColor.stripColor(e.getMessage());
			MDB.appendThread("\n" + e.getPlayer().getName() + ": " + message);
		}
	}
}