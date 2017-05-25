package com.myththewolf.MythBans.commands;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.manDB.Manual;
import com.myththewolf.MythBans.lib.tool.Utils;

import net.md_5.bungee.api.ChatColor;

public class Man implements CommandExecutor {
	private JavaPlugin i;

	public Man(JavaPlugin pl) {
		i = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (args.length < 1) {
			sender.sendMessage("Use /man -l <page number> to list man pages");
			return true;
		}
		int look = 1;
		if (Arrays.asList(args).contains("-l")) {
			if (args.length >= 2) {
				if (!Utils.isNumber(args[1])) {
					sender.sendMessage("Invalid page number");
					return true;
				} else {
					look = Integer.parseInt(args[1]);
				}
			}
			HashMap<Integer, String> map = new HashMap<>();
			File dir = new File(i.getDataFolder() + File.separator + "man-db");
			File[] directoryListing = dir.listFiles();
			int count = 1;
			if (directoryListing != null) {
				for (File child : directoryListing) {
					if (child.getName().substring(child.getName().length() - 3).equalsIgnoreCase("man")) {
						map.put(count, child.getName().substring(0,child.getName().lastIndexOf('.')));
						count++;
					}
				}
			} else {
				System.out.println("!!!!!!!!!!!!!!!!!!");
			}
			if (look > count) {
				sender.sendMessage("Page out of bounds!");
				return true;
			} else {
				Utils.paginate(sender, map, look, 10, "Manual entries");
			}
		} else {
			try {

				Manual M = new Manual(args[0], sender, i);

				int req = 0;
				if (args.length > 1) {
					if (Utils.isNumber(args[1])) {
						req = Integer.parseInt(args[1]);
					}
				} else {
					sender.sendMessage("---Index of manual: " + args[0] + "---");
					String[] index = M.getIndex();
					for (int i = 0; i < index.length - 1; i++) {
						sender.sendMessage("Page " + i + ":" + " "+index[i]);
					}
					return true;
				}
				if (M.getMap().size() - 1 < req) {
					sender.sendMessage("Page out of bounds!");
					return true;
				}
				if (sender instanceof ConsoleCommandSender) {
					sender.sendMessage(
							ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', M.getPage(req))));
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', M.getPage(req)));
				}
				M.getProperties();
			} catch (IOException e) {

				sender.sendMessage("Man page not found!");
				e.printStackTrace();
				return true;
			} catch (ParseException e) {
				sender.sendMessage("Corrupted Man page! Talk to a staff member about this!");
				e.printStackTrace();
				return true;
			}
		}
		return true;
	}
}
