package com.myththewolf.MythBans.lib.discord;

import org.bukkit.OfflinePlayer;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public interface MythCommandExecute {
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args,Message theMessage);
}
