package com.myththewolf.MythBans.threads;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;

import net.md_5.bungee.api.ChatColor;

public class AlerResolved implements Runnable {
	private Connection con = MythSQLConnect.getConnection();
	private PlayerCache pCache = new PlayerCache(con);
	private PreparedStatement ps;
	private ResultSet rs;
	private OfflinePlayer p;

	@Override
	public void run() {
		try {
			ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE user_seen = ?");
			ps.setString(1, "NO");
			rs = ps.executeQuery();
			while (rs.next()) {
				p = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("SENDER_UUID")));
				if (p.isOnline()) {
					p.getPlayer().sendMessage(
							ConfigProperties.PREFIX + ChatColor.GOLD + pCache.getName(rs.getString("handler")) + " closed your ticket #" + rs.getString("ID"));
					ps = (PreparedStatement) con
							.prepareStatement("UPDATE MythBans_Tickets SET user_seen = ? WHERE ID = ?");
					ps.setBoolean(1, true);
					ps.setString(2, rs.getString("ID"));
					ps.executeUpdate();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
