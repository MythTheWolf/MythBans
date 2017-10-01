package com.myththewolf.MythBans.lib.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class ChatChannel {
	private String NAME;
	private String NODE;
	private String PREFIX;
	private String SHORTCUT;
	private ResultSet rs;
	private PreparedStatement ps;
	private Connection con;
	private HashMap<String, String> chanMap;
	private boolean EXIST;

	public ChatChannel() throws SQLException {
		this.chanMap = new HashMap<String, String>();
		this.con = com.myththewolf.MythBans.lib.SQL.MythSQLConnect.getConnection();
		this.ps = this.con.prepareStatement("SELECT * FROM MythBans_Channels");
		this.rs = this.ps.executeQuery();
		while (this.rs.next()) {
			this.chanMap.put(this.rs.getString("shortcut"), this.rs.getString("name"));
		}
	}

	public HashMap<String, String> getMap() {
		return this.chanMap;
	}

	public ChatChannel(String name) throws SQLException {
		this.NAME = name;
		this.NODE = "mythbans.nullperm";
		this.PREFIX = ("[" + name + "]");
		this.EXIST = true;
		this.SHORTCUT = "undefinedmythbansshortcut";
		this.con = com.myththewolf.MythBans.lib.SQL.MythSQLConnect.getConnection();
		this.ps = this.con.prepareStatement("SELECT * FROM MythBans_Channels WHERE `name` = ?");
		this.ps.setString(1, name);
		this.rs = this.ps.executeQuery();
		if (!this.rs.next()) {
			this.EXIST = false;
			return;
		}

		this.NAME = this.rs.getString("name");
		this.NODE = this.rs.getString("node");
		this.PREFIX = this.rs.getString("prefix");
		this.SHORTCUT = this.rs.getString("shortcut");
	}

	public void push(Player send, String message) throws SQLException {
		Logger log = Bukkit.getServer().getLogger();
		MythPlayer sen = new MythPlayer(send.getUniqueId().toString());
		String who = send.getDisplayName();
		String template = "";
		log.info("["+this.NAME+"]"+send.getName()+": "+ message);
		if (send.hasPermission("essentials.chat.color")) {
			template = ChatColor.WHITE + "<" + who + ChatColor.WHITE + "> "
					+ ChatColor.translateAlternateColorCodes('&', message);
		} else {
			template = ChatColor.WHITE + "<" + who + ChatColor.WHITE + "> " + message;
		}
		for (Player i : org.bukkit.Bukkit.getOnlinePlayers()) {
			MythPlayer get = new MythPlayer(i.getUniqueId().toString());
			if (get.isIgnoring(sen.getId())) {

			} else if (i.hasPermission(ConfigProperties.STAFF_CHAT_GET)) {
				if (sen.getProbate()) {
					i.sendMessage(this.PREFIX + ChatColor.RED + "* " + template);
				} else {
					i.sendMessage(this.PREFIX + template);
				}
			} else if ((get.getChannel().equals(this.NAME)) || (i.hasPermission(ConfigProperties.STAFF_CHAT_GET))) {

				if (get.getChannel().equals(this.NAME)) {
					i.sendMessage(template);
				}
			}
		}
	}

	public boolean canUse(Player p) {
		return p.hasPermission(this.NODE);
	}

	public boolean exists() {
		return this.EXIST;
	}

	public void create() {
	}

	public void setPrefix(String name) {
		this.PREFIX = name;
	}

	public void setShortcut(String shor) {
		this.SHORTCUT = shor;
	}

	public void setNode(String perm) {
	}

	public java.util.List<String> getOnline() throws SQLException {
		java.util.List<String> tmp = new java.util.ArrayList<String>();
		for (Player p : org.bukkit.Bukkit.getOnlinePlayers()) {
			MythPlayer M = new MythPlayer(p.getUniqueId().toString());
			if ((M.getChannel().equals(this.NAME)) || (p.hasPermission(ConfigProperties.STAFF_CHAT_GET))) {
				tmp.add(p.getUniqueId().toString());
			}
		}
		return tmp;
	}

	public String getNode() {
		return this.NODE;
	}

	public void compile() {
	}

	public String getShortcut() {
		return this.SHORTCUT;
	}
}
