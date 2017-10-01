package com.myththewolf.MythBans.lib.player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

import net.md_5.bungee.api.ChatColor;

public class PlayerLanguage {
	public HashMap<String, String> languageList = new HashMap<String, String>();
	private FileConfiguration file;
	static boolean CON_RUN = false;
	private String[] codes = { "&4", "&c", "&6", "&e", "&2", "&a", "&b", "&3", "&1", "&9", "&d", "&5", "&f", "&7", "&8",
			"&0", "&l", "&n", "&o", "&k", "&m", "&r" };

	public PlayerLanguage(String theLang) {
		if (theLang == null || theLang.equals("")) {

			file = ConfigProperties.langMap.get("en_US");
			if (CON_RUN) {
				writeStatic();
				clearColor();
				return;
			} else {
				writeStatic();
				writeColor();
				return;
			}
		} else {
			file = ConfigProperties.langMap.get(theLang);
			if (CON_RUN) {
				writeStatic();
				clearColor();
				return;
			} else {
				writeColor();
				return;
			}
		}
	}

	public PlayerLanguage(FileConfiguration config) {
		file = config;
		writeStatic();
	}

	public PlayerLanguage() {
		this("en_US");
	}

	public PlayerLanguage(Player thePlayer) {

		this(UUIDThat(thePlayer));
	}

	public PlayerLanguage(CommandSender sender) {
		this(convert(sender));
	}

	public PlayerLanguage(OfflinePlayer toBan) {
		this(UUIDThat(toBan));
	}

	

	

	public void writeColor() {

		writeStatic();
		HashMap<String, String> NEW = new HashMap<String, String>();
		Iterator<Entry<String, String>> it = this.languageList.entrySet().iterator();
		while (it.hasNext()) {

			Entry<String, String> pair = it.next();

			NEW.put(pair.getKey().toString(), ChatColor.translateAlternateColorCodes('&', pair.getValue().toString()));
			it.remove(); // avoids a ConcurrentModificationException
		}
		this.languageList = new HashMap<String, String>(NEW);
	}

	public HashMap<String, String> clearColor() {
		System.out.println("Clearing color...");
		HashMap<String, String> NEW = new HashMap<String, String>();
		Iterator<Entry<String, String>> it = this.languageList.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pair = it.next();
			String bad = pair.getValue();
			for (String I : codes) {
				bad = bad.replaceAll(I, "");
				bad = bad.replaceAll(I.toUpperCase(), "");
				bad = bad.replaceAll(I.toLowerCase(), "");
				bad = ChatColor.stripColor(bad);
			}
			NEW.put(pair.getKey().toString(), bad);

		}
		this.languageList = null;
		this.languageList = NEW;
		// System.out.println(this.languageList);

