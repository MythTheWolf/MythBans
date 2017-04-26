package com.myththewolf.MythBans.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class UpdateXenForo implements CommandExecutor {

	public UpdateXenForo() {
		// TODO Auto-generated constructor stub
	}

	private Connection con;
	private ResultSet rs;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof ConsoleCommandSender)) {
			return true;
		}
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://" + ConfigProperties.XEN_SQL_HOST + ":" + ConfigProperties.XEN_SQL_PORT + "/"
							+ ConfigProperties.XEN_SQL_DATABASE + "?useUnicode=true&characterEncoding=UTF-8",
					ConfigProperties.XEN_SQL_USERNAME, ConfigProperties.XEN_SQL_PASSWORD);
		} catch (SQLException e1) {
			System.out.println("COULDNT CONNECT TO XENFORO!");
			e1.printStackTrace();
		}
		// String GROUP_ADD = args[1];

		try {
			String USERNAME = "";
			String XF_ID = "";
			PreparedStatement ps = (PreparedStatement) con
					.prepareStatement("SELECT * FROM `apms2_key` WHERE `uuid` = ?");
			ps.setString(1, args[0]);
			rs = ps.executeQuery();
			if (!rs.next()) {
				System.out.println("FATAL: No linked user matching UUID----> " + args[0]);
				return true;
			}
			XF_ID = rs.getString("user_id");
			ps = (PreparedStatement) con.prepareStatement("SELECT * FROM `xf_user` WHERE `user_id` = ?");
			ps.setString(1, XF_ID);
			rs = ps.executeQuery();
			if (!rs.next()) {
				System.out.println("FATAL: No user matching xenForo ID----> " + XF_ID);
				return true;
			}
			USERNAME = rs.getString("username");
			
			URL xenForo = new URL(ConfigProperties.API_URL + "?action=editUser&hash=" + ConfigProperties.API_KEY
					+ "&user=" + USERNAME + "&add_groups=Verified");
			System.out.println(xenForo.toString());
			HttpURLConnection yc = (HttpURLConnection) xenForo.openConnection();
			if (yc.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {

			} else {
				/* error from server */

				BufferedReader BUFF = new BufferedReader(new InputStreamReader(yc.getErrorStream()));

				String inputLine;

				while ((inputLine = BUFF.readLine()) != null) {
					System.out.println(inputLine);
				}
				BUFF.close();

			}
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
			in.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return true;
	}

}
