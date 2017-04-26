package com.myththewolf.MythBans.commands.discord;

import java.util.concurrent.ExecutionException;

import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.discord.DiscordCommand;
import com.myththewolf.MythBans.lib.discord.MythCommandExecute;
import com.myththewolf.MythBans.lib.discord.MythDiscordBot;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class ClearLog implements MythCommandExecute{
	private MythDiscordBot bot;
	public ClearLog(MythDiscordBot BOT) {
		bot = BOT;
	}

	@Override
	@DiscordCommand(requiresLinked=false,requiresRoot=true)
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage) {
			try {
				bot.getConsoleThread().edit("[MythBansBot]Cleared log!");
		
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}


}
