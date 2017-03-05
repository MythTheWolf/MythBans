package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import org.bukkit.entity.Player;

public class PlayerTicket {
	private PreparedStatement ps;
	private Connection con = MythSQLConnect.getConnection();
	private ResultSet rs;

	public void recordTicket(String UUID, String timestamp, String location, String text,String pri) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement(
				"INSERT INTO MythBans_Tickets (`PRIORITY`,`SENDER_UUID`,`status`,`message`,`location`) VALUES (?,?,?,?,?)");
		ps.setString(1, pri);
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
				"UPDATE MythBans_Tickets SET status = ?, handler = ?, close_message = ?, user_seen = ? WHERE ID = ?");
		ps.setString(1, "CLOSED");
		ps.setString(2, handle);
		ps.setString(3, text);
		ps.setString(4, "NO");
		ps.setString(5, ID);
		ps.executeUpdate();
	}

	public String getSender(String ID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE ID = ?");
		ps.setString(1, ID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("SENDER_UUID");
		}
		return null;
	}

	public void getTickets(CommandSender sender, int page, int pageLength) throws SQLException {
		SortedMap<Integer, String> map = new TreeMap<Integer, String>(Collections.reverseOrder());
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE status = ?");
		ps.setString(1, "OPEN");
		rs = ps.executeQuery();
		while (rs.next()) {
			String KEY = "";
			switch (this.getPriority(rs.getString("ID"))) {
			case "HIGH":
				KEY = ChatColor.DARK_RED + rs.getString("message");
				break;
			case "MEDIUM":
				KEY = ChatColor.YELLOW + rs.getString("message");
				break;
			case "LOW":
				KEY = ChatColor.GREEN + rs.getString("message");
				break;
			}
			map.put(rs.getInt("ID"), KEY);
		}
		paginate(sender, map, page, 4);
	}

	private boolean paginate(CommandSender sender, SortedMap<Integer, String> map, int page, int pageLength) {
		int len = (((map.size() % pageLength) == 0) ? map.size() / pageLength : (map.size() / pageLength) + 1);
		if (page > len) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Out of bounds, requsted " + page
					+ " out of maximum of " + len);
			return false;
		}
		sender.sendMessage(ChatColor.GOLD + "------------------" + ChatColor.GREEN + " Ticket List " + ChatColor.GOLD
				+ "------------------");
		String at = String.valueOf(page);
		int i = 0, k = 0;
		page--;
		for (final Entry<Integer, String> e : map.entrySet()) {
			k++;
			if ((((page * pageLength) + i + 1) == k) && (k != ((page * pageLength) + pageLength + 1))) {
				i++;
				sender.sendMessage(ChatColor.YELLOW + " #" + e.getKey() + ": " + e.getValue());
			}
		}
		sender.sendMessage(ChatColor.YELLOW + "Page " + at + " of "
				+ (((map.size() % pageLength) == 0) ? map.size() / pageLength : (map.size() / pageLength) + 1));
		sender.sendMessage(ChatColor.GREEN + "Use /tickets <page> to view more pages.");
		sender.sendMessage(ChatColor.GOLD + "------------------" + ChatColor.GOLD + "------------" + ChatColor.GOLD
				+ "-----------------");
		return true;
	}

	public void getMyTickets(CommandSender sender, int page, int max) throws SQLException {
		SortedMap<Integer, String> map = new TreeMap<Integer, String>(Collections.reverseOrder());
		ps = (PreparedStatement) con
				.prepareStatement("SELECT * FROM MythBans_Tickets WHERE status = ? AND SENDER_UUID = ?");
		ps.setString(1, "OPEN");
		ps.setString(2, ((Player) sender).getUniqueId().toString());
		rs = ps.executeQuery();
		while (rs.next()) {
			map.put(rs.getInt("ID"), rs.getString("message"));
		}
		paginate(sender, map, page, 4);
	}

	public String getStatus(String ID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE ID = ?");
		ps.setString(1, ID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("status");
		}
		return null;
	}

	public String getHandler(String ID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE ID = ?");
		ps.setString(1, ID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("handler");
		}
		return null;
	}

	public String getClose(String ID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE ID = ?");
		ps.setString(1, ID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("close_message");
		}
		return null;
	}

	public String getMessage(String ID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE ID = ?");
		ps.setString(1, ID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("message");
		}
		return null;
	}

	public void getClosedTickets(CommandSender sender, int page, int pageLength) throws SQLException {
		SortedMap<Integer, String> map = new TreeMap<Integer, String>(Collections.reverseOrder());
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE status = ?");
		ps.setString(1, "CLOSED");
		rs = ps.executeQuery();
		while (rs.next()) {
			map.put(rs.getInt("ID"), rs.getString("message"));
		}
		paginate(sender, map, page, 4);
	}

	public void getMyClosedTickets(CommandSender sender, int page, int max) throws SQLException {
		SortedMap<Integer, String> map = new TreeMap<Integer, String>(Collections.reverseOrder());
		ps = (PreparedStatement) con
				.prepareStatement("SELECT * FROM MythBans_Tickets WHERE status = ? AND SENDER_UUID = ?");
		ps.setString(1, "CLOSED");
		ps.setString(2, ((Player) sender).getUniqueId().toString());
		rs = ps.executeQuery();
		while (rs.next()) {
			map.put(rs.getInt("ID"), rs.getString("message"));
		}
		paginate(sender, map, page, 4);
	}

	public String getPriority(String ID) throws SQLException {
		ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MythBans_Tickets WHERE ID = ?");
		ps.setString(1, ID);
		rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getString("PRIORITY");
		}
		return null;
	}
}
