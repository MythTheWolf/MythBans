package com.myththewolf.MythBans.commands.discord;

import java.util.concurrent.ExecutionException;

import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.discord.MythCommandExecute;
import com.myththewolf.MythBans.lib.discord.MythDiscordBot;
import com.myththewolf.MythBans.tasks.LogWatcher;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class ClearLog implements MythCommandExecute{
	private MythDiscordBot bot;
	public ClearLog(MythDiscordBot BOT) {
		bot = BOT;
	}

	@Override
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage) {
			try {
				bot.getConsoleThread().edit("[MythBansBot]Cleared log!");
				LogWatcher.clearLog();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	@Override
	public boolean requiresRoot() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean requiresLinked() {
		// TODO Auto-generated method stub
		return false;
	}

}
