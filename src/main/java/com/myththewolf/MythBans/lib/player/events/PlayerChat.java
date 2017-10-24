package com.myththewolf.MythBans.lib.player.events;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;

public class PlayerChat implements Listener {
    private DatabaseCommands dbc = new DatabaseCommands();

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent e)
            throws SQLException, InterruptedException, ExecutionException {
        Player p = e.getPlayer();

        MythPlayer playerClass = DataCache.getPlayerInstance(p.getUniqueId().toString());
        e.setCancelled(true);
        if ((e.getMessage().equalsIgnoreCase(ConfigProperties.SOFTMUTE_RELEASE_COMMAND))
                && (!playerClass.isOverride())) {
            MythPlayer.setOverride(p.getUniqueId().toString(), true);
            playerClass.setStatus("OK");
            this.dbc.cleanUser(p.getUniqueId().toString());
            p.sendMessage(ConfigProperties.PREFIX + ChatColor.GREEN + "You may now speak!");
            e.setCancelled(true);
            DataCache.rebuildUser(p.getUniqueId().toString());
            return;
        }
        if (playerClass.getStatus().equals("muted")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigProperties.PREFIX)
                    + "Your voice has been silenced!");

            e.setCancelled(true);
        } else if (this.dbc.getIPStatus(e.getPlayer().getAddress().getAddress().toString()).equals("muted")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigProperties.PREFIX)
                    + "Your voice has been silenced!");

            e.setCancelled(true);
            return;
        } else if (playerClass.getStatus().equals("softmuted")) {
            p.sendMessage("<" + p.getName() + " : " + e.getMessage() + ">");
            for (Player r : Bukkit.getOnlinePlayers()) {
                if (r.hasPermission(ConfigProperties.STAFF_CHAT_GET)) {
                    r.sendMessage(ChatColor.GRAY + "[SOFTMUTED: " + p.getName() + "]" + e.getMessage());
                }
            }
            e.setCancelled(true);
            return;
        }
        if (e.getMessage().charAt(0) == '#' && e.getPlayer().hasPermission(ConfigProperties.STAFF_CHAT_SEND)) {
            for (Player r : Bukkit.getOnlinePlayers()) {
                if (r.hasPermission(ConfigProperties.STAFF_CHAT_GET)) {
                    r.sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_RED + "#!STAFF" + ChatColor.GRAY + "]"
                            + p.getDisplayName() + ": " + e.getMessage().substring(1));
                }
            }
            e.setCancelled(true);
            return;
        }

        /* Checks for shorcuts */
        if (DataCache.getAvailibleChannels().stream().anyMatch(chan -> {
            return chan.getShortcut().length() < e.getMessage().length()
                    && (e.getMessage().substring(0, chan.getShortcut().length()).equals(chan.getShortcut()));
        })) {
            DataCache.getAvailibleChannels().stream().filter(chan -> {
                return chan.getShortcut().length() < e.getMessage().length()
                        && (e.getMessage().substring(0, chan.getShortcut().length()).equals(chan.getShortcut()));
            }).forEach(channel -> channel.push(playerClass, e.getMessage().substring(channel.getShortcut().length())));
        } else {
            /* Its now a reg chat channel */
            MythPlayer MP = DataCache.getPlayerInstance(e.getPlayer().getUniqueId().toString());
            MP.getWritingChannel().push(MP, e.getMessage());
        }
    }
}
