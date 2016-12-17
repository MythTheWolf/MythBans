package com.myththewolf.MythBans.lib.player;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class Player {
	private ResultSet rs;
	private PreparedStatement ps;
	private PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	public String getStatus(String UUID) throws SQLException
	{
		ps = (PreparedStatement) MythSQLConnect.getConnection().prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		while(rs.next())
		{
			return rs.getString("status");
		}
		return "OK";
	}
	public String getReason(String UUID) throws SQLException
	{
		ps = (PreparedStatement) MythSQLConnect.getConnection().prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		while(rs.next())
		{
			return rs.getString("reason");
		}
		return ConfigProperties.DEFAULT_BAN_REASON;
	}
	
	public String getWhoBanned(String UUID) throws SQLException
	{
		ps = (PreparedStatement) MythSQLConnect.getConnection().prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		while(rs.next())
		{
			String BY = rs.getString("byUUID");
			return pc.getName(BY);
		}
		return ConfigProperties.CONSOLE_UUID;
	}
	public void processNewUser(String UUID, String name) throws SQLException
	{
		pc.insertPlayer(UUID, name);
		ps = (PreparedStatement) MythSQLConnect.getConnection().prepareStatement("INSERT INTO MythBans_PlayerStats ");
	}
	
}
