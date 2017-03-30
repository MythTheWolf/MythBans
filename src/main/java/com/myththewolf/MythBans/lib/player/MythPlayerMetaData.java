package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;

public class MythPlayerMetaData {
	private String theUUID;
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
	private Connection con = MythSQLConnect.getConnection();
	private ResultSet rs;
	private PreparedStatement ps;

	public MythPlayerMetaData(String theirUUID) throws Exception {
		theUUID = theirUUID;
		if (pCache.getName(theirUUID) == null) {
			throw new Exception();
		}
	}

	public boolean isSpying() throws SQLException {
		rs = this.getPair(theUUID + "_spying");
		if (!rs.next()) {
			return false;
		} else {
			return rs.getBoolean("value");
		}
	}

	public ResultSet getPair(String key) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_AbstractData WHERE key = ?");
		ps.setString(1, key);
		return ps.executeQuery();
	}

	public void putPair(String key, String val) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("INSERT INTO MythBans_AbstractData (`key`,`value`) VALUES (?,?)");
		ps.setString(1, key);
		ps.setString(2, val);
	}

	public void setSpy(boolean spy) throws SQLException {
		this.putPair(theUUID + "_spying", Boolean.toString(spy));
	}
}
