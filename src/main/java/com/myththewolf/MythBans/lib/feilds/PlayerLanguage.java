package com.myththewolf.MythBans.lib.feilds;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;

public class PlayerLanguage {
	public HashMap<String, String> languageList = new HashMap<String, String>();
	private FileConfiguration file;
	public PlayerLanguage(String theLang) {
	file = ConfigProperties.langMap.get(theLang);
	writeColor();
	}
	public PlayerLanguage(FileConfiguration config){
		file = config;
	}
	public PlayerLanguage() {
		this("en_US");
	}
	public void writeColor(){
		// System messages
				languageList.put("ERR_NO_PERMISSION",
						ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NO-PERMISSION")));
				languageList.put("ERR_NULL_PLAYER",
						ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NULL-PLAYER")));
				languageList.put("ERR_NULL_IP", ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NULL-IP")));
				languageList.put("ERR_NON_PLAYER",
						ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NON-PLAYER")));
				languageList.put("ERR_INVALID_KEY",
						ChatColor.translateAlternateColorCodes('&', file.getString("ERR-INVALID-KEY")));

				
				languageList.put("ERR_DUPLICATE_USER",
						ChatColor.translateAlternateColorCodes('&', file.getString("ERR-DUPLICATE-USER")));
				languageList.put("ERR_OFFLINE_PLAYER",
						ChatColor.translateAlternateColorCodes('&', file.getString("ERR-OFFLINE-PLAYER")));
				languageList.put("ERR_NOMUTUALUSERS",
						ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NOMUTUALUSERS")));
				// Command-Specific messages
				languageList.put("COMMAND_BAN_USAGE",
						ChatColor.translateAlternateColorCodes('&', file.getString("COMMAND-BAN-USAGE")));
				languageList.put("COMMAND_CREATEUI_USAGE",
						ChatColor.translateAlternateColorCodes('&', file.getString("COMMAND-CREATEUI-USAGE")));
				languageList.put("COMMAND_GETFAM_USAGE",
						ChatColor.translateAlternateColorCodes('&', file.getString("COMMAND-GETFAM-USAGE")));
				languageList.put("COMMAND_IPBAN_USAGE",
						ChatColor.translateAlternateColorCodes('&', file.getString("COMMAND-IPBAN-USAGE")));

				languageList.put("COMMAND_KICK_USAGE",
						ChatColor.translateAlternateColorCodes('&', file.getString("COMMAND-KICK-USAGE")));

				languageList.put("COMMAND_LINK_USAGE",
						ChatColor.translateAlternateColorCodes('&', file.getString("COMMAND-LINK-USAGE")));
				
				languageList.put("COMMAND_LINK_SUCCESS",
						ChatColor.translateAlternateColorCodes('&', file.getString("COMMAND-LINK-SUCCESS")));
				
				// Punishment messages
				languageList.put("PUNISHMENT_BAN_INFORM",
						ChatColor.translateAlternateColorCodes('&', file.getString("PUNISHMENT-BAN-INFORM")));
				languageList.put("PUNISHMENT_BAN_KICK",
						ChatColor.translateAlternateColorCodes('&', file.getString("PUNISHMENT-BAN-KICK")));

				languageList.put("PUNISHMENT_IPBAN_INFORM",
						ChatColor.translateAlternateColorCodes('&', file.getString("PUNISHMENT-IPBAN-INFORM")));
				languageList.put("PUNISHMENT_IPBAN_KICK",
						ChatColor.translateAlternateColorCodes('&', file.getString("PUNISHMENT-IPBAN-KICK")));

				languageList.put("PUNISHMENT_KICK_INFORM",
						ChatColor.translateAlternateColorCodes('&', file.getString("PUNISHMENT-KICK-INFORM")));
				languageList.put("PUNISHMENT_KICK",
						ChatColor.translateAlternateColorCodes('&', file.getString("PUNISHMENT-KICK")));
				// Data tapback
				languageList.put("MUTUAL_USERS", ChatColor.translateAlternateColorCodes('&', file.getString("MUTUAL-USERS")));
	}
}
