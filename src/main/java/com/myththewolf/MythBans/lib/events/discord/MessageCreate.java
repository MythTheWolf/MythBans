package com.myththewolf.MythBans.lib.events.discord;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.Discord;
import com.myththewolf.MythBans.lib.DiscordConnection;
import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.AbstractPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissionsBuilder;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import net.md_5.bungee.api.ChatColor;

public class MessageCreate implements MessageCreateListener {
	private PlayerCache PC = new PlayerCache(MythSQLConnect.getConnection());
	private DatabaseCommands dbc = new DatabaseCommands();

	@Override
	public void onMessageCreate(DiscordAPI connection, Message m) {
		if (m.getAuthor().isBot()) {
			return;
		}
		if (m.getChannelReceiver() == null) {
			if (m.getContent().equals("mclink")) {
				try {
					if (PC.isLinked(m.getAuthor().getId())) {
						m.getAuthor().sendMessage("You already have been linked!");
						return;
					} else {
						String theSecret = Utils.getSaltString();
						while (PC.secretExists(theSecret)) {
							theSecret = Utils.getSaltString();
						}
						PC.bindSecret(theSecret, m.getAuthor().getId());
						m.getAuthor().sendMessage("Type this in minecraft: /link " + theSecret);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				m.getAuthor().sendMessage("I literally do nothing here, move along, get outta my face.");
			}
		} else if (m.getChannelReceiver().getId().equals(ConfigProperties.DISCORD_MINECRAFT_CHANNEL_ID)) {
			AbstractPlayer AB = new AbstractPlayer(m.getAuthor().getId());
			String name = "";
			for (Player P : Bukkit.getOnlinePlayers()) {
				try {
					name = AB.getPlayer().getName();
					String msg = "<" + ChatColor.DARK_GREEN + "[DISCORD]" + ChatColor.GOLD + AB.getPlayer().getName()
							+ ChatColor.WHITE + "> " + m.getContent();
					P.sendMessage(msg);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				dbc.writeMessageToHistory(m.getContent(), name);
				Discord disc = new Discord(DiscordConnection.getConnection());
				disc.appendToThread(m.getContent(), AB.getPlayer().getName());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m.delete();
		}
		if (!ConfigProperties.DISCORD_SETUP
				&& m.getAuthor().getId().equals(m.getChannelReceiver().getServer().getOwnerId())) {
			String SERV_ID = m.getChannelReceiver().getServer().getId();
			try {
				String MC_ID = connection.getServerById(SERV_ID).createChannel("minecraft").get().getId();
				String LOG_ID = connection.getServerById(SERV_ID).createChannel("bot_logs").get().getId();
				ConfigProperties.writeDiscordInfo(SERV_ID, MC_ID, LOG_ID);
				ConfigProperties.dumpDiscord();
				m.getAuthor().sendMessage(
						"I have been setup on your server \"" + m.getChannelReceiver().getServer().getName() + "\"t");
				Discord disc = new Discord(connection);
				disc.pushToDiscord();
				ImplPermissionsBuilder IM = new ImplPermissionsBuilder();
				IM.setState(PermissionType.SEND_MESSAGES, PermissionState.DENIED);
				Permissions PERMS = IM.build();
				for (Role R : DiscordConnection.getConnection().getServerById(ConfigProperties.DISCORD_SERVER_ID).getRoles()) {
					try {
						DiscordConnection.getConnection().getChannelById(ConfigProperties.DISCORD_MINECRAFT_CHANNEL_ID)
								.updateOverwrittenPermissions(R, PERMS).get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
