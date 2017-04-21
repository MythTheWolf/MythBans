package com.myththewolf.MythBans.lib.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class MythPlayer {
	private ResultSet rs;
	private static PreparedStatement ps;
	private static PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private final static com.myththewolf.MythBans.lib.tool.Date MythDate = new com.myththewolf.MythBans.lib.tool.Date();
	private String PLAYER_STATUS;
	private String BAN_REASON;
	private String WHO_BANNED;
	private boolean IS_OVERRIDE;
	private Date EXPIRE_DATE;
	private String UUID;
	private Long PLAY_TIME;
	private Date QUIT_DATE;
	private Date JOIN_DATE;
	private Date SESSION_START;
	private String LANG_FILE;

	public MythPlayer(String theUUID) {
		try {
			ps = (PreparedStatement) MythSQLConnect.getConnection()
					.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
			ps.setString(1, theUUID);
			rs = ps.executeQuery();
			if (!rs.next()) {
				return;
			} else {
				UUID = theUUID;
				PLAYER_STATUS = rs.getString("status");
				BAN_REASON = rs.getString("reason");
				WHO_BANNED = rs.getString("byUUID");
				IS_OVERRIDE = rs.getBoolean("override");
				if (rs.getString("expires") == null || rs.getString("expires").equals("")) {
					EXPIRE_DATE = null;
				} else {
					EXPIRE_DATE = MythDate.parseDate(rs.getString("expires"));
				}
				PLAY_TIME = rs.getLong("playtime");
				QUIT_DATE = MythDate.parseDate(rs.getString("last_quit_date"));
				JOIN_DATE = MythDate.parseDate(rs.getString("timestamp"));
				SESSION_START = MythDate.parseDate(rs.getString("session_start"));
				LANG_FILE = rs.getString("lang_file");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getStatus() {
		return PLAYER_STATUS;
	}

	public String getReason() {
		if (BAN_REASON == null || BAN_REASON.equals("")) {
			return ConfigProperties.DEFAULT_BAN_REASON;
		} else {
			return BAN_REASON;
		}
	}

	public String getWhoBanned() {
		return WHO_BANNED;
	}

	public static void processNewUser(String UUID, String name) throws SQLException {
		Date date = MythDate.getNewDate();

		pc.insertPlayer(UUID, name);
		ps = (PreparedStatement) MythSQLConnect.getConnection().prepareStatement(
				"INSERT INTO MythBans_PlayerStats (`UUID`,`status`,`group`,`last_name`,`timestamp`,`playtime`) VALUES (?,?,?,?,?,?);");
		ps.setString(1, UUID);
		if (ConfigProperties.AUTO_MUTE) {
			ps.setString(2, "softmuted");
		} else {
			ps.setString(2, "OK");
		}
		ps.setString(3, "DEFAULT");
		ps.setString(4, name);
		ps.setString(5, MythDate.formatDate(date));
		ps.setString(6, "0");
		ps.executeUpdate();
		setOverride(UUID, false);
		ps.close();
	}

	public boolean isOverride() throws SQLException {
		return IS_OVERRIDE;
	}

	public static void setOverride(String UUID, boolean over) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET `override` = ? where UUID = ?");
		ps.setString(1, Boolean.toString(over));
		ps.setString(2, UUID);
		ps.executeUpdate();
	}

	public Date getExpireDate() throws SQLException {
		return EXPIRE_DATE;
	}

	public void clearExpire() throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET `status` = ?, `expires` = ? WHERE `UUID` = ?");
		ps.setString(1, "OK");
		ps.setString(2, null);
		ps.setString(3, UUID);
		ps.executeUpdate();
		ps.close();
	}

	public long getPlayTime() {
		return PLAY_TIME;
	}

	public void setQuitTime(String time) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET last_quit_date = ? WHERE UUID = ?");
		ps.setString(1, time);
		ps.setString(2, UUID);
		ps.executeUpdate();
	}

	public ResultSet getHistoryPack() throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_History WHERE UUID = ?");
		ps.setString(1, UUID);
		return ps.executeQuery();
	}

	public Date getQuitDate() throws SQLException {
		return QUIT_DATE;
	}

	public Date getJoinDate() throws SQLException {
		return JOIN_DATE;
	}

	public Date getSessionJoinDate(String UUID) throws SQLException {
		return SESSION_START;
	}

	public void setSession(String time) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET session_start = ?  WHERE UUID = ?");
		ps.setString(2, UUID);
		ps.setString(1, time);
		ps.executeUpdate();
	}

	public void setPlayTime(long timeDifference) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET playtime = ?  WHERE UUID = ?");
		ps.setString(2, UUID);
		ps.setString(1, Long.toString(timeDifference));
		ps.executeUpdate();
	}

	public void setStatus(String stat) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET status = ?  WHERE UUID = ?");
		ps.setString(2, UUID);
		ps.setString(1, stat);
		ps.executeUpdate();

	}

	public String getLang() {
		if (LANG_FILE == null || LANG_FILE.equals("")) {
			return ConfigProperties.SYSTEM_LOCALE;
		} else {
			return LANG_FILE;
		}
	}
}
