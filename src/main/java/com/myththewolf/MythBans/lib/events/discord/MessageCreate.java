package com.myththewolf.MythBans.lib.events.discord;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;

import com.myththewolf.MythBans.lib.MythDiscordBot;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.player.AbstractPlayer;
import com.myththewolf.MythBans.lib.player.Player;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import net.md_5.bungee.api.ChatColor;

public class MessageCreate implements MessageCreateListener {
	private MythDiscordBot myBot;
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());

	public MessageCreate(MythDiscordBot MDB) {
		myBot = MDB;
	}

	@Override
	public void onMessageCreate(DiscordAPI theAPI, Message theMessage) {
		try {
			if (theMessage.getAuthor().isBot()) {
				return;
			}

			String[] theSplit = theMessage.getContent().split(" ");
			if (theSplit[0].equals("mclink")) {
				if (pCache.isLinked(theMessage.getAuthor().getId())) {
					theMessage.getAuthor().sendMessage("You are already linked!");
					return;
				}
				String SALT = Utils.getSaltString();
				while(pCache.secretExists(SALT)){
					SALT = Utils.getSaltString();
				}
				theMessage.getAuthor().sendMessage("Type this in game: /link " + SALT);
				pCache.bindSecret(SALT, theMessage.getAuthor().getId());
				return;
			}
			if (theSplit[0].equals("!setup") && !myBot.isSetup()) {
				String SRV_ID = theMessage.getChannelReceiver().getServer().getId();
				String CHAN_ID = theMessage.getChannelReceiver().getServer().createChannel("minecraft").get().getId();
				myBot.writeData(SRV_ID, CHAN_ID);
				theMessage.delete();
				theMessage.getAuthor().sendMessage(
						"Alright sparky, here's the deal: I have written down everythig I needed, and all you need to do is restart your server.\n I will remain disconnected until then.");
				myBot.disconnect();
				return;
			}
			AbstractPlayer AB = new AbstractPlayer(theMessage.getAuthor().getId());
			String MC_ID = null;
			Player thePlayer = new Player();
			PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
			if(!theMessage.getChannelReceiver().getId().equals(myBot.getChannel().getId())){
				theMessage.getAuthor().sendMessage("Command not found");
				return;
			}
			if (!AB.isLinked()) {
				
				Thread.sleep(500);
				theMessage.delete();
				theMessage.getAuthor().sendMessage("I couldn't send your message because your MC account isn't linked");
				return;
			} else {
				MC_ID = AB.getPlayer().getUniqueId().toString();
			}
			if (thePlayer.getStatus(MC_ID).equals("muted") || thePlayer.getStatus(MC_ID).equals("softmuted")) {
				theMessage.delete();
				theMessage.getAuthor().sendMessage("I couldn't send your message because you are muted in game");
				return;
			} else {
				String theString = ChatColor.GREEN + "[DISCORD]" + ChatColor.GOLD + pCache.getName(MC_ID) + " >> "
						+ theMessage.getContent() + "\n";
				theMessage.delete();
				myBot.appendThread("\n"+ChatColor.stripColor(theString));
				for (org.bukkit.entity.Player P : Bukkit.getOnlinePlayers()) {
					P.sendMessage(theString);
				}

			}
		} catch (SQLException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}
