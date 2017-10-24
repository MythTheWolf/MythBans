package com.myththewolf.MythBans.lib.player;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.myththewolf.MythBans.lib.feilds.DataCache;

public class ChatChannel{
    
    private String CHANNEL_NAME = "undefined";
    private String CHANNEL_PREFIX = "[UNDEFINED NAME]";
    private String CHANNEL_SHORTCUT = "undefinedmythbansshourtcut&#&$";

    public ChatChannel(String name) {

    }

    public void push(MythPlayer sender, String message) {
        // This is to send to all people writing
        Bukkit.getServer().getOnlinePlayers().stream().filter(rawPlayer -> {
            MythPlayer MP = DataCache.getPlayerInstance(rawPlayer.getUniqueId().toString());
            return (MP.getChannels().contains(this) && !MP.isIgnoring(MP.getId())
                    && MP.getWritingChannel().equals(this));
        }).forEach(player -> {
            player.sendMessage(ChatColor.GREEN + this.CHANNEL_PREFIX
                    + sender.getBukkitPlayer()
                            .orElseThrow(
                                    () -> new IllegalStateException("Tried to send message but player doesn't exist!"))
                            .getDisplayName()
                    + ": " + message);
        });

        // All write channels are done,now lets do read onlys.
        Bukkit.getServer().getOnlinePlayers().stream().filter(rawPlayer -> {
            MythPlayer MP = DataCache.getPlayerInstance(rawPlayer.getUniqueId().toString());
            return (MP.getChannels().contains(this) && !MP.isIgnoring(MP.getId())
                    && !MP.getWritingChannel().equals(this));
        }).forEach(player -> {
            player.sendMessage(ChatColor.GREEN + this.CHANNEL_PREFIX
                    + sender.getBukkitPlayer()
                            .orElseThrow(
                                    () -> new IllegalStateException("Tried to send message but player doesn't exist!"))
                            .getDisplayName()
                    + ": " + message);
        });

    }

    public List<Player> getAllPlayers() {
        return Bukkit.getServer().getOnlinePlayers().stream().filter(entry -> {
            return DataCache.getPlayerInstance(entry.getUniqueId().toString()).getChannels().contains(this);
        }).collect(Collectors.toList());
    }

    public String getName() {
        return this.CHANNEL_NAME;
    }

    public String getPrefix() {
        return this.CHANNEL_PREFIX;
    }

    public boolean equals(ChatChannel obj) {
        return getName().equals(obj.getName());
    }
    
    public String getShortcut() {
        return this.CHANNEL_SHORTCUT;
    }
}
