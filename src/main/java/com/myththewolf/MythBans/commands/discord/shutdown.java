package com.myththewolf.MythBans.commands.discord;

import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.discord.MythCommandExecute;
import com.myththewolf.MythBans.lib.discord.MythDiscordBot;
import com.myththewolf.MythBans.lib.player.AbstractPlayer;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class shutdown implements MythCommandExecute{
	private MythDiscordBot bot;
	public shutdown(MythDiscordBot MB) {
		bot = MB;
	}
	@Override
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage) {
		AbstractPlayer AB = new AbstractPlayer(theDiscordUser.getId());
		if(!AB.isRootAccount()){
			theMessage.reply("You are not a root account!");
			return;
		}else{
			theMessage.reply("Bot going down :(");
			bot.disconnect();
			return;
		}
		
	}

}
