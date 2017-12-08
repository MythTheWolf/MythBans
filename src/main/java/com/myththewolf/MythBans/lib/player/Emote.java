package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;

/**
 * Represents a Emote class
 * 
 * @author 100048201
 *
 */
public class Emote {
	private String NAME;
	private String SEND_MESSAGE;
	private String RECEIVER_MESSAGE;
	private String SOUND;
	private String COMMAND;
	private String PERMISSION;
	private String RECEIVER_CHANNEL;

	public Emote(String name) throws IllegalAccessException, SQLException {
		Connection con = MythSQLConnect.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = con.prepareStatement("SELECT * FROM `MythBans_Emotes` WHERE `name` = ?");
		ps.setString(1, name);
		rs = ps.executeQuery();

		this.RECEIVER_CHANNEL = rs.getString("channel_template");
		this.SEND_MESSAGE = rs.getString("send_message");
		this.RECEIVER_MESSAGE = rs.getString("user_template");
		this.COMMAND = rs.getString("command");
		this.SOUND = rs.getString("sound");
		this.PERMISSION = rs.getString("permission_node");
		this.NAME = name;
	}

	public boolean canBeUsedBy(Player p) {
		return p.hasPermission(this.PERMISSION);
	}

	public void pushToChannel(ChatChannel channel, MythPlayer sender) {

		String parsed = this.RECEIVER_CHANNEL;
		parsed = parsed.replaceAll("%channelname%", channel.getName());
		parsed = parsed.replaceAll("%sender%", sender.getDisplayName());
		channel.PushRaw(sender, parsed);
	}

	public void pushToPlayer(MythPlayer receiver, MythPlayer sender,String extra) {
		String parsed = this.RECEIVER_MESSAGE;
		parsed = parsed.replaceAll("%receiver%", receiver.getDisplayName());
		parsed = parsed.replaceAll("%sender%", sender.getDisplayName());
		parsed = parsed.replaceAll("%extramessage%", extra);
		String [] extras = extra.split(" ");
		for(int i = 0; i<extras.length; i++){
			parsed = parsed.replaceAll("%extra["+i+"]%", extras[i]);
		}
	}
}
