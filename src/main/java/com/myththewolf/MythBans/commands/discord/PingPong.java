package com.myththewolf.MythBans.commands.discord;

import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.discord.MythCommandExecute;

import de.btobastian.javacord.entities.User;

public class PingPong implements MythCommandExecute{

	public PingPong() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args) {
		theDiscordUser.sendMessage("PONG~");
		
	}

}
