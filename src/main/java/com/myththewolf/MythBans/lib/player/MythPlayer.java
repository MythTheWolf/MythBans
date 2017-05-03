package com.myththewolf.MythBans.lib.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerDataCache;

/**
 * Contains all getters for a player,main SQL table is MythBans_PlayerStats.
 * 
 * @author MythTheWolf
 *
 */
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
	private static String UUID;
	private Long PLAY_TIME;
	private Date QUIT_DATE;
	private Date JOIN_DATE;
	private Date SESSION_START;
	private String LANG_FILE;
	private boolean IS_PROBATED;

	/**
	 * The Main Constructor for this class, this constructor must be called.
	 * 
	 * @author MythTheWolf
	 * @param theUUID
	 *            - Player's UUID
	 */
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
				if (rs.getString("last_quit_date") == null || rs.getString("last_quit_date").equals("")) {
					QUIT_DATE = null;
				} else {
					QUIT_DATE = MythDate.parseDate(rs.getString("last_quit_date"));
				}
				JOIN_DATE = MythDate.parseDate(rs.getString("timestamp"));
				if (rs.getString("session_start") == null) {
					SESSION_START = MythDate.getNewDate();
					setSession(UUID, MythDate.formatDate(MythDate.getNewDate()));
				} else {
					SESSION_START = MythDate.parseDate(rs.getString("session_start"));
				}
				LANG_FILE = rs.getString("lang_file");
				IS_PROBATED = rs.getBoolean("probated");
			}
		} catch (

		SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Get the player's current status from the cache created by constructor
	 * 
	 * @see MythPlayer
	 * @return String - The Player's current status
	 * 
	 */
	public String getStatus() {
		return PLAYER_STATUS;
	}

	/**
	 * Set a player's session start in the database
	 * 
	 * @param UUID2
	 *            - The player's UUID
	 * @param time
	 *            - The time in a string format
	 * @throws SQLException
	 */
	public static void setSession(String UUID2, String time) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET session_start = ?  WHERE UUID = ?");
		ps.setString(2, UUID2);
		ps.setString(1, time);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(UUID);

	}

	/**
	 * 
	 * @return String - Ban Reason: The reason a player is banned. <br />
	 *         <b>Note: </b>If the reason is null or empty, the return is the
	 *         default ban reason.
	 */
	public String getReason() {
		if (BAN_REASON == null || BAN_REASON.equals("")) {
			return ConfigProperties.DEFAULT_BAN_REASON;
		} else {
			return BAN_REASON;
		}
	}

	/**
	 * 
	 * @return String - The UUID of the user who executed the punishment. <br />
	 *         <b>Note: </b>Returns "CONSOLE" if the console was the command
	 *         sender.
	 */
	public String getWhoBanned() {
		return WHO_BANNED;
	}

	/**
	 * Adds a new player to the database
	 * 
	 * @param UUID
	 *            - User UUID
	 * @param name
	 *            - User name
	 * @throws SQLException
	 */
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
		PlayerDataCache.rebuildUser(UUID);
	}

	/**
	 * Gets the player's override value.
	 * 
	 * @return Boolean - If the softmute value is overriden.
	 * @throws SQLException
	 */
	public boolean isOverride() throws SQLException {
		return IS_OVERRIDE;
	}

	/**
	 * Set's a users probate value
	 * 
	 * @param pro
	 *            - The probate value
	 * @throws SQLException
	 */
	public void setProbate(boolean pro) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET `probated` = ? where UUID = ?");
		ps.setString(1, Boolean.toString(pro));
		ps.setString(2, UUID);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(UUID);
	}

	/**
	 * Set's a users Override value
	 * 
	 * @param 
	 * UUID - The Player's UUID
	 * @param 
	 * over - The Override value
	 * @throws SQLException
	 */
	public static void setOverride(String UUID, boolean over) throws SQLException {
		ps = MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET `override` = ? where UUID = ?");
		ps.setString(1, Boolean.toString(over));
		ps.setString(2, UUID);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(UUID);
	}
	/**
	 * 
	 * @return The expire date of a tempban
	 * @throws SQLException
	 * @see com.myththewolf.MythBans.lib.tool.Date
	 */
	public Date getExpireDate() throws SQLException {
		return EXPIRE_DATE;
	}
	/**
	 * Clears a player's expire date. Purely just for database cosmetics.
	 * @throws SQLException
	 */
	public void clearExpire() throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET `status` = ?, `expires` = ? WHERE `UUID` = ?");
		ps.setString(1, "OK");
		ps.setString(2, null);
		ps.setString(3, UUID);
		ps.executeUpdate();
		ps.close();
		PlayerDataCache.rebuildUser(UUID);
	}
	/**
	 * 
	 * @return Long - The player's total time in miliseconds
	 */
	public long getPlayTime() {
		return PLAY_TIME;
	}

	public boolean getProbate() {
		return IS_PROBATED;
	}

	public void setQuitTime(String time) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET last_quit_date = ? WHERE UUID = ?");
		ps.setString(1, time);
		ps.setString(2, UUID);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(UUID);
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

	public void setPlayTime(long timeDifference) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET playtime = ?  WHERE UUID = ?");
		ps.setString(2, UUID);
		ps.setString(1, Long.toString(timeDifference));
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(UUID);
	}

	public void setStatus(String stat) throws SQLException {
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("UPDATE MythBans_PlayerStats SET status = ?  WHERE UUID = ?");
		ps.setString(2, UUID);
		ps.setString(1, stat);
		ps.executeUpdate();
		PlayerDataCache.rebuildUser(UUID);

	}

	public String getLang() {
		if (LANG_FILE == null || LANG_FILE.equals("")) {
			return ConfigProperties.SYSTEM_LOCALE;
		} else {
			return LANG_FILE;
		}
	}

	public String getId() {
		return UUID;
	}
}
