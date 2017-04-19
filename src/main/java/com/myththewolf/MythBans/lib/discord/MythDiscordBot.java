package com.myththewolf.MythBans.lib.discord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.util.concurrent.FutureCallback;
import com.myththewolf.MythBans.commands.discord.ClearLog;
import com.myththewolf.MythBans.commands.discord.McLink;
import com.myththewolf.MythBans.commands.discord.Ping;
import com.myththewolf.MythBans.commands.discord.ReloadConsoleEngine;
import com.myththewolf.MythBans.commands.discord.setup;
import com.myththewolf.MythBans.commands.discord.shutdown;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.discord.events.MessageCreate;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.tasks.LogWatcher;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissionsBuilder;
import net.md_5.bungee.api.ChatColor;

public class MythDiscordBot {
	public static DiscordAPI theConnection;
	private static boolean OK;
	private PreparedStatement ps;
	private ResultSet rs;
	private Connection con = MythSQLConnect.getConnection();
	private Channel mcChannel;
	private Server connectedServer;
	private static HashMap<String, MythCommandExecute> CommandMap = new HashMap<String, MythCommandExecute>();
	private Channel consoleChannel;
	private Message conThread;
	static MythDiscordBot tmp;
	private boolean supposed2bshutdown = false;
	int log_watch_id;
	private JavaPlugin plugin;
	public MythDiscordBot(final JavaPlugin pl) {
		tmp = this;
		DiscordAPI api = Javacord.getApi(ConfigProperties.BOT_API, true);
		plugin = pl;
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
						updateRoles();
						consoleChannel = getConsole();
						mcChannel.updateTopic("PM MythBot with \"mclink\" to use this channel");
						log_watch_id = Bukkit.getScheduler().runTaskAsynchronously(pl, new LogWatcher()).getTaskId();
						registerCommand("!mclink", new McLink());
						registerCommand("!ping", new Ping());
						registerCommand("!shutdown", new shutdown(tmp));
						registerCommand("!clear", new ClearLog(tmp));
						registerCommand("!reload", new ReloadConsoleEngine(log_watch_id, pl));
					} else {
						OK = false;
						if (api.getServers().size() < 0) {
							System.out.println(
									"The bot key and everythins is OK, but you need to join it to your server!");
							return;
						} else {
							System.out.println(
									"Bot is joined and everything is OK. But you need to run !setup on your server.");
							registerCommand("!setup", new setup(tmp));
							return;
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

	public static MythDiscordBot getBot() {
		return tmp;
	}

	private void updateRoles() throws SQLException, InterruptedException, ExecutionException {
		ImplPermissionsBuilder IMPL = new ImplPermissionsBuilder();
		IMPL.setState(PermissionType.EMBED_LINKS, PermissionState.DENIED);
		IMPL.setState(PermissionType.ATTACH_FILE, PermissionState.DENIED);
		IMPL.setState(PermissionType.USE_EXTERNAL_EMOJIS, PermissionState.DENIED);
		getChannel().updateOverwrittenPermissions(getAllowedRoll(), IMPL.build());

		for (Role R : getServer().getRoles()) {
			if (R.getId() == getAllowedRoll().getId()) {
				System.out.println("Setting allow--->" + R.getName());
				IMPL.setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.ALLOWED);
				IMPL.setState(PermissionType.READ_MESSAGES, PermissionState.ALLOWED);
				IMPL.setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED);
				getChannel().updateOverwrittenPermissions(R, IMPL.build());
				for (User U : R.getUsers()) {
					getChannel().updateOverwrittenPermissions(U, IMPL.build());
				}
			} else {
				System.out.println("Setting deny--->" + R.getName());
				IMPL.setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.DENIED);
				IMPL.setState(PermissionType.READ_MESSAGES, PermissionState.DENIED);
				IMPL.setState(PermissionType.SEND_MESSAGES, PermissionState.DENIED);
				getChannel().updateOverwrittenPermissions(R, IMPL.build());
			}

			if (R.getId() == getRootroll().getId()) {
				IMPL.setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.ALLOWED);
				IMPL.setState(PermissionType.READ_MESSAGES, PermissionState.ALLOWED);
				IMPL.setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED);
				for (User U : R.getUsers()) {
					getConsole().updateOverwrittenPermissions(U, IMPL.build());
				}
				getConsole().updateOverwrittenPermissions(R, IMPL.build());
			} else {
				System.out.println("Setting deny--->" + R.getName());
				IMPL.setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.DENIED);
				IMPL.setState(PermissionType.READ_MESSAGES, PermissionState.DENIED);
				IMPL.setState(PermissionType.SEND_MESSAGES, PermissionState.DENIED);
				getConsole().updateOverwrittenPermissions(R, IMPL.build());
			}

		}
		IMPL.setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.ALLOWED);
		IMPL.setState(PermissionType.READ_MESSAGES, PermissionState.ALLOWED);
		IMPL.setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED);
		getChannel().updateOverwrittenPermissions(theConnection.getYourself(), IMPL.build());
		getConsole().updateOverwrittenPermissions(theConnection.getYourself(), IMPL.build());
	}

	public void linkUser(String ID) {
		try {
			getAllowedRoll().addUser(theConnection.getUserById(ID).get());
		} catch (SQLException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Role getAllowedRoll() throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Discord WHERE `key` = ?");
		ps.setString(1, "ROLE-ID");
		rs = ps.executeQuery();
		while (rs.next()) {

			return getServer().getRoleById(rs.getString("value"));
		}
		return null;
	}

	private Role getRootroll() throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Discord WHERE `key` = ?");
		ps.setString(1, "ROOT-ROLE-ID");
		rs = ps.executeQuery();
		while (rs.next()) {

			return getServer().getRoleById(rs.getString("value"));
		}
		return null;
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

	public void writeData(String SRVID, String ROLEID, String ROOT_ID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("INSERT INTO MythBans_Discord (`key`,`value`) VALUES (?,?)");
		ps.setString(1, "SYSTEM-SETUP");
		ps.setBoolean(2, true);
		ps.executeUpdate();
		ps.setString(1, "SERVER-ID");
		ps.setString(2, SRVID);
		ps.executeUpdate();
		ps.setString(1, "ROLE-ID");
		ps.setString(2, ROLEID);
		ps.executeUpdate();
		ps.setString(1, "ROOT-ROLE-ID");
		ps.setString(2, ROOT_ID);
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

			return rs.getString("value");
		}
		return null;
	}



	public Message getConsoleThread() throws InterruptedException, ExecutionException {
		if (this.conThread == null) {
			this.conThread = this.consoleChannel.sendMessage("-----Bot online-----\n").get();
			return this.conThread;
		} else {
			return this.conThread;
		}

	}
	public JavaPlugin getJavaPlugin(){
		return this.plugin;
	}
	public void remakeConsoleThread(String input) {
		try {
			this.consoleChannel.sendMessage(input).get();
			LogWatcher.clearLog();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void appendThread(String append) {
		try {
			getChannel().sendMessage(append);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Server getServer() {
		return connectedServer;
	}

	public Channel getChannel() throws InterruptedException, ExecutionException {
		if (this.mcChannel == null) {
			this.mcChannel = getServer().createChannel("minecraft").get();
			return this.mcChannel;
		} else {
			return this.mcChannel;
		}

	}

	public Channel getConsole() throws SQLException, InterruptedException, ExecutionException {
		if (this.consoleChannel == null) {
			this.consoleChannel = getServer().createChannel("console").get();
			return this.consoleChannel;
		} else {
			return this.consoleChannel;
		}

	}

	public void disconnect() {
		try {
			this.getChannel().delete();
			this.getConsole().delete();
			supposed2bshutdown = true;
		} catch (SQLException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		theConnection.disconnect();
	}

	public boolean isShutdown() {
		return supposed2bshutdown;
	}

	public void appendConsole(String stuff) {
		String app = ChatColor.stripColor(stuff);
		try {
			if (getConsoleThread().getContent().length() > 2000) {
				this.remakeConsoleThread(app);
				return;
			}
			getConsoleThread().edit(ChatColor.stripColor(getConsoleThread().getContent() + app));
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	public static HashMap<String, MythCommandExecute> getCommandMap() {
		return CommandMap;
	}

	public void registerCommand(String command, MythCommandExecute executor) {
		CommandMap.put(command, executor);
	}
}