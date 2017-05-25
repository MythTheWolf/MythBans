package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;

public class MythPlayerIP {
	private Connection con = MythSQLConnect.getConnection();
	private PreparedStatement ps;
	private ResultSet rs;
	private PlayerCache pc = new PlayerCache(con);

	public String getWhoBanned(String IP) throws SQLException {
		
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE IP_ADDRESS = ?");
		ps.setString(1, IP);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("byUUID");
		}
		return "NO_USER_FOUND";
	}

	public String getReason(String IP) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE IP_ADDRESS = ?");
		ps.setString(1, IP);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("reason");
		}
		return "NO_REASON_FOUND";
	}

	public String[] getUUIDPack(String IP) throws SQLException {
		List<String> list = new ArrayList<String>();
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE IP_ADDRESS = ?");
		ps.setString(1, IP);
		rs = ps.executeQuery();
		while (rs.next()) {
			list.add(rs.getString("UUID"));
		}
		String[] arr = new String[list.size()];
		arr = list.toArray(new String[list.size()]);
		return arr;
	}

	public String[] getTheFam(String IP, String UUID) throws SQLException {
		List<String> fam = new ArrayList<String>();
		ps = (PreparedStatement) con
				.prepareStatement("SELECT * FROM MythBans_IPCache WHERE IP_ADDRESS = ? AND UUID != ?");
		ps.setString(1, IP);
		ps.setString(2, UUID);
		rs = ps.executeQuery();
		int theAmount = 0;
		while (rs.next()) {
			fam.add(pc.getName(rs.getString("UUID")));
			theAmount++;
		}

		if (theAmount <= 0) {
			return new String[0];
		} else {
			
			String[] arr = new String[fam.size()];
			arr = fam.toArray(new String[fam.size()]);
			return arr;
		}
	}

	public String[] getIPPack(String UUID) throws SQLException {
		List<String> list = new ArrayList<String>();
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE UUID = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		while (rs.next()) {
			list.add(rs.getString("IP_ADDRESS"));
		}
		String[] arr = new String[list.size()];
		arr = list.toArray(new String[list.size()]);
		return arr;
	}

	public boolean mappedIpExist(String UUID, String IP) throws SQLException {
		ps = (PreparedStatement) con
				.prepareStatement("SELECT * FROM MythBans_IPCache WHERE IP_ADDRESS = ? AND UUID = ?");
		ps.setString(1, IP);
		ps.setString(2, UUID);
		rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;

		}
	}
}
