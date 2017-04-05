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
		String bool = this.getPair(theUUID + "_spying");
		return Boolean.parseBoolean(bool);
	}

	public String getPair(String key) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_AbstractData WHERE `key` = ?");
		ps.setString(1, key);
		rs = ps.executeQuery();
		while(rs.next()){
			return rs.getString("value");
		}
		return null;
	}

	public void putPair(String key, String val) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("INSERT INTO MythBans_AbstractData (`key`,`value`) VALUES (?,?)");
		ps.setString(1, key);
		ps.setString(2, val);
		ps.executeUpdate();
	}

	public void setSpy(boolean spy) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_AbstractData WHERE `key` = ?");
		ps.setString(1, theUUID + "_spying");
		rs = ps.executeQuery();
		if (!rs.next()) {
			this.putPair(theUUID + "_spying", Boolean.toString(spy));
		} else {
			this.updatePair(theUUID + "_spying", Boolean.toString(spy));
		}
	}

	public void updatePair(String key, String value) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("UPDATE MythBans_AbstractData SET `value` = ?  WHERE `key` = ?");
		ps.setString(1, value);
		ps.setString(2, key);
		ps.executeUpdate();
	}
}
