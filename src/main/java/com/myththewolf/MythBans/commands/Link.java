package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerCache;

import net.md_5.bungee.api.ChatColor;

public class Link implements CommandExecutor {
	private PlayerCache PC = new PlayerCache(MythSQLConnect.getConnection());
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender instanceof ConsoleCommandSender)
		{
			sender.sendMessage("Nice try console");
			return true;
		}
		if(args.length < 1)
		{
			sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /link <secret key>");
		}
		try {
			if(!PC.secretExists(args[0]))
			{
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Invalid secret key.");
				return true;
			}else{
				String UUID = ((Player) sender).getUniqueId().toString();
				PC.linkDiscord(args[0], UUID);
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GREEN + "Your discord has been linked!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
