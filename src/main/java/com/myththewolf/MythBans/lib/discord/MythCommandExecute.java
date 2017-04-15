package com.myththewolf.MythBans.lib.discord;

import org.bukkit.OfflinePlayer;

import de.btobastian.javacord.entities.User;

public interface MythCommandExecute {
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args);
}
