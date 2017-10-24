package com.myththewolf.MythBans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;

public class open implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(!(args.length > 0)) {
            sender.sendMessage(ConfigProperties.PREFIX + DataCache.getAvailibleChannels().toString());
            return true;
        }
        return true;
    }

}
