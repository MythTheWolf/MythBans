package com.myththewolf.MythBans.lib;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.FutureCallback;
import com.myththewolf.MythBans.lib.events.discord.MessageCreate;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.AbstractPlayer;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissionsBuilder;

public class DiscordConnection {
	public static DiscordAPI theConnection;
	private static boolean OK;

	public static boolean connect() {

		DiscordAPI api = Javacord.getApi(ConfigProperties.BOT_API, true);

		api.connect(new FutureCallback<DiscordAPI>() {
			@Override
			public void onSuccess(final DiscordAPI api) {

				OK = true;
				theConnection = api;
				api.registerListener(new MessageCreate());
				Discord disc = new Discord(api);
				if (ConfigProperties.DISCORD_SETUP) {
					disc.getLogger().sendMessage("UP!");
					disc.pushToDiscord();
					disc.getMinecraft().updateTopic("PM MythBot with \"mclink\" to use this channel.");
					ImplPermissionsBuilder IM = new ImplPermissionsBuilder();
					IM.setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED);
					Permissions PERMS = IM.build();
					for (User UU : DiscordConnection.getConnection().getUsers()) {

						try {
							if (!UU.isBot()) {
								AbstractPlayer AB = new AbstractPlayer(UU.getId());
								if (AB.isLinked()) {
									DiscordConnection.getConnection()
											.getChannelById(ConfigProperties.DISCORD_MINECRAFT_CHANNEL_ID)
											.updateOverwrittenPermissions(UU, PERMS).get();
								}
							} else {
								DiscordConnection.getConnection()
										.getChannelById(ConfigProperties.DISCORD_MINECRAFT_CHANNEL_ID)
										.updateOverwrittenPermissions(UU, PERMS).get();
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

			@Override
			public void onFailure(Throwable t) {
				// login failed
				OK = false;
				t.printStackTrace();
			}
		});
		return OK;
	}

	public static DiscordAPI getConnection() {
		return theConnection;
	}
}
