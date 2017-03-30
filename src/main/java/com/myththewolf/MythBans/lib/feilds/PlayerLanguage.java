package com.myththewolf.MythBans.lib.feilds;

import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class PlayerLanguage {
	public HashMap<String, String> languageList = new HashMap<String, String>();

	public PlayerLanguage(String theLang) {
		YamlConfiguration file = ConfigProperties.langMap.get(theLang);
		languageList.put("ERR_NO_PERMISSION", ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NO-PERMISSION")));
		languageList.put("ERR_NULL_PLAYER",ChatColor.translateAlternateColorCodes('&', file.getString("ERR-NULL-PLAYER")));
	}

	private String replaceString(String in, String zero, String one, String two, String three, String four) {
		String fin = in;
		fin = fin.replaceAll("\\{0\\}", zero);
		fin = fin.replaceAll("\\{1\\}", one);
		fin = fin.replaceAll("\\{2\\}", two);
		fin = fin.replaceAll("\\{3\\}", three);
		fin = fin.replaceAll("\\{4\\}", four);
		return fin;
	}
}
