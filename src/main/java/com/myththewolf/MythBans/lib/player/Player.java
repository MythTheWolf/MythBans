package com.myththewolf.MythBans.lib.player;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.Date;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class Player {
	private ResultSet rs;
	private PreparedStatement ps;
	private PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private final com.myththewolf.MythBans.lib.tool.Date MythDate = new com.myththewolf.MythBans.lib.tool.Date();

	public String getStatus(String UUID) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("status");
		}
		return "OK";
	}

	public String getReason(String UUID) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next()) {

			return rs.getString("reason");
		}
		return ConfigProperties.DEFAULT_BAN_REASON;
	}

	public String getWhoBanned(String UUID) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("byUUID");
		}
		return null;
	}

	public void processNewUser(String UUID, String name) throws SQLException {
		Date date = MythDate.getNewDate();

		pc.insertPlayer(UUID, name);
		ps = (PreparedStatement) MythSQLConnect.getConnection().prepareStatement(
				"INSERT INTO MythBans_PlayerStats (`UUID`,`status`,`group`,`last_name`,`timestamp`,`playtime`) VALUES (?,?,?,?,?,?);");
		ps.setString(1, UUID);
		ps.setString(2, "OK");
		ps.setString(3, "DEFAULT");
		ps.setString(4, name);
		ps.setString(5, MythDate.formatDate(date));
		ps.setString(6, "0");
		ps.executeUpdate();
		ps.close();
	}

	public Date getExpireDate(String UUID) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return MythDate.parseDate(rs.getString("expires"));
		}
		return MythDate.parseDate(rs.getString("expire"));
	}

	public void clearExpire(String UUID) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET `status` = ?, `expires` = ? WHERE `UUID` = ?");
		ps.setString(1, "OK");
		ps.setString(2, null);
		ps.setString(3, UUID);
		ps.executeUpdate();
		ps.close();
	}

	public long getPlayTime(String UUID) throws SQLException {
		long store = 0;
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next()) {
			store = rs.getLong("playtime");
		}
		return store;
	}

	public void setQuitTime(String time, String UUID) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET last_quit_date = ? WHERE UUID = ?");
		ps.setString(1, time);
		ps.setString(2, UUID);
		ps.executeUpdate();
	}

	public ResultSet getHistoryPack(String UUID) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_History WHERE UUID = ?");
		ps.setString(1, UUID);
		return ps.executeQuery();
	}

	public Date getQuitDate(String UUID) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return MythDate.parseDate(rs.getString("last_quit_date"));
		}
		return null;
	}

	public Date getJoinDate(String UUID) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return MythDate.parseDate(rs.getString("timestamp"));
		}
		return null;
	}

	public Date getSessionJoinDate(String UUID) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return MythDate.parseDate(rs.getString("session_start"));
		}
		return null;
	}

	public void setSession(String UUID, String time) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET session_start = ?  WHERE UUID = ?");
		ps.setString(2, UUID);
		ps.setString(1, time);
		ps.executeUpdate();
	}

	public void setPlayTime(String UUID, long timeDifference) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET playtime = ?  WHERE UUID = ?");
		ps.setString(2, UUID);
		ps.setString(1, Long.toString(timeDifference));
		ps.executeUpdate();
	}
}
