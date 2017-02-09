package com.myththewolf.MythBans.lib.SQL;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DatabaseCommands {
	private Connection c = MythSQLConnect.getConnection();
	private PreparedStatement ps;

	public void muteUser(String UUID, String byUUID) throws SQLException {
		ps = (PreparedStatement) c
				.prepareStatement("UPDATE MythBans_PlayerStats SET status = ?, byUUID = ? WHERE UUID = ?");
		ps.setString(1, "muted");
		ps.setString(2, byUUID);
		ps.setString(3, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) c
				.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`) VALUES (?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userMute");
		ps.setString(3, byUUID);
		ps.executeUpdate();

	}

	public void banUser(String UUID, String byUUID, String reason) throws SQLException {
		ps = (PreparedStatement) c
				.prepareStatement("UPDATE MythBans_PlayerStats SET status = ?, byUUID = ?, reason = ? WHERE UUID = ?");
		ps.setString(1, "banned");
		ps.setString(2, byUUID);
		ps.setString(3, reason);
		ps.setString(4, UUID);
		ps.executeUpdate();
		ps.close();
		ps = (PreparedStatement) c
				.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`,`reason`) VALUES (?,?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userBan");
		ps.setString(3, byUUID);
		ps.setString(4, reason);
		ps.executeUpdate();
		ps.close();
	}

	public void tmpBanUser(String UUID, String byUUID, String reason, String expireDate) throws SQLException {
		ps = (PreparedStatement) c.prepareStatement(
				"UPDATE MythBans_PlayerStats SET status = ?, byUUID = ?, reason = ?,expires = ? WHERE UUID = ?");
		ps.setString(1, "tempBanned");
		ps.setString(2, byUUID);
		ps.setString(3, reason);
		ps.setString(4, expireDate);
		ps.setString(5, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) c.prepareStatement(
				"INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`,`reason`,`expires`) VALUES (?,?,?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userTempBan");
		ps.setString(3, byUUID);
		ps.setString(4, reason);
		ps.setString(5, expireDate);
		ps.executeUpdate();
	}

	public void kickUser(String UUID, String byUUID, String reason) throws SQLException {
		ps = (PreparedStatement) c
				.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`,`reason`) VALUES (?,?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userKick");
		ps.setString(3, byUUID);
		ps.setString(4, reason);
		ps.executeUpdate();
		ps = (PreparedStatement) c
				.prepareStatement("UPDATE MythBans_PlayerStats SET byUUID = ?,reason = ? WHERE UUID = ?");
		ps.setString(1, byUUID);
		ps.setString(2, reason);
		ps.setString(3, UUID);
		ps.executeUpdate();
	}

	public void setIP(Player p) throws SQLException {
		ps = (PreparedStatement) c.prepareStatement("SELECT * FROM MythBans_IPCache WHERE IP_ADDRESS = ?");
		ps.setString(1, p.getAddress().getAddress().toString());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_IPCache SET IP_ADDRESS = ? WHERE `UUID` = ?");
			ps.setString(1, p.getAddress().getAddress().toString());
			ps.setString(2, p.getUniqueId().toString());
		} else {
			ps = (PreparedStatement) c
					.prepareStatement("INSERT INTO MythBans_IPCache (`IP_ADDRESS`,`UUID`,`Status`) VALUES (?,?,?)");
			ps.setString(1, p.getAddress().getAddress().toString());
			ps.setString(2, p.getUniqueId().toString());
			ps.setString(3, "OK");
			ps.executeUpdate();
		}
	}

	public String getStoredIP(String UUID) throws SQLException {
		ps = (PreparedStatement) c.prepareStatement("SELECT * FROM MythBans_IPCache WHERE `UUID` = ?");
		ps.setString(1, UUID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("IP_ADDRESS");
		}
		return null;
	}

	public String getIPStatus(String IP) throws SQLException {
		ps = (PreparedStatement) c.prepareStatement("SELECT * FROM MythBans_IPCache WHERE `IP_ADDRESS` = ?");
		ps.setString(1, IP);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("Status");
		}
		return null;
	}

	public void setProbation(String UUID, String byUUID, String reason) throws SQLException {
		ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_PlayerStats SET status = ? WHERE UUID = ?");
		ps.setString(1, "trial");
		ps.setString(2, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) c
				.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`,`reason`) VALUES (?,?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userProbate");
		ps.setString(3, byUUID);
		ps.setString(4, reason);
		ps.executeUpdate();
	}

	public void banIP(String IP, String byUUID, String reason) throws SQLException {
		ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_IPCache SET status = ? WHERE IP_ADDRESS = ?");
		ps.setString(1, "banned");
		ps.setString(2, IP);
		ps.executeUpdate();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getAddress().getAddress().toString().equals(IP)) {
				ps = (PreparedStatement) c
						.prepareStatement("UPDATE MythBans_PlayerStats SET byUUID = ?, reason = ? WHERE UUID = ?");
				ps.setString(1, byUUID);
				ps.setString(2, reason);
				ps.setString(3, p.getUniqueId().toString());
				ps.executeUpdate();
			}
		}

		ps = (PreparedStatement) c
				.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`,`reason`) VALUES (?,?,?,?)");
		ps.setString(1, IP);
		ps.setString(2, "IPBan");
		ps.setString(3, byUUID);
		ps.setString(4, reason);
		ps.executeUpdate();
	}

	public void unProbate(String UUID, String byUUID) throws SQLException {
		ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_PlayerStats SET status = ? WHERE UUID = ?");
		ps.setString(1, "OK");
		ps.setString(2, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) c
				.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`) VALUES (?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "unProbate");
		ps.setString(3, byUUID);
		ps.executeUpdate();
	}

	public void cleanUser(String UUID) throws SQLException {
		ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_PlayerStats SET status = ?,byUUID = ?,reason = ? WHERE UUID = ?");
		ps.setString(1, "OK");
		ps.setString(2, "");
		ps.setString(3, "");
		ps.setString(4, UUID);
		ps.executeUpdate();
	}
	
	public void pardonUser(String UUID,String byUUID) throws SQLException {
		ps = (PreparedStatement) c.prepareStatement("UPDATE MythBans_PlayerStats SET status = ? WHERE UUID = ?");
		ps.setString(1, "OK");
		ps.setString(2, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) c
				.prepareStatement("INSERT INTO MythBans_History (`UUID`,`action`,`byUUID`) VALUES (?,?,?)");
		ps.setString(1, UUID);
		ps.setString(2, "userPardon");
		ps.setString(3, byUUID);
		ps.executeUpdate();
	}
}
