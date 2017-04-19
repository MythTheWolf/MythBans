package com.myththewolf.MythBans.lib.discord;

import org.bukkit.OfflinePlayer;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

/**
 * @author MythTheWolf
 *
 */
public interface MythCommandExecute {


	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage);
	/**
	 * 
	 * @return If the command requires a linked account
	 *
	 */
	public boolean requiresRoot();
	/**
	 * 
	 * @return If the command requires a root account
	 *
	 */
	public boolean requiresLinked();
}
