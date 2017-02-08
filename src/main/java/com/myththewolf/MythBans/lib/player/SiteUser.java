package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;

public class SiteUser {
	private Connection con = MythSQLConnect.getConnection();
	private PreparedStatement ps;
	private PlayerCache pc = new PlayerCache(con);

	public void insertUser(String username, String password_hash) throws SQLException {
		ps = (PreparedStatement) con
				.prepareStatement("INSERT INTO MythBans_SiteUsers (`UUID`,`password`,`group`) VALUES (?,?,?)");
		ps.setString(1, username);
		ps.setString(2, password_hash);
		ps.setString(3, "root");
		ps.executeUpdate();
	}

	public boolean isExistant(String username) throws SQLException {
		if (pc.getUUID(username) == null) {
			return false;
		}
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_SiteUsers WHERE UUID = ?");
		ps.setString(1, pc.getUUID(username));
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			return false;
		}
		return true;
	}
}
