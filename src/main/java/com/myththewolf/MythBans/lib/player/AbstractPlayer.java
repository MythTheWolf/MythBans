package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;

public class AbstractPlayer {
	private String discordID;
	private PreparedStatement ps;
	private ResultSet rs;
	private Connection con = MythSQLConnect.getConnection();

	public AbstractPlayer(String discordID2) {
		this.discordID = discordID2;
	}

	public boolean isLinked() throws SQLException {

		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_NameCache WHERE `discord_id` = ?");
		ps.setString(1, discordID);
		rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}

	}

	public OfflinePlayer getPlayer() throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_NameCache WHERE `discord_id` = ?");
		ps.setString(1, discordID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID")));
		}
		return null;
	}

}
