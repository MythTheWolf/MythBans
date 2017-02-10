package com.myththewolf.MythBans.lib.events.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;

public class IP {
	private Connection con = MythSQLConnect.getConnection();
	private PreparedStatement ps;
	private ResultSet rs;

	public String getWhoBanned(String IP) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE IP_ADDRESS = ?");
		ps.setString(1, IP);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("byUUID");
		}
		return null;
	}

	public String getReason(String IP) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE IP_ADDRESS = ?");
		ps.setString(1, IP);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("reason");
		}
		return null;
	}
}
