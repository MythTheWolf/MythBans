package com.myththewolf.MythBans.commands.discord;

import java.sql.SQLException;

import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.discord.DiscordCommand;
import com.myththewolf.MythBans.lib.discord.MythCommandExecute;
import com.myththewolf.MythBans.lib.player.PlayerCache;
import com.myththewolf.MythBans.lib.tool.Utils;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class McLink implements MythCommandExecute {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());

	public McLink() {
		// TODO Auto-generated constructor stub
	}

	@DiscordCommand(requiresLinked = false, requiresRoot = false)
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage) {
		try {
			
			if (pCache.isLinked(theMessage.getAuthor().getId())) {
				theMessage.reply("You are already linked");
				return;
			}
			String SALT = Utils.getSaltString();
			while (pCache.secretExists(SALT)) {
				SALT = Utils.getSaltString();
			}
			theMessage.getAuthor().sendMessage("Type this in game: /link " + SALT);
			pCache.bindSecret(SALT, theMessage.getAuthor().getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
