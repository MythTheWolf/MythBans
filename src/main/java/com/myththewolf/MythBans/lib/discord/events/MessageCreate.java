package com.myththewolf.MythBans.lib.discord.events;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.discord.CommandDispatcher;
import com.myththewolf.MythBans.lib.discord.MythDiscordBot;
import com.myththewolf.MythBans.lib.player.AbstractPlayer;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import net.md_5.bungee.api.ChatColor;

public class MessageCreate implements MessageCreateListener {
	private MythDiscordBot myBot;

	public MessageCreate(MythDiscordBot MDB) {
		myBot = MDB;

	}

	@Override
	public void onMessageCreate(DiscordAPI theAPI, Message theMessage) {
		try {
			String[] theSplit = theMessage.getContent().split(" ");
			if (theMessage.getAuthor().isBot()) {
				return;
			}
			if (theSplit[0].equals("!startup") && myBot.isShutdown()) {
				new CommandDispatcher(theMessage.getContent(), theMessage.getAuthor(), theMessage);
				return;
			} else if (myBot.isShutdown()) {
				return;
			}
			if (theSplit[0].charAt(0) == '!') {
				new CommandDispatcher(theMessage.getContent(), theMessage.getAuthor(), theMessage);
				return;
			}

			if (!myBot.isSetup()) {
				return;
			}
			AbstractPlayer AB = new AbstractPlayer(theMessage.getAuthor().getId());

			String MC_ID = null;

			if (!AB.isLinked()) {

				Thread.sleep(500);
				theMessage.delete();
				theMessage.getAuthor().sendMessage("I couldn't send your message because your MC account isn't linked");
				return;
			}
			MC_ID = AB.getPlayer().getUniqueId().toString();
			MythPlayer thePlayer = new MythPlayer(AB.getPlayer().getUniqueId().toString());
			if (myBot.isSetup() && !theMessage.getChannelReceiver().equals(myBot.getChannel())) {
				return;
			}

			PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
			if (!theMessage.getChannelReceiver().getId().equals(myBot.getChannel().getId())) {
				theMessage.getAuthor().sendMessage("Command not found");
				return;
			}
			if (thePlayer.getStatus().equals("muted") || thePlayer.getStatus().equals("softmuted")) {
				theMessage.delete();
				theMessage.getAuthor().sendMessage("I couldn't send your message because you are muted in game");
				return;
			} else {
				String theString = ChatColor.GREEN + "[DISCORD]" + ChatColor.GOLD + pCache.getName(MC_ID) + " >> "
						+ theMessage.getContent() + "";
				theMessage.delete();
				myBot.appendThread("\n" + ChatColor.stripColor(theString));
				for (org.bukkit.entity.Player P : Bukkit.getOnlinePlayers()) {
					P.sendMessage(theString);
				}

			}
		} catch (SQLException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}