		return NEW;
	}

	public void writeStatic() {
		// System messages
		languageList.put("ERR_NO_PERMISSION", file.getString("ERR-NO-PERMISSION"));
		languageList.put("ERR_NULL_PLAYER", file.getString("ERR-NULL-PLAYER"));
		languageList.put("ERR_NULL_IP", file.getString("ERR-NULL-IP"));
		languageList.put("ERR_NON_PLAYER", file.getString("ERR-NON-PLAYER"));
		languageList.put("ERR_INVALID_KEY", file.getString("ERR-INVALID-KEY"));
		languageList.put("ERR_OVERRIDE_CONFLICT", file.getString("ERR-OVERRIDE-CONFLICT"));

		languageList.put("ERR_DUPLICATE_USER", file.getString("ERR-DUPLICATE-USER"));
		languageList.put("ERR_OFFLINE_PLAYER", file.getString("ERR-OFFLINE-PLAYER"));
		languageList.put("ERR_NOMUTUALUSERS", file.getString("ERR-NOMUTUALUSERS"));
		// Command-Specific messages
		languageList.put("COMMAND_BAN_USAGE", file.getString("COMMAND-BAN-USAGE"));
		languageList.put("COMMAND_TEMPBAN_USAGE", file.getString("COMMAND-TEMPBAN-USAGE"));
		languageList.put("COMMAND_CREATEUI_USAGE", file.getString("COMMAND-CREATEUI-USAGE"));
		languageList.put("COMMAND_GETFAM_USAGE", file.getString("COMMAND-GETFAM-USAGE"));
		languageList.put("COMMAND_IPBAN_USAGE", file.getString("COMMAND-IPBAN-USAGE"));

		languageList.put("COMMAND_KICK_USAGE", file.getString("COMMAND-KICK-USAGE"));

		languageList.put("COMMAND_LINK_USAGE", file.getString("COMMAND-LINK-USAGE"));

		languageList.put("COMMAND_MUTE_USAGE", file.getString("COMMAND-MUTE-USAGE"));

		languageList.put("COMMAND_IPPARDON_USAGE", file.getString("COMMAND-IPPARDON-USAGE"));
		languageList.put("COMMAND_USERPARDON_USAGE", file.getString("COMMAND-USERPARDON-USAGE"));
		languageList.put("COMMAND_LINK_SUCCESS", file.getString("COMMAND-LINK-SUCCESS"));
		languageList.put("COMMAND_CHATCLEAR_SUCCESS", file.getString("COMMAND-CHATCLEAR-SUCCESS"));
		languageList.put("COMMAND_IMPORTJSON_TASKSTART", file.getString("COMMAND-IMPORTJSON-TASKSTART"));
		// Punishment messages
		languageList.put("PUNISHMENT_BAN_INFORM", file.getString("PUNISHMENT-BAN-INFORM"));
		languageList.put("PUNISHMENT_TEMPBAN_INFORM", file.getString("PUNISHMENT-TEMPBAN-INFORM"));
		languageList.put("PUNISHMENT_BAN_KICK", file.getString("PUNISHMENT-BAN-KICK"));
		languageList.put("PUNISHMENT_TEMPBAN_KICK", file.getString("PUNISHMENT-TEMPBAN-KICK"));
		languageList.put("PUNISHMENT_IPBAN_INFORM", file.getString("PUNISHMENT-IPBAN-INFORM"));
		languageList.put("PUNISHMENT_IPBAN_KICK", file.getString("PUNISHMENT-IPBAN-KICK"));

		languageList.put("PUNISHMENT_KICK_INFORM", file.getString("PUNISHMENT-KICK-INFORM"));
		languageList.put("PUNISHMENT_KICK", file.getString("PUNISHMENT-KICK"));

		languageList.put("PUNISHMENT_MUTE_INFORM", file.getString("PUNISHMENT-MUTE-INFORM"));
		languageList.put("PUNISHMENT_MUTE_PLAYER", file.getString("PUNISHMENT-MUTE-PLAYER"));

		languageList.put("PUNISHMENT_UNMUTE_INFORM", file.getString("PUNISHMENT-UNMUTE-INFORM"));
		languageList.put("PUNISHMENT_UNMUTE_PLAYER", file.getString("PUNISHMENT-UNMUTE-PLAYER"));
		languageList.put("PUNISHMENT_IPPARDON", file.getString("PUNISHMENT-IPPARDON"));
		languageList.put("PUNISHMENT_USERPARDON", file.getString("PUNISHMENT-USERPARDON"));
		// Data tapback
		languageList.put("MUTUAL_USERS", file.getString("MUTUAL-USERS"));

	}

	public HashMap<String, String> getList() {
		return this.languageList;
	}

	private static String UUIDThat(Player p) {
		com.myththewolf.MythBans.lib.player.MythPlayer pClass = new com.myththewolf.MythBans.lib.player.MythPlayer(
				p.getUniqueId().toString());
		if (pClass.getLang() == null) {
			return ConfigProperties.SYSTEM_LOCALE;
		} else {
			return pClass.getLang();
		}

	}

	private static String UUIDThat(OfflinePlayer p) {

		com.myththewolf.MythBans.lib.player.MythPlayer pClass = new com.myththewolf.MythBans.lib.player.MythPlayer(
				p.getUniqueId().toString());
		if (pClass.getLang() == null) {
			return ConfigProperties.SYSTEM_LOCALE;
		} else {
			return pClass.getLang();
		}
	}

	private static String convert(CommandSender sender) {
		if (sender instanceof ConsoleCommandSender) {
			ConfigProperties.PREFIX = ChatColor.stripColor(ConfigProperties.PREFIX);
			CON_RUN = true;
			return ConfigProperties.SYSTEM_LOCALE;
		} else {
			CON_RUN = false;
			ConfigProperties.PREFIX = ConfigProperties.PREFIX_UNALTERED;
			return UUIDThat((Player) sender);
		}
	}
}
