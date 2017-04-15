package com.myththewolf.MythBans.lib.discord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.FutureCallback;
import com.myththewolf.MythBans.commands.discord.PingPong;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.discord.events.MessageCreate;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.AbstractPlayer;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissionsBuilder;

public class MythDiscordBot {
	public static DiscordAPI theConnection;
	private static boolean OK;
	private PreparedStatement ps;
	private ResultSet rs;
	private Connection con = MythSQLConnect.getConnection();
	private Channel mcChannel;
	private Server connectedServer;
	private Message thread = null;
	private static HashMap<String, MythCommandExecute> CommandMap = new HashMap<String, MythCommandExecute>();

	public MythDiscordBot() {
		final MythDiscordBot tmp = this;
		DiscordAPI api = Javacord.getApi(ConfigProperties.BOT_API, true);

		api.connect(new FutureCallback<DiscordAPI>() {
			@Override
			public void onSuccess(final DiscordAPI api) {
				OK = true;
				theConnection = api;
				api.registerListener(new MessageCreate(tmp));
				try {
					if (isSetup()) {
						connectedServer = api.getServerById(getServerID());
						mcChannel = getChannel();
						thread = getThread();
						updateRoles();
						registerCommand("!ping", new PingPong());
						mcChannel.updateTopic("PM MythBot with \"mclink\" to use this channel");
						return;
					} else {
						OK = false;
						if (api.getServers().size() < 0) {
							System.out.println(
									"The bot key and everythins is OK, but you need to join it to your server!");
							return;
						} else {
							System.out.println(
									"Bot is joined and everything is OK. But you need to run !setup on your server.");
						}
					}
				} catch (SQLException | InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t) {
				// login failed
				OK = false;
				t.printStackTrace();
			}
		});

	}

	private void updateRoles() throws SQLException {
		ImplPermissionsBuilder IMPL = new ImplPermissionsBuilder();

		iterateUser: for (User UU : theConnection.getUsers()) {
			if (UU.isBot()) {
				continue iterateUser;
			}
			AbstractPlayer AB = new AbstractPlayer(UU.getId());
			System.out.println(AB.isLinked());
			if (AB.isLinked()) {
				IMPL.setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED);
				IMPL.setState(PermissionType.EMBED_LINKS, PermissionState.DENIED);
				IMPL.setState(PermissionType.ATTACH_FILE, PermissionState.DENIED);
				IMPL.setState(PermissionType.USE_EXTERNAL_EMOJIS, PermissionState.DENIED);
				System.out.println("IS_LINKED");
				this.mcChannel.updateOverwrittenPermissions(UU, IMPL.build());
				continue iterateUser;
			}
			System.out.println("IS_LINKED_NOT");
			IMPL.setState(PermissionType.READ_MESSAGES, PermissionState.DENIED);
			IMPL.setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.DENIED);
			IMPL.setState(PermissionType.SEND_MESSAGES, PermissionState.DENIED);
			this.mcChannel.updateOverwrittenPermissions(UU, IMPL.build());
			continue iterateUser;
		}
	}

	public DiscordAPI getConnection() {
		return theConnection;
	}

	public boolean isConnected() {
		return OK;
	}

	public boolean isSetup() throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Discord WHERE `key` = ?");
		ps.setString(1, "SYSTEM-SETUP");
		rs = ps.executeQuery();

		while (rs.next()) {
			return rs.getBoolean("value");
		}
		return false;
	}

	public void writeData(String SRVID, String CHANID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("INSERT INTO MythBans_Discord (`key`,`value`) VALUES (?,?)");
		ps.setString(1, "SYSTEM-SETUP");
		ps.setBoolean(2, true);
		ps.executeUpdate();
		ps.setString(1, "SERVER-ID");
		ps.setString(2, SRVID);
		ps.executeUpdate();
		ps.setString(1, "MINECRAFT-CHANNEL-ID");
		ps.setString(2, CHANID);
		ps.executeUpdate();
	}

	public void setThread(String MID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("UPDATE MythBans_Discord SET `value` = ? WHERE `key` = ?");
		ps.setString(1, MID);
		ps.setString(2, "MINECRAFT-THREAD-ID");
		ps.executeUpdate();
	}

	public String getServerID() throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Discord WHERE `key` = ?");
		ps.setString(1, "SERVER-ID");
		rs = ps.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getString("value"));
			return rs.getString("value");
		}
		return null;
	}

	public Message getThread() throws InterruptedException, ExecutionException {
		if (this.thread == null) {
			return this.mcChannel.sendMessage("-----Bot online-----\n").get();

		} else {
			return this.thread;
		}

	}

	public void appendThread(String append) {
		String old = this.thread.getContent();
		this.thread.edit(old + append);
	}

	public Server getServer() {
		return connectedServer;
	}

	public Channel getChannel() throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Discord WHERE `key` = ?");
		ps.setString(1, "MINECRAFT-CHANNEL-ID");
		rs = ps.executeQuery();
		rs.next();

		return MythDiscordBot.theConnection.getChannelById(rs.getString("value"));
	}

	public void disconnect() {
		if (this.thread != null) {
			this.thread.delete();
		}
		if (this.mcChannel != null) {
			this.mcChannel.updateTopic("MC Server offline.");
			ImplPermissionsBuilder IM = new ImplPermissionsBuilder();
			IM.setState(PermissionType.SEND_MESSAGES, PermissionState.DENIED);
			Permissions PERMS = IM.build();
			iterateUser: for (User UU : theConnection.getUsers()) {
				if (UU.isBot()) {
					continue iterateUser;
				}
				this.mcChannel.updateOverwrittenPermissions(UU, PERMS);
			}
		}
		theConnection.disconnect();
	}

	public static HashMap<String, MythCommandExecute> getCommandMap() {
		return CommandMap;
	}
	public void registerCommand(String command, MythCommandExecute executor){
		CommandMap.put(command, executor);
	}
}