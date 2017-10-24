 package com.myththewolf.MythBans.lib.player.events;
 
 import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.feilds.AbstractMaps;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.MythPlayerMetaData;
import com.myththewolf.MythBans.lib.player.PlayerCache;

import net.md_5.bungee.api.ChatColor;
 
 public class CommandEvent implements org.bukkit.event.Listener
 {
   private PlayerCache pCache = new PlayerCache(com.myththewolf.MythBans.lib.SQL.MythSQLConnect.getConnection());
   private String[] arr;
   
   public CommandEvent(JavaPlugin pl) {}
   
   @EventHandler(priority=EventPriority.HIGH)
   public void onCommand(PlayerCommandPreprocessEvent e) throws Exception
   {
     this.arr = e.getMessage().split(" ");
     MythPlayer MP = new MythPlayer(e.getPlayer().getUniqueId().toString());
     if (!MP.isOverride()) {
       if (e.getMessage().indexOf("rules") > -1) {
         System.out.println("UPDATING_RULES");
         AbstractMaps.read_rules.put(e.getPlayer().getUniqueId().toString(), Boolean.valueOf(true));
       } else if (!AbstractMaps.read_rules.containsKey(e.getPlayer().getUniqueId().toString())) {
         System.out.println("SETTING_FALSE");
         AbstractMaps.read_rules.put(e.getPlayer().getUniqueId().toString(), Boolean.valueOf(false));
       }
     }
     if (ConfigProperties.DEBUG.booleanValue()) {
       System.out.println("[MythBans]Captured command event!");
     }
     
     if (e.getPlayer().hasMetadata("is_potato")) {
       if (ConfigProperties.DEBUG.booleanValue()) {
         System.out.println("[MythBans]User is potato, canceling..");
       }
       e.getPlayer().sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Potatoes can't execute commands!");
       e.setCancelled(true);
       return;
     }
     
     if (e.getMessage().indexOf("cs_trigger") <= 0) {
       for (Player p : org.bukkit.Bukkit.getOnlinePlayers()) {
         MythPlayerMetaData MPM = new MythPlayerMetaData(p.getUniqueId().toString());
         if (MPM.isSpying()) {
           p.sendMessage(e.getPlayer().getDisplayName() + ":" + e.getMessage());
         }
       }
     }
     
     if (this.arr[0].equals("/ignore")) {
       OfflinePlayer op = this.pCache.getOfflinePlayerExact(this.arr[1]);
       if ((op != null) && (MP.isIgnoring(op.getUniqueId().toString()))) {
         MP.removeIgnore(op.getUniqueId().toString());
       } else if ((op != null) && (!MP.isIgnoring(op.getUniqueId().toString()))) {
         MP.addIgnore(op.getUniqueId().toString());
       }
       else {
         e.getPlayer().sendMessage(ConfigProperties.PREFIX + "Couldnt hook ignore, user not found in database");
       }
     }
   }
 }

