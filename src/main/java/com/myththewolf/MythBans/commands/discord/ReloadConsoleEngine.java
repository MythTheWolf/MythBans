package com.myththewolf.MythBans.commands.discord;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.discord.DiscordCommand;
import com.myththewolf.MythBans.lib.discord.MythCommandExecute;
import com.myththewolf.MythBans.lib.discord.MythDiscordBot;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class ReloadConsoleEngine implements MythCommandExecute {
	private int TaskId;
	public ReloadConsoleEngine(int task, JavaPlugin thePL) {
		TaskId = task;

	}

	@Override
	@DiscordCommand(requiresLinked = false, requiresRoot = true, deleteTriggerMessage = false)
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage) {
		theMessage.reply("Reloading ConsoleLogWatcher engine...");
		Bukkit.getScheduler().cancelTask(TaskId);
		
		MythDiscordBot.getBot().remakeConsoleThread("---Requested Reload---");
		theMessage.getChannelReceiver().sendMessage("Engine reloaded.");

	}

}
