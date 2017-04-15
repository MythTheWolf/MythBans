package com.myththewolf.MythBans.lib.discord;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.myththewolf.MythBans.lib.player.AbstractPlayer;

import de.btobastian.javacord.entities.User;

public class CommandDispatcher {

	public CommandDispatcher(String cmd, User sender) {

		List<String> split = Arrays.asList(cmd.split(" "));
		if (!MythDiscordBot.getCommandMap().containsKey(split.get(0))) {
			sender.sendMessage("Command not found!");
			return;
		} else {
			try {
				MythCommandExecute MCE = MythDiscordBot.getCommandMap().get(split.get(0));
				AbstractPlayer AP = new AbstractPlayer(sender.getId());
				if (AP.isLinked()) {
					split.remove(0);
					MCE.runCommand(sender, AP.getPlayer(), split.toArray(new String[split.size()]));
				} else {
					split.remove(0);
					MCE.runCommand(sender, null, split.toArray(new String[split.size()]));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
