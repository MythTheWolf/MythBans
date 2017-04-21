package com.myththewolf.MythBans.commands.discord;

import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.discord.DiscordCommand;
import com.myththewolf.MythBans.lib.discord.MythCommandExecute;
import com.myththewolf.MythBans.lib.discord.MythDiscordBot;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class shutdown implements MythCommandExecute {
	private MythDiscordBot bot;

	public shutdown(MythDiscordBot MB) {
		bot = MB;
	}

	@Override
	@DiscordCommand(requiresLinked = false, requiresRoot = true)
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage) {

		theMessage.reply("Bot going down :(");
		bot.disconnect();
		return;

	}

}
