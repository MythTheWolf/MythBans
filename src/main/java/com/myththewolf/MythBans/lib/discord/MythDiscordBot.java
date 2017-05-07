package com.myththewolf.MythBans.lib.discord;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.util.concurrent.FutureCallback;
import com.myththewolf.MythBans.commands.discord.McLink;
import com.myththewolf.MythBans.commands.discord.Ping;
import com.myththewolf.MythBans.commands.discord.setup;
import com.myththewolf.MythBans.commands.discord.shutdown;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.discord.events.MessageCreate;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissionsBuilder;
/**
 * 
 * @author MythTheWolf
 *
 */
public class MythDiscordBot {
	public static DiscordAPI theConnection;
	private static boolean OK;
	private PreparedStatement ps;
	private ResultSet rs;
	private Connection con = MythSQLConnect.getConnection();
	private Channel mcChannel;
	private Server connectedServer;
	private static HashMap<String, MythCommandExecute> CommandMap = new HashMap<String, MythCommandExecute>();

	static MythDiscordBot tmp;
	private boolean supposed2bshutdown = false;
	int log_watch_id;
	private JavaPlugin plugin;
	/**
	 * The main constructor, requires the bukkit instance.
	 * @param pl The java plugin object
	 */
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

						mcChannel.updateTopic("PM MythBot with \"mclink\" to use this channel");
						registerCommand("!mclink", new McLink());
						registerCommand("!ping", new Ping());
						registerCommand("!shutdown", new shutdown(tmp));
					} else {
						OK = false;
						if (api.getServers().size() < 0) {
							System.out.println(
									"The bot key and everythings is OK, but you need to join it to your server!");
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
	/**
	 * Gets the current instance of this bot
	 * @return The bot
	 */
	public static MythDiscordBot getBot() {
		return tmp;
	}
	/**
	 * Updates the roles of the server to the newly generated channels.
	 * @throws SQLException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
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

		}
		IMPL.setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.ALLOWED);
		IMPL.setState(PermissionType.READ_MESSAGES, PermissionState.ALLOWED);
		IMPL.setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED);
		getChannel().updateOverwrittenPermissions(theConnection.getYourself(), IMPL.build());

	}
	/**
	 * Adds a user to the Linked account role
	 * @param ID The discord user's IDs
	 */
	public void linkUser(String ID) {
		try {
			getAllowedRoll().addUser(theConnection.getUserById(ID).get());
		} catch (SQLException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Gets the linked account role
	 * @return The Linked Account role
	 * @throws SQLException
	 */
	private Role getAllowedRoll() throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Discord WHERE `key` = ?");
		ps.setString(1, "ROLE-ID");
		rs = ps.executeQuery();
		while (rs.next()) {

			return getServer().getRoleById(rs.getString("value"));
		}
		return null;
	}
	/**
	 * Gets the discord connection
	 * @return The current discord connection
	 */
	public DiscordAPI getConnection() {
		return theConnection;
	}
	/**
	 * Gets the connection and test status
	 * @return Wether if the bot joined and all checks are good
	 */
	public boolean isConnected() {
		return OK;
	}
	/**
	 * Gets the setup status
	 * @return Whether if no further action is needed (default false) 
	 * @throws SQLException
	 */
	public boolean isSetup() throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Discord WHERE `key` = ?");
		ps.setString(1, "SYSTEM-SETUP");
		rs = ps.executeQuery();

		while (rs.next()) {
			return rs.getBoolean("value");
		}
		return false;
	}
	/**
	 * Writes the needed data to database,so we can retrive them even after the server closes.
	 * @param SRVID The server's ID
	 * @param ROLEID The Linked account Role ID
	 * @throws SQLException
	 */
	public void writeData(String SRVID, String ROLEID) throws SQLException {
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
	}
	/**
	 * @deprecated We don't use this anymore,but ill still be here just in case
	 * @param MID
	 * @throws SQLException
	 */
	public void setThread(String MID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("UPDATE MythBans_Discord SET `value` = ? WHERE `key` = ?");
		ps.setString(1, MID);
		ps.setString(2, "MINECRAFT-THREAD-ID");
		ps.executeUpdate();
	}
	/**
	 * Get's the discord server ID that we should be using
	 * @return The ID
	 * @throws SQLException
	 */
	public String getServerID() throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Discord WHERE `key` = ?");
		ps.setString(1, "SERVER-ID");
		rs = ps.executeQuery();
		while (rs.next()) {

			return rs.getString("value");
		}
		return null;
	}
	/**
	 * Gets a JavaPlugin instance
	 * @return The JavaPlugin instance
	 */
	public JavaPlugin getJavaPlugin() {
		return this.plugin;
	}
	/**
	 * Adds a new line to the text thread of the minecrat discord chanel
	 * @param append The string to append
	 * 
	 */
	public void appendThread(String append) {
		try {
			getChannel().sendMessage(append);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Gets the Discord server instance
	 * @return The Server
	 */
	public Server getServer() {
		return connectedServer;
	}
	/**
	 * Gets the minecraft discord channel instance
	 * <br /> <br />
	 * <b>NOTE:</b> Will create a new channel if not already existant
	 * @return The Minecraft Channel instance
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Channel getChannel() throws InterruptedException, ExecutionException {
		if (this.mcChannel == null) {
			this.mcChannel = getServer().createChannel("minecraft").get();
			return this.mcChannel;
		} else {
			return this.mcChannel;
		}

	}
	/**
	 * Shuts down the bot.
	 */
	public void disconnect() {
		try {
			this.getChannel().delete();

			supposed2bshutdown = true;
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		theConnection.disconnect();
	}
	/**
	 * Gets the bot's status
	 * @return If the bot is shutdown or not
	 */
	public boolean isShutdown() {
		return supposed2bshutdown;
	}
	/**
	 * Gets the registerd discord command map
	 * @return The map
	 */
	public static HashMap<String, MythCommandExecute> getCommandMap() {
		return CommandMap;
	}
	/**
	 * Adds new discord command
	 * @param command The literal command string
	 * @param executor The MythCommandExecute class to run when excuted
	 * @see MythCommandExecute
	 * @see CommandDispatcher
	 * @see DiscordCommand
	 */
	public void registerCommand(String command, MythCommandExecute executor) {
		try {
			Method M = executor.getClass().getMethod("runCommand");
			if (!M.isAnnotationPresent(DiscordCommand.class)) {
				this.plugin.getLogger().severe("Can't parse command " + command + ", missing annotations!");
				return;
			}

		} catch (NoSuchMethodException | SecurityException e) {

		}
		CommandMap.put(command, executor);
	}
}