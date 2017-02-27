package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;

public class PlayerTicket {
	private PreparedStatement ps;
	private Connection con = MythSQLConnect.getConnection();
	private ResultSet rs;

	public void recordGreif(String UUID, String timestamp, String location, String text) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement(
				"INSERT INTO MythBans_Tickets (`PRIORITY`,`SENDER_UUID`,`status`,`message`,`location`) VALUES (?,?,?,?,?)");
		ps.setString(1, "HIGH");
		ps.setString(2, UUID);
		ps.setString(3, "OPEN");
		ps.setString(4, text);
		ps.setString(5, location);
		ps.executeUpdate();
	}

	public boolean exists(String ticket_id) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE ID = ?");
		ps.setString(1, ticket_id);
		rs = ps.executeQuery();
		if (!rs.next()) {
			return false;
		} else {
			return true;
		}
	}

	public String getLocation(String ticket_id) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE ID = ?");
		ps.setString(1, ticket_id);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("location");
		}
		return null;
	}

	public int getUnclosed() throws SQLException {
		int count = 0;
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE status = ?");
		ps.setString(1, "OPEN");
		rs = ps.executeQuery();
		while (rs.next()) {
			count++;
		}
		return count;
	}

	public void closeTicket(String ID, String handle, String text) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement(
				"UPDATE MythBans_Tickets SET status = ?, handler = ?, close_message = ? WHERE ID = ?");
		ps.setString(1, "CLOSED");
		ps.setString(2, handle);
		ps.setString(3, text);
		ps.setString(4, ID);
		ps.executeUpdate();
	}
}
