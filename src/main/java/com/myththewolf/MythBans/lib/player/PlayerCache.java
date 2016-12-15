package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.mysql.jdbc.PreparedStatement;

public class PlayerCache {
	private ResultSet rs;
	private PreparedStatement ps;
	private Connection con;
	public PlayerCache(Connection j)
	{
		con = j;
	}
	public OfflinePlayer getOfflinePlayerExact(String name) throws SQLException
	{
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_NameCache WHERE name = ?");
		ps.setString(1, name);
		rs = ps.executeQuery();
		if(!rs.next())
		{
			return null;
		}else{
			return Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID")));
		}
	}
	public Player getPlayerExact(String name) throws SQLException
	{
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_NameCache WHERE `name` = ?");
		ps.setString(1, name);
		rs = ps.executeQuery();
		if(!rs.next())
		{
			return null;
		}else{
			return Bukkit.getPlayer(UUID.fromString(rs.getString("UUID")));
		}
	}
	public void updatePlayer(String UUID, String name) throws SQLException
	{
		ps = (PreparedStatement) con.prepareStatement("UPDATE MythBans_NameCache SET `name` = ? WHERE `UUID` = ?");
		ps.setString(1, name);
		ps.setString(2, UUID);
		ps.executeUpdate();
	}
}
