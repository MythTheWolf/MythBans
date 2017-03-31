package com.myththewolf.MythBans.lib.feilds;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;

public class PlayerLanguage {
	public HashMap<String, String> languageList = new HashMap<String, String>();

	public PlayerLanguage(String theLang) {
		System.out.println(theLang);
		FileConfiguration file = ConfigProperties.langMap.get(theLang);
		// System messages
		languageList.put("ERR_NO_PERMISSION",
				ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NO-PERMISSION")));
		languageList.put("ERR_NULL_PLAYER",
				ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NULL-PLAYER")));
		languageList.put("ERR_NULL_IP", ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NULL-IP")));
		languageList.put("ERR_NON_PLAYER",
				ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NON-PLAYER")));
		languageList.put("ERR_DUPLICATE_USER",
				ChatColor.translateAlternateColorCodes('&', file.getString("ERR-DUPLICATE-USER")));
		languageList.put("ERR_NOMUTUALUSERS",
				ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NOMUTUALUSERS")));
		// Command-Specific messages
		languageList.put("COMMAND_BAN_USAGE",
				ChatColor.translateAlternateColorCodes('&', file.getString("COMMAND-BAN-USAGE")));
		languageList.put("COMMAND_CREATEUI_USAGE",
				ChatColor.translateAlternateColorCodes('&', file.getString("COMMAND-CREATEUI-USAGE")));
		languageList.put("COMMAND_GETFAM_USAGE",
				ChatColor.translateAlternateColorCodes('&', file.getString("COMMAND-GETFAM-USAGE")));
		// Punishment messages
		languageList.put("PUNISHMENT_BAN_INFORM",
				ChatColor.translateAlternateColorCodes('&', file.getString("PUNISHMENT-BAN-INFORM")));
		languageList.put("PUNISHMENT_BAN_KICK",
				ChatColor.translateAlternateColorCodes('&', file.getString("PUNISHMENT-BAN-KICK")));
		//Data tapback
		languageList.put("MUTUAL_USERS",
				ChatColor.translateAlternateColorCodes('&', file.getString("MUTUAL-USERS")));
	}

	public PlayerLanguage() {
		this("en_US");
	}

}
