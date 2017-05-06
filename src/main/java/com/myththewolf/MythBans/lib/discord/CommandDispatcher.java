package com.myththewolf.MythBans.lib.discord;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.player.AbstractPlayer;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class CommandDispatcher {

	public CommandDispatcher(String cmd, User sender, Message theMessage) {
		
		List<String> split = Arrays.asList(cmd.split(" "));
		if (!MythDiscordBot.getCommandMap().containsKey(split.get(0))) {
			theMessage.reply("Command not found!");
			return;
		} else {
			try {
				
				MythCommandExecute MCE = MythDiscordBot.getCommandMap().get(split.get(0));
				AbstractPlayer AP = new AbstractPlayer(sender.getId());
				@SuppressWarnings("rawtypes")
				Class[] args = new Class[4];
				args[0] = User.class;
				args[1] = OfflinePlayer.class;
				args[2] = String[].class;
				args[3] = Message.class;
				Method M = MCE.getClass().getMethod("runCommand",args);
				DiscordCommand  CO;
				if(!M.isAnnotationPresent(DiscordCommand.class)){
					theMessage.delete();
					return;
					
				}else{
					CO = M.getAnnotation(DiscordCommand.class);
				}
				if(CO.deleteTriggerMessage()){
					theMessage.delete();
				}
				if (CO.requiresLinked() && !AP.isLinked()) {
					theMessage.reply("You must be a linked account before executing this command.");
					return;
				}
				if (CO.requiresRoot() && !AP.isRootAccount()) {
					theMessage.reply("You must be a root account before executing this command.");
					return;
				}
				if (AP.isLinked()) {
					if (split.size() > 1) {
						split.remove(0);
					}
					MCE.runCommand(sender, AP.getPlayer(), split.toArray(new String[split.size()]), theMessage);
				} else {
					MCE.runCommand(sender, null, split.toArray(new String[split.size()]), theMessage);
					System.out.println("RUNNING____NOTBUKKIT");
				}
			} catch (SQLException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

}
