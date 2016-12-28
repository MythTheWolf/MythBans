package com.myththewolf.MythBans.lib.SQL;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;


public class DatabaseCommands {
	private Connection c = MythSQLConnect.getConnection();
	private PreparedStatement ps;
	public void muteUser(String UUID, String byUUID) throws SQLException
	{
		ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_PlayerStatus SET status = ?, byUUID = ? WHERE UUID = ?");
		ps.setString(1, "muted");
		ps.setString(2, byUUID);
		ps.setString(3, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) c.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`) (?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userMute");
		ps.setString(3, byUUID);
		ps.executeUpdate();
		
	}
}
