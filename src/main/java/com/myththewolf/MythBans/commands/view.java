package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;
import com.myththewolf.MythBans.lib.player.ChatChannel;
import com.myththewolf.MythBans.lib.player.MythPlayer;

import net.md_5.bungee.api.ChatColor;

public class view implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        MythPlayer MP = DataCache.getPlayerInstance(((Player) sender).getUniqueId().toString());

        if (!(args.length > 0)) {
            sender.sendMessage(ConfigProperties.PREFIX
                    + "Channels you are viewing: [Red = View only, Green = Channel your messages are being sent to]");
            String build = "";
            for (ChatChannel C : MP.getChannels()) {
                if (MP.getWritingChannel().equals(C)) {
                    build += ChatColor.GREEN + C.getName() + " ";
                } else {
                    build += ChatColor.RED + C.getName() + " ";
                }
            }
            sender.sendMessage(build);
            sender.sendMessage(
                    "You can toggle channel viewing by doing /view <channel>, and you can set your writing channel by doing /open");
            return true;
        }
        if (args[0].equals("-w")) {
            if (!DataCache.getAvailibleChannels().stream().anyMatch(chan -> chan.getName().equals(args[1]))) {
                sender.sendMessage(ConfigProperties.PREFIX + "No such channel!");
            } else {
                MP.updateChannel(args[1]);
                sender.sendMessage(ConfigProperties.PREFIX + "Your messages will now be sent to: " + args[0]);
            }
        } else {
            if (!DataCache.getAvailibleChannels().stream().anyMatch(chan -> chan.getName().equals(args[0]))) {
                sender.sendMessage(ConfigProperties.PREFIX + "No such channel!");
            } else {
                ChatChannel question = new ChatChannel(args[0]);
                if (MP.getChannels().contains(question)) {
                    try {

                        MP.removeChannel(question.getName());
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    try {

                        MP.addChannel(question.getName());
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        sender.sendMessage(ConfigProperties.PREFIX
                + "Your channels: [Red = View only, Green = Channel your messages are being sent to]");
        String build = "";
        MP = DataCache.getPlayerInstance(((Player) sender).getUniqueId().toString());
        for (ChatChannel C : MP.getChannels()) {
            if (MP.getWritingChannel().equals(C)) {
                build += ChatColor.GREEN + C.getName() + " ";
            } else {
                build += ChatColor.RED + C.getName() + " ";
            }
        }
        sender.sendMessage(build);
        return true;
    }

}
