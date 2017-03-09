package com.myththewolf.MythBans.lib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.tool.Utils;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import net.md_5.bungee.api.ChatColor;

public class Discord {
	private DiscordAPI con;
	private DatabaseCommands dbc;

	public Discord(DiscordAPI i) {
		con = i;
	}

	public Channel getLogger() {
		Channel theChannel;
		theChannel = con.getChannelById(ConfigProperties.DISCORD_LOGGER_ID);
		return theChannel;
	}

	public void pushToDiscord() {
		ConfigProperties.dumpDiscord();
		try {
			StringBuilder sb = new StringBuilder();
			PreparedStatement ps;
			ResultSet packet = null;
			sb.append("------Bot online------");
			ps = (PreparedStatement) MythSQLConnect.getConnection()
					.prepareStatement("SELECT * FROM MythBans_GameChat ORDER BY ID DESC LIMIT 20");
			packet = ps.executeQuery();
			while (packet.next()) {
				sb.append(packet.getString("name") + " : " + ChatColor.stripColor(packet.getString("text")) + "\n\n");
			}
			ConfigProperties.TEMP_THREAD = this.getMinecraft().sendMessage(sb.toString()).get().getId();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Channel getMinecraft() {
		Channel theChannel;
		theChannel = con.getChannelById(ConfigProperties.DISCORD_MINECRAFT_CHANNEL_ID);
		return theChannel;
	}

	public void appendToThread(String message, String displayName) {
		StringBuilder sb = new StringBuilder();
		PreparedStatement ps;
		ResultSet packet = null;
		try {
			ps = (PreparedStatement) MythSQLConnect.getConnection()
					.prepareStatement("SELECT * FROM MythBans_GameChat ORDER BY ID ASC LIMIT 500");
			packet = ps.executeQuery();
			int count = 0;
			while (packet.next()) {
				count++;
				sb.append(packet.getString("name") + " : " + ChatColor.stripColor(packet.getString("text")) + "\n\n");
			}
			if(count >= 500)
			{
				ps = (PreparedStatement) MythSQLConnect.getConnection().prepareStatement("TRUNCATE MythBans_GameChat");
				ps.executeUpdate();
			}
			DiscordConnection.getConnection().getMessageById(ConfigProperties.TEMP_THREAD).edit(sb.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
