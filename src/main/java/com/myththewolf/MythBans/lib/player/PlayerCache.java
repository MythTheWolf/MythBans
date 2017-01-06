package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;



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
	public void insertPlayer(String UUID, String name) throws SQLException
	{
		ps = (PreparedStatement) con.prepareStatement("INSERT INTO MythBANS_NameCache (`UUID`,`name`) VALUES (?,?);");
		ps.setString(1, UUID);
		ps.setString(2, name);
		ps.executeUpdate();
		
	}
	
	public String getName(String UUID) throws SQLException
	{
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_NameCache WHERE `UUID` = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		if(!rs.next())
		{
			return null;
		}else{
			return rs.getString("Name");
		}
	}
	public boolean ipExist(String IP) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE `IP_ADDRESS` = ?");
		ps.setString(1, IP);
		rs = ps.executeQuery();
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}
	public void addIP(String UUID,String IP) throws SQLException
	{
		ps = (PreparedStatement) con.prepareStatement("INSERT INTO MythBans_IPCache (`IP_ADDRESS`,`UUID`,`Status`) VALUES (?,?,?);");
		ps.setString(1, IP);
		ps.setString(2, UUID);
		ps.setString(3, "OK");
	}
	public String getUUIDbyIP(String IP) throws SQLException{
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE `IP_ADDRESS` = ?");
		ps.setString(1, IP);
		rs = ps.executeQuery();
		if(rs.next()){
			return rs.getString("UUID");
		}else{
			return null;
		}
	}
}
