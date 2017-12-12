package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.DataCache;

import net.md_5.bungee.api.ChatColor;

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
	private String parsed;

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
		this.parsed = "";
	}

	public String getCommand() {
		return this.COMMAND;
	}

	public String getName() {
		return this.NAME;
	}

	public boolean canBeUsedBy(Player p) {
		return p.hasPermission(this.PERMISSION);
	}

	public void pushToChannel(ChatChannel channel, MythPlayer sender, String extra) {
		sender.getBukkitPlayer().ifPresent(sender1 -> {
			String msg = translateMessage(SEND_MESSAGE, extra, sender, null);
			sender1.sendMessage(msg);
		});
		channel.getAllPlayers().forEach(player -> {
			Location loc = player.getLocation();
			player.playSound(loc, SOUND, 0, 0);
			player.sendMessage(translateMessage(RECEIVER_CHANNEL, extra, sender,
					DataCache.getPlayerInstance(player.getUniqueId().toString())));

		});

	}

	private String translateMessage(String str, String extra, MythPlayer sender, MythPlayer player) {
		parsed = str;
		if (player == null) {
			parsed = parsed.replace("%receiver%", "");
		} else {
			parsed = parsed.replace("%receiver%", player.getDisplayName());
		}
		if (extra == null || extra.equals("")) {
			parsed = parsed.replace("%extramessage%", "");
			String[] extras = extra.split(" ");
			for (int i = 0; i < extras.length; i++) {
				parsed = parsed.replace("%extra[" + i + "]%", extras[i]);
			}
		} else {
			parsed = parsed.replace("%extramessage%", extra);
		}
		parsed = parsed.replace("%sender%", sender.getDisplayName());

		return ChatColor.translateAlternateColorCodes('&', parsed);
	}

	public void pushToPlayer(MythPlayer receiver, MythPlayer sender, String extra) {
		sender.getBukkitPlayer().ifPresent(sender1 -> {
			String msg = translateMessage(SEND_MESSAGE, extra, sender, receiver);
			sender1.sendMessage(msg);
		});
		receiver.getBukkitPlayer().ifPresent(pl -> {
			pl.sendMessage(translateMessage(RECEIVER_MESSAGE, extra, sender, receiver));
			pl.playSound(pl.getLocation(), this.SOUND, 0, 0);
		});
	}
}
