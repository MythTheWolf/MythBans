package com.myththewolf.MythBans.lib.discord;

import org.bukkit.OfflinePlayer;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

/**
 * @author MythTheWolf
 *
 */
public interface MythCommandExecute {

	/**
	 * The method run on a valid command
	 * @param theDiscordUser - The JavaCord User object of the message sender
	 * @param theBukkitUser - The OfflinePlayer object of the User's linked account (null if discord isnt linked)
	 * @param args - Arguments to the command
	 * @param theMessage - The unmodified message. Note that if  the deleteMessage annoation is true, this will be NULL
	 */
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage);
}
