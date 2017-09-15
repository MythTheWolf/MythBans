package com.myththewolf.MythBans.lib.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerDataCache;

public class MythPlayer {
	private ResultSet rs;
	private static PreparedStatement ps;
	private static PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private static final com.myththewolf.MythBans.lib.tool.Date MythDate = new com.myththewolf.MythBans.lib.tool.Date();
	private String PLAYER_STATUS;
	private String BAN_REASON;
	private String WHO_BANNED;
	private boolean IS_OVERRIDE;
	private java.util.Date EXPIRE_DATE;
	private String UUID;
	private Long PLAY_TIME;
	private java.util.Date QUIT_DATE;
	private java.util.Date JOIN_DATE;
	private java.util.Date SESSION_START;
	private String LANG_FILE;
	private boolean IS_PROBATED;
	private String CHANNEL;
	private List<String> IG = new ArrayList<String>();

	public MythPlayer(String theUUID) {
		try {
			ps = MythSQLConnect.getConnection().prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
			ps.setString(1, theUUID);
			this.rs = ps.executeQuery();
			if (!this.rs.next()) {
				return;
			}
			this.UUID = theUUID;
			this.PLAYER_STATUS = this.rs.getString("status");
			this.BAN_REASON = this.rs.getString("reason");
			this.WHO_BANNED = this.rs.getString("byUUID");
			this.IS_OVERRIDE = this.rs.getBoolean("override");
			this.CHANNEL = this.rs.getString("channel");
			if ((this.rs.getString("ignores") != null) && (!this.rs.getString("ignores").equals(""))) {

				for (String a : this.rs.getString("ignores").split(",")) {
					this.IG.add(a);
				}
			}
			if ((this.rs.getString("expires") == null) || (this.rs.getString("expires").equals(""))) {
				this.EXPIRE_DATE = null;
			} else {
				this.EXPIRE_DATE = MythDate.parseDate(this.rs.getString("expires"));
			}
			this.PLAY_TIME = Long.valueOf(this.rs.getLong("playtime"));
			if ((this.rs.getString("last_quit_date") == null) || (this.rs.getString("last_quit_date").equals(""))) {
				this.QUIT_DATE = null;
			} else {
				this.QUIT_DATE = MythDate.parseDate(this.rs.getString("last_quit_date"));
			}
			this.JOIN_DATE = MythDate.parseDate(this.rs.getString("timestamp"));
			if (this.rs.getString("session_start") == null) {
				this.SESSION_START = MythDate.getNewDate();
				setSession(this.UUID, MythDate.formatDate(MythDate.getNewDate()));
			} else {
				this.SESSION_START = MythDate.parseDate(this.rs.getString("session_start"));
			}
			this.LANG_FILE = this.rs.getString("lang_file");
			this.IS_PROBATED = this.rs.getBoolean("probated");

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void setChannel(String chan) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET `channel` = ? WHERE UUID = ?");
		ps.setString(1, chan);
		ps.setString(2, this.UUID);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(this.UUID);
	}

	public String getChannel() throws SQLException {
		if (this.CHANNEL == null) {
			return "GLOBAL";
		}
		ChatChannel CC = new ChatChannel(this.CHANNEL);
		if (CC.exists()) {
			return this.CHANNEL;
		}
		return "GLOBAL";
	}

	public String getStatus() {
		return this.PLAYER_STATUS;
	}

	public boolean isIgnoring(String id) {
		return this.IG.contains(id);
	}

	public void addIgnore(String id) throws SQLException {
		String theList = "";
		this.IG.add(id);
		for (String j : this.IG) {
			theList = theList + j + ",";
		}

		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE `MythBans_PlayerStats` SET `ignores` = ? WHERE UUID = ?");
		ps.setString(1, theList);
		ps.setString(2, this.UUID);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(this.UUID);
	}

	public void removeIgnore(String id) throws SQLException {
		String theList = "";
		this.IG.remove(id);
		for (String j : this.IG) {
			theList = theList + j + ",";
		}
		System.out.println(theList);
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE `MythBans_PlayerStats` SET `ignores` = ? WHERE UUID = ?");
		ps.setString(1, theList);
		ps.setString(2, this.UUID);
		ps.executeUpdate();
	}

	public static void setSession(String UUID2, String time) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET session_start = ?  WHERE UUID = ?");
		ps.setString(2, UUID2);
		ps.setString(1, time);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(UUID2);
	}

	public String getReason() {
		if ((this.BAN_REASON == null) || (this.BAN_REASON.equals(""))) {
			return ConfigProperties.DEFAULT_BAN_REASON;
		}
		return this.BAN_REASON;
	}

	public String getWhoBanned() {
		return this.WHO_BANNED;
	}

	public static void processNewUser(String UUID, String name) throws SQLException {
		java.util.Date date = MythDate.getNewDate();

		pc.insertPlayer(UUID, name);
		ps = MythSQLConnect.getConnection().prepareStatement(
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
		PlayerDataCache.rebuildUser(UUID);
	}

	public boolean isOverride() throws SQLException {
		return this.IS_OVERRIDE;
	}

	public void setProbate(boolean pro) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET `probated` = ? where UUID = ?");
		ps.setString(1, Boolean.toString(pro));
		ps.setString(2, this.UUID);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(this.UUID);
	}

	public static void setOverride(String UUID, boolean over) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET `override` = ? where UUID = ?");
		ps.setString(1, Boolean.toString(over));
		ps.setString(2, UUID);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(UUID);
	}

	public java.util.Date getExpireDate() throws SQLException {
		return this.EXPIRE_DATE;
	}

	public void clearExpire() throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET `status` = ?, `expires` = ? WHERE `UUID` = ?");
		ps.setString(1, "OK");
		ps.setString(2, null);
		ps.setString(3, this.UUID);
		ps.executeUpdate();
		ps.close();
		PlayerDataCache.rebuildUser(this.UUID);
	}

	public long getPlayTime() {
		return this.PLAY_TIME.longValue();
	}

	public void setName(String name) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_NameCache SET `Name` = ? WHERE `UUID` = ?");
		ps.setString(1, name);
		ps.setString(2, this.UUID);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(this.UUID);
	}

	public boolean getProbate() {
		return this.IS_PROBATED;
	}

	public void setQuitTime(String time) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET last_quit_date = ? WHERE UUID = ?");
		ps.setString(1, time);
		ps.setString(2, this.UUID);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(this.UUID);
	}

	public ResultSet getHistoryPack() throws SQLException {
		ps = MythSQLConnect.getConnection().prepareStatement("SELECT * FROM MythBans_History WHERE UUID = ?");
		ps.setString(1, this.UUID);
		return ps.executeQuery();
	}

	public java.util.Date getQuitDate() throws SQLException {
		return this.QUIT_DATE;
	}

	public java.util.Date getJoinDate() throws SQLException {
		return this.JOIN_DATE;
	}

	public java.util.Date getSessionJoinDate(String UUID) throws SQLException {
		return this.SESSION_START;
	}

	public void setPlayTime(long timeDifference) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET playtime = ?  WHERE UUID = ?");
		ps.setString(2, this.UUID);
		ps.setString(1, Long.toString(timeDifference));
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(this.UUID);
	}

	public void setStatus(String stat) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET status = ?  WHERE UUID = ?");
		ps.setString(2, this.UUID);
		ps.setString(1, stat);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(this.UUID);
	}

	public String getLang() {
		if ((this.LANG_FILE == null) || (this.LANG_FILE.equals(""))) {
			return ConfigProperties.SYSTEM_LOCALE;
		}
		return this.LANG_FILE;
	}

	public String getId() {
		return this.UUID;
	}
}
