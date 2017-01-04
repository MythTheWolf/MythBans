package com.myththewolf.MythBans.lib.SQL;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;




public class DatabaseCommands {
	private Connection c = MythSQLConnect.getConnection();
	private PreparedStatement ps;
	public void muteUser(String UUID, String byUUID) throws SQLException
	{
		ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_PlayerStats SET status = ?, byUUID = ? WHERE UUID = ?");
		ps.setString(1, "muted");
		ps.setString(2, byUUID);
		ps.setString(3, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) c.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`) VALUES (?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userMute");
		ps.setString(3, byUUID);
		ps.executeUpdate();
		
	}
	public void banUser(String UUID,String byUUID, String reason) throws SQLException
	{
		ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_PlayerStats SET status = ?, byUUID = ?, reason = ? WHERE UUID = ?");
		ps.setString(1, "banned");
		ps.setString(2, byUUID);
		ps.setString(3, reason);
		ps.setString(4, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) c.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`,`reason`) VALUES (?,?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userBan");
		ps.setString(3, byUUID);
		ps.setString(4, reason);
		ps.executeUpdate();
	}
	public void tmpBanUser(String UUID,String byUUID, String reason,String expireDate) throws SQLException
	{
		ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_PlayerStats SET status = ?, byUUID = ?, reason = ?,expires = ? WHERE UUID = ?");
		ps.setString(1, "tempBanned");
		ps.setString(2, byUUID);
		ps.setString(3, reason);
		ps.setString(4, expireDate);
		ps.setString(5, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) c.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`,`reason`,`expires`) VALUES (?,?,?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userBan");
		ps.setString(3, byUUID);
		ps.setString(4, reason);
		ps.setString(5, expireDate);
		ps.executeUpdate();
	}
	
	public void kickUser(String UUID, String byUUID,String reason) throws SQLException
	{
		ps = (PreparedStatement) c.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`,`reason`) VALUES (?,?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userKick");
		ps.setString(3, byUUID);
		ps.setString(4, reason);
		ps.executeUpdate();
		ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_PlayerStats SET byUUID = ?,reason = ? WHERE UUID = ?");
		ps.setString(1, byUUID);
		ps.setString(2, reason);
		ps.setString(3, UUID);
		ps.executeUpdate();
	}
	
}
