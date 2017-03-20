package com.myththewolf.MythBans.commands;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Group;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.SiteUser;
import com.myththewolf.MythBans.lib.tool.Utils;

import net.md_5.bungee.api.ChatColor;

public class createUI implements CommandExecutor {
	private Group group = new Group();
	private SiteUser SU = new SiteUser();
	private PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());

	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /createUI <player>");
			return true;
		}
		if (!sender.hasPermission(ConfigProperties.CREATE_UI_PERMISSION)) {
			sender.sendMessage(ConfigProperties.PREFIX + "You do not have permission for that command.");
			return true;
		}
		try {
			if (pc.getUUID(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player not found!");
				return true;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (SU.isExistant(args[0])) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "User already exists!");
				return false;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String pw = Utils.getSaltString();
			SU.insertUser(pc.getUUID(args[0]), Utils.sha1(pw));
			sender.sendMessage("------------PAY ATTENTION------------");
			sender.sendMessage("This is the generated password. Give this to the desired user.");
			sender.sendMessage("This password is encrypted in the database. You will not see it again.");
			sender.sendMessage("The user will be required to change their password upon login");
			sender.sendMessage("PASSWORD------> " + pw);
			sender.sendMessage("sha1 Hash ---->" + Utils.sha1(pw));
			sender.sendMessage("--------------------------------------");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	
}
