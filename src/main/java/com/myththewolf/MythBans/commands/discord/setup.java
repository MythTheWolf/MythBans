package com.myththewolf.MythBans.commands.discord;

import java.awt.Color;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.discord.DiscordCommand;
import com.myththewolf.MythBans.lib.discord.MythCommandExecute;
import com.myththewolf.MythBans.lib.discord.MythDiscordBot;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissionsBuilder;

public class setup implements MythCommandExecute {
	private MythDiscordBot myBot;


	public setup(MythDiscordBot j) {
	
		myBot = j;
		// TODO Auto-generated constructor stub
	}

	@Override
	@DiscordCommand(requiresLinked=false,requiresRoot=true)
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args,Message theMessage) {
		try {
			if(myBot.isSetup()){
				theMessage.reply("This server is already setup. If you want to re-install, wipe the database and delete the proper channels & roles");
				return;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String SRV_ID = theMessage.getChannelReceiver().getServer().getId();
		Role theRole = null;
		Role ROOT = null;
		try {
			theRole = theMessage.getChannelReceiver().getServer().createRole().get();
			ROOT = theMessage.getChannelReceiver().getServer().createRole().get();

			ImplPermissionsBuilder IB = new ImplPermissionsBuilder();
			IB.setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.ALLOWED);
			IB.setState(PermissionType.READ_MESSAGES, PermissionState.ALLOWED);
			IB.setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED);
			theRole.update("Linked account", Color.YELLOW, false, IB.build());
			ROOT.update("Root user", Color.RED, false, IB.build());
			myBot.writeData(SRV_ID, theRole.getId(), ROOT.getId());
			theMessage.delete();
			theMessage.getAuthor().sendMessage(
					"Alright sparky, here's the deal: I have written down everythig I needed, and all you need to do is restart your server.\n I will remain disconnected until then.");

			myBot.disconnect();
			return;
		} catch (InterruptedException | ExecutionException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
