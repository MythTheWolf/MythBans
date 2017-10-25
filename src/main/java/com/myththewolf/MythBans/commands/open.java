package com.myththewolf.MythBans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;

public class open implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(args.length > 0)) {
            sender.sendMessage(
                    ConfigProperties.PREFIX + "Channels availible:" + DataCache.getAvailibleChannels().toString());
            return true;
        }
        if (!DataCache.getAvailibleChannels().stream().anyMatch(chan -> chan.getName().equals(args[0]))) {
            sender.sendMessage(ConfigProperties.PREFIX + "No such channel!");
            return true;
        }
        DataCache.getPlayerInstance(((Player) sender).getUniqueId().toString()).updateChannel(args[0]);
        sender.sendMessage(ConfigProperties.PREFIX + "Your messages will now be sent to: " + args[0]);
        return true;
    }

}
