package com.myththewolf.MythBans.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.tool.Date;

public class Dump implements CommandExecutor {

	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender send, Command arg1, String arg2, String[] args) {
		
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		if (!(send instanceof ConsoleCommandSender)) {
			send.sendMessage(ConfigProperties.PREFIX + "You must be console to execute this command.");
			return true;
		} else {
			Connection theConnection = MythSQLConnect.getConnection();
			try {
				PreparedStatement ps = theConnection
						.prepareStatement("SELECT * FROM `MythBans_PlayerStats` WHERE `status` = ?");
				ps.setString(1, "banned");
				ResultSet rs = ps.executeQuery();
				JSONArray root = new JSONArray();
			
				while (rs.next()) {
					
					String UUID = rs.getString("UUID");
					String cachedName = rs.getString("last_Name");
					String SOURCE = "";
					if (rs.getString("byUUID").equals("CONSOLE")) {
						SOURCE = "CONSOLE";
					} else {
						MythPlayer MP = DataCache.getPlayerInstance(rs.getString("byUUID"));
						SOURCE = MP.getUsername();
					}
					JSONObject ob = new JSONObject();
					ob.put("uuid", UUID);
					ob.put("name", cachedName);
					ob.put("source", SOURCE);
					ob.put("expires", "forever");
					ob.put("created", (new Date()).toString());
					ob.put("reason", rs.getString("reason"));
					root.add(ob);
				}

				String FINAL = root.toJSONString();
				System.out.println(FINAL);
				fw = new FileWriter(new File("banned-players.json").getAbsolutePath(),false);
				bw = new BufferedWriter(fw);
				bw.write(FINAL);

			} catch (IOException e) {

				e.printStackTrace();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (bw != null)
					try {
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				if (fw != null)
					try {
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			}
		}
		return true;
	}
}
