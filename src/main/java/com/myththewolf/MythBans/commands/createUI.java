package com.myththewolf.MythBans.commands;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.Group;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.player.SiteUser;

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
			String pw = getSaltString();
			SU.insertUser(pc.getUUID(args[0]), sha1(pw));
			sender.sendMessage("------------PAY ATTENTION------------");
			sender.sendMessage("This is the generated password. Give this to the desired user.");
			sender.sendMessage("This password is encrypted in the database. You will not again.");
			sender.sendMessage("The user will be required to change their password upon login");
			sender.sendMessage("PASSWORD------> " + pw);
			sender.sendMessage("sha1 Hash ---->" + sha1(pw));
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

	private String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	// This replicates the PHP sha1 so that we can authenticate the same users.
	public static String sha1(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return byteArray2Hex(MessageDigest.getInstance("SHA1").digest(s.getBytes("UTF-8")));
	}

	private static final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private static String byteArray2Hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (final byte b : bytes) {
			sb.append(hex[(b & 0xF0) >> 4]);
			sb.append(hex[b & 0x0F]);
		}
		return sb.toString();
	}

}
