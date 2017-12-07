package com.myththewolf.MythBans.lib.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.DataCache;

public class ChatChannel {

	private String CHANNEL_NAME = "undefined";
	private String CHANNEL_PREFIX = "[UNDEFINED NAME]";
	private String CHANNEL_SHORTCUT = "undefinedmythbansshourtcut&#&$";

	public ChatChannel(String name) {
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = MythSQLConnect.getConnection().prepareStatement("SELECT * FROM `MythBans_Channels` WHERE `name` = ?");
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (!rs.next()) {
				throw new IllegalStateException("Channel name invalid!");
			}
			this.CHANNEL_NAME = rs.getString("name");
			this.CHANNEL_PREFIX = rs.getString("prefix");
			this.CHANNEL_SHORTCUT = rs.getString("shortcut");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void push(MythPlayer sender, String message) {
		// This is to send to all people writing
		Bukkit.getServer().getOnlinePlayers().stream().filter(rawPlayer -> {
			MythPlayer MP = DataCache.getPlayerInstance(rawPlayer.getUniqueId().toString());
			return (MP.getChannels().contains(this) && !MP.isIgnoring(MP.getId())
					&& MP.getWritingChannel().equals(this));
		}).forEach(player -> {
			player.sendMessage(ChatColor.GREEN + this.CHANNEL_PREFIX
					+ sender.getBukkitPlayer()
							.orElseThrow(
									() -> new IllegalStateException("Tried to send message but player doesn't exist!"))
							.getDisplayName()
					+ ChatColor.WHITE + ": " + message);
		});

		// All write channels are done,now lets do read onlys.
		Bukkit.getServer().getOnlinePlayers().stream().filter(rawPlayer -> {
			MythPlayer MP = DataCache.getPlayerInstance(rawPlayer.getUniqueId().toString());
			return (MP.getChannels().contains(this) && !MP.isIgnoring(MP.getId())
					&& !MP.getWritingChannel().equals(this));
		}).forEach(player -> {
			player.sendMessage(ChatColor.RED + this.CHANNEL_PREFIX
					+ sender.getBukkitPlayer()
							.orElseThrow(
									() -> new IllegalStateException("Tried to send message but player doesn't exist!"))
							.getDisplayName()
					+ ChatColor.WHITE + ": " + message);
		});
	}

	public List<Player> getAllPlayers() {
		return Bukkit.getServer().getOnlinePlayers().stream().filter(entry -> {
			return DataCache.getPlayerInstance(entry.getUniqueId().toString()).getChannels().contains(this);
		}).collect(Collectors.toList());
	}

	public String getName() {
		return this.CHANNEL_NAME;
	}

	public String getPrefix() {
		return this.CHANNEL_PREFIX;
	}

	public boolean equals(ChatChannel obj) {
		return getName().equals(obj.getName());
	}

	public void PushRaw(MythPlayer sender, String rawmessage) {
		String message = ChatColor.translateAlternateColorCodes('&', rawmessage);
		
		Bukkit.getServer().getOnlinePlayers().stream().filter(rawPlayer -> {
			MythPlayer MP = DataCache.getPlayerInstance(rawPlayer.getUniqueId().toString());
			return (MP.getChannels().contains(this) && !MP.isIgnoring(MP.getId())
					&& MP.getWritingChannel().equals(this));
		}).forEach(player -> {
			player.sendMessage(sender.getBukkitPlayer()
					.orElseThrow(() -> new IllegalStateException("Tried to send message but player doesn't exist!"))
					.getDisplayName() + ChatColor.WHITE + message);
		});

		// All write channels are done,now lets do read onlys.
		Bukkit.getServer().getOnlinePlayers().stream().filter(rawPlayer -> {
			MythPlayer MP = DataCache.getPlayerInstance(rawPlayer.getUniqueId().toString());
			return (MP.getChannels().contains(this) && !MP.isIgnoring(MP.getId())
					&& !MP.getWritingChannel().equals(this));
		}).forEach(player -> {
			player.sendMessage(sender.getBukkitPlayer()
					.orElseThrow(() -> new IllegalStateException("Tried to send message but player doesn't exist!"))
					.getDisplayName() + ChatColor.WHITE + message);
		});
	}

	public String getShortcut() {
		return this.CHANNEL_SHORTCUT;
	}

	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChatChannel))
			return false;
		else
			return ((ChatChannel) obj).getName().equals(getName());
	}
}
