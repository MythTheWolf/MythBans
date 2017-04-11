package com.myththewolf.MythBans.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.FutureCallback;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.events.discord.MessageCreate;
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
import de.btobastian.javacord.entities.permissions.Role;
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
						
						mcChannel.updateTopic("PM MythBot with \"mclink\" to use this channel");
						return;
					} else {
						System.out.println(
								"The system isn't setup, after running the setup method on your server, restart this MC server.");
						OK = false;
						return;
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
		IMPL.setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED);
		IMPL.setState(PermissionType.EMBED_LINKS, PermissionState.DENIED);
		IMPL.setState(PermissionType.ATTACH_FILE, PermissionState.DENIED);
		IMPL.setState(PermissionType.USE_EXTERNAL_EMOJIS, PermissionState.DENIED);
		Permissions perms = IMPL.build();
		for (User UU : theConnection.getUsers()) {
			AbstractPlayer AB = new AbstractPlayer(UU.getId());
			if (AB.isLinked()) {
				this.mcChannel.updateOverwrittenPermissions(UU, perms);
			}
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
			return this.mcChannel.sendMessage("-----Bot online-----\n" + " \n ").get();

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
			for (Role R : this.connectedServer.getRoles()) {

				this.mcChannel.updateOverwrittenPermissions(R, PERMS);
			}
		}
		theConnection.disconnect();
	}
}