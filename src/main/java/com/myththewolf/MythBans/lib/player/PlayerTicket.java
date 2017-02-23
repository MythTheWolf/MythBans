package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerTicket {
	private PreparedStatement ps;
	private Connection con;
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
}
