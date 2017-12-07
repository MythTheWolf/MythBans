package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.DataCache;

public class PlayerEmote {
	private String NAME;
	private String SEND_MESSAGE;
	private String RECEIVER_MESSAGE;
	private String SOUND;
	private String COMMAND;
	private String PERMISSION;
	private String RECEIVER_CHANNEL;

	public PlayerEmote(String name) throws IllegalAccessException, SQLException {
		Connection con = MythSQLConnect.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = con.prepareStatement("SELECT * FROM `MythBans_Emotes` WHERE `name` = ?");
		ps.setString(1, name);
		rs = ps.executeQuery();

	}

	public boolean canBeUsedBy(Player p) {
		return p.hasPermission(this.PERMISSION);
	}

	public void push(MythPlayer sender, ChatChannel channel) {
		String parsed = this.RECEIVER_CHANNEL;
		parsed = parsed.replaceAll("%channelname%", channel.getName());
		parsed = parsed.replaceAll("%sender%", sender.getDisplayName());
		channel.PushRaw(sender, parsed);
	}
}
