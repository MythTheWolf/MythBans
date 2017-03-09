package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.Discord;
import com.myththewolf.MythBans.lib.DiscordConnection;

import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissionsBuilder;

public class PlayerCache {
	private ResultSet rs;
	private PreparedStatement ps;
	private Connection con;

	public PlayerCache(Connection j) {
		con = j;
	}

	public OfflinePlayer getOfflinePlayerExact(String name) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_NameCache WHERE name = ?");
		ps.setString(1, name);
		rs = ps.executeQuery();
		if (!rs.next()) {
			return null;
		} else {
			return Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID")));
		}
	}

	public Player getPlayerExact(String name) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_NameCache WHERE `name` = ?");
		ps.setString(1, name);
		rs = ps.executeQuery();
		if (!rs.next()) {
			return null;
		} else {
			return Bukkit.getPlayer(UUID.fromString(rs.getString("UUID")));
		}
	}

	public void updatePlayer(String UUID, String name) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("UPDATE MythBans_NameCache SET `name` = ? WHERE `UUID` = ?");
		ps.setString(1, name);
		ps.setString(2, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) con.prepareStatement("UPDATE MythBans_PlayerStats SET `name` = ? WHERE `UUID` = ?");
		ps.setString(1, name);
		ps.setString(2, UUID);
		ps.executeUpdate();
		ps.close();
	}

	public void insertPlayer(String UUID, String name) throws SQLException {
		ps = (PreparedStatement) con
				.prepareStatement("INSERT INTO MythBans_NameCache (`UUID`,`name`,`discord_id`) VALUES (?,?,?);");
		ps.setString(1, UUID);
		ps.setString(2, name);
		ps.setString(3, "NOP");
		ps.executeUpdate();
		ps.close();

	}

	public String getName(String UUID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_NameCache WHERE `UUID` = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		if (!rs.next()) {
			return null;
		} else {
			return rs.getString("Name");
		}
	}

	public String getUUID(String name) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_NameCache WHERE `Name` = ?");
		ps.setString(1, name);
		rs = ps.executeQuery();
		if (!rs.next()) {
			return null;
		} else {
			return rs.getString("UUID");
		}
	}

	public boolean ipExist(String IP) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE `IP_ADDRESS` = ?");
		ps.setString(1, IP);
		rs = ps.executeQuery();
		if (!rs.next()) {
			return false;
		} else {
			return true;
		}
	}

	public void addIP(String UUID, String IP) throws SQLException {
		ps = (PreparedStatement) con
				.prepareStatement("INSERT INTO MythBans_IPCache (`IP_ADDRESS`,`UUID`,`Status`) VALUES (?,?,?);");
		ps.setString(1, IP);
		ps.setString(2, UUID);
		ps.setString(3, "OK");
		ps.executeUpdate();
	}

	public String[] getIPbyUUID(String UUID) throws SQLException {
		List<String> IPs = new ArrayList<String>();
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE `UUID` = ?");
		ps.setString(1, UUID);
		rs = ps.executeQuery();
		int count = 0;
		while (rs.next()) {
			IPs.add(rs.getString("IP_ADDRESS"));
			count++;
		}
		if (count <= 0) {
			return null;
		}
		String[] arr = new String[IPs.size()];
		arr = IPs.toArray(new String[IPs.size()]);
		return arr;
	}

	public String[] getUUIDbyIP(String IP) throws SQLException {
		List<String> IPs = new ArrayList<String>();
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_IPCache WHERE `IP_ADDRESS` = ?");
		ps.setString(1, IP);
		rs = ps.executeQuery();
		int count = 0;
		while (rs.next()) {
			IPs.add(rs.getString("UUID"));
			count++;
		}
		if (count <= 0) {
			return null;
		}
		String[] arr = new String[IPs.size()];
		arr = IPs.toArray(new String[IPs.size()]);
		return arr;
	}

	public boolean isLinked(String d_id) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_NameCache WHERE `discord_id` = ?");
		ps.setString(1, d_id);
		rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	public void bindSecret(String secret, String ID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("DELETE FROM MythBans_Discord WHERE `value` = ?");
		ps.setString(1, ID);
		ps.executeUpdate();
		ps = (PreparedStatement) con.prepareStatement("INSERT INTO MythBans_Discord (`key`,value) VALUES ( ? , ? )");
		ps.setString(1, secret);
		ps.setString(2, ID);
		ps.executeUpdate();
	}

	public boolean secretExists(String secret) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Discord WHERE `key` = ?");
		ps.setString(1, secret);
		rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	public void linkDiscord(String secret, String UUID) throws SQLException {
		Discord disc = new Discord(DiscordConnection.getConnection());
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Discord WHERE `key` = ?");
		ps.setString(1, secret);
		rs = ps.executeQuery();
		String ID = "";
		while (rs.next()) {
			ID = rs.getString("value");
		}
		ps = (PreparedStatement) con
				.prepareStatement("UPDATE MythBans_NameCache SET `discord_id` = ? WHERE `UUID` = ?");
		ps.setString(1, ID);
		ps.setString(2, UUID);
		ps.executeUpdate();
		ps = (PreparedStatement) con.prepareStatement("DELETE FROM MythBans_Discord WHERE `value` = ?");
		ps.setString(1, ID);
		ps.executeUpdate();
		ImplPermissionsBuilder IM = new ImplPermissionsBuilder();
		IM.setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED);
		Permissions PERMS = IM.build();
		disc.getMinecraft().updateOverwrittenPermissions(DiscordConnection.getConnection().getCachedUserById(ID),
				PERMS);
	}
}
