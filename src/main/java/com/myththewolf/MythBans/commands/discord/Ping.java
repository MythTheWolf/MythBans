package com.myththewolf.MythBans.commands.discord;

import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.discord.DiscordCommand;
import com.myththewolf.MythBans.lib.discord.MythCommandExecute;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class Ping implements MythCommandExecute{

	public Ping() {
		// TODO Auto-generated constructor stub
	}
	@Override
	@DiscordCommand(requiresLinked=false,requiresRoot=false)
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage) {
		theMessage.reply("Pong!");
	}


	

}
