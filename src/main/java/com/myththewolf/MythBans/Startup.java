package com.myththewolf.MythBans;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.DiscordConnection;
import com.myththewolf.MythBans.lib.MythBans;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.tool.Date;

import ch.qos.logback.classic.Level;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissionsBuilder;

public class Startup extends JavaPlugin {
	private Logger MythLogger = this.getLogger();

	public void onEnable() {
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
				.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
		MythBans mb = new MythBans(this);
		mb.loadConfig();
		if (mb.loadMySQL() == null) {
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}

		mb.loadCommands();
		MythLogger.info("Loaded 6 tables.");
		mb.startDaemon();

		mb.loadEvents();
		if (ConfigProperties.use_bot) {
			mb.startDiscordBot();
			ConfigProperties.dumpDiscord();
		}
	}

	public void onDisable() {
		com.myththewolf.MythBans.lib.player.Player pClass = new com.myththewolf.MythBans.lib.player.Player();
		Date date = new Date();
		try{
		for (Player p : Bukkit.getOnlinePlayers()) {
			String UUID = p.getUniqueId().toString();
			pClass.setQuitTime(date.formatDate(date.getNewDate()), UUID);
			pClass.setPlayTime(UUID, date.getTimeDifference(pClass.getSessionJoinDate(UUID), date.getNewDate())
					+ pClass.getPlayTime(UUID));
		}
		}catch(SQLException e){
			e.printStackTrace();
		}
		if (ConfigProperties.DISCORD_SETUP) {
			DiscordConnection.getConnection().getChannelById(ConfigProperties.DISCORD_MINECRAFT_CHANNEL_ID)
					.updateTopic("Minecraft server is offline..");
			DiscordConnection.getConnection().getMessageById(ConfigProperties.TEMP_THREAD).delete();
			ImplPermissionsBuilder IM = new ImplPermissionsBuilder();
			IM.setState(PermissionType.SEND_MESSAGES, PermissionState.DENIED);
			Permissions PERMS = IM.build();
			for (Role R : DiscordConnection.getConnection().getServerById(ConfigProperties.DISCORD_SERVER_ID)
					.getRoles()) {

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
		}
		if (ConfigProperties.DISCORD_SETUP) {
			DiscordConnection.getConnection().disconnect();
		}

	}
}
