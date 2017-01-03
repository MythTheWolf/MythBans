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

	public String getStatus(String UUID) throws SQLException
	{
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next())
		{
			return rs.getString("status");
		}
		return "OK";
	}

	public String getReason(String UUID) throws SQLException
	{
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next())
		{
			if(rs.getString("reason").equals("") || rs.getString("reason").equals(" ") || rs.getString("reason") == null)
			{
				return ConfigProperties.DEFAULT_BAN_REASON;
			}
			return rs.getString("reason");
		}
		return ConfigProperties.DEFAULT_BAN_REASON;
	}

	public String getWhoBanned(String UUID) throws SQLException
	{
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next())
		{
			return rs.getString("byUUID");
		}
		return ConfigProperties.CONSOLE_UUID;
	}

	public void processNewUser(String UUID, String name) throws SQLException
	{
		pc.insertPlayer(UUID, name);
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("INSERT INTO MythBans_PlayerStats (`UUID`,`status`,`group`) VALUES (?,?,?);");
		ps.setString(1, UUID);
		ps.setString(2, "OK");
		ps.setString(3, "DEFAULT");
		ps.executeUpdate();
	}

	public Date getExpireDate(String UUID) throws SQLException
	{
		ps = (PreparedStatement) MythSQLConnect.getConnection()
				.prepareStatement("SELECT * FROM MythBans_PlayerStats WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next())
		{
			return MythDate.parseDate(rs.getString("expire"));
		}
		return MythDate.parseDate(rs.getString("expire"));
	}

	public void clearExpire(String UUID) throws SQLException
	{
		ps = (PreparedStatement) MythSQLConnect.getConnection().prepareStatement("UPDATE MythBans_PlayerStats SET `staus` = ?, `expire` = ? WHERE `UUID` = ?");
		ps.setString(1, UUID);
		ps.setString(2, "OK");
		ps.setString(3, null);
		ps.executeUpdate();
	}
}
