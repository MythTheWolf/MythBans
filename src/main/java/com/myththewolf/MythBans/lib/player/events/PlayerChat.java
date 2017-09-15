 package com.myththewolf.MythBans.lib.player.events;
 
 import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
 import com.myththewolf.MythBans.lib.discord.MythDiscordBot;
 import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
 import com.myththewolf.MythBans.lib.feilds.PlayerDataCache;
 import com.myththewolf.MythBans.lib.player.ChatChannel;
 import com.myththewolf.MythBans.lib.player.MythPlayer;
 import java.io.PrintStream;
 import java.net.InetAddress;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.UUID;
 import java.util.concurrent.ExecutionException;
 import org.bukkit.Bukkit;
 import org.bukkit.ChatColor;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.Listener;
 import org.bukkit.event.player.AsyncPlayerChatEvent;
 
 public class PlayerChat implements Listener
 {
   private DatabaseCommands dbc = new DatabaseCommands();
   private MythDiscordBot MDB;
   
   public PlayerChat(MythDiscordBot mBD2) {
     this.MDB = mBD2;
   }
   
   @EventHandler
   public void onPlayerChatEvent(AsyncPlayerChatEvent e)
     throws SQLException, InterruptedException, ExecutionException
   {
     Player p = e.getPlayer();
     
     MythPlayer playerClass = PlayerDataCache.getInstance(p.getUniqueId().toString());
     e.setCancelled(true);
     if ((e.getMessage().equalsIgnoreCase(ConfigProperties.SOFTMUTE_RELEASE_COMMAND)) && (!playerClass.isOverride())) {
       MythPlayer.setOverride(p.getUniqueId().toString(), true);
       playerClass.setStatus("OK");
       this.dbc.cleanUser(p.getUniqueId().toString());
       p.sendMessage(ConfigProperties.PREFIX + ChatColor.GREEN + "You may now speak!");
       e.setCancelled(true);
       PlayerDataCache.rebuildUser(p.getUniqueId().toString());
       return;
     }
     if (playerClass.getStatus().equals("muted")) {
       p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigProperties.PREFIX) + "Your voice has been silenced!");
       
       e.setCancelled(true);
     } else if (this.dbc.getIPStatus(e.getPlayer().getAddress().getAddress().toString()).equals("muted")) {
       p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigProperties.PREFIX) + "Your voice has been silenced!");
       
       e.setCancelled(true);
       return;
     }else if(playerClass.getStatus().equals("softmuted")){
	             p.sendMessage("<"+p.getName()+" : "+e.getMessage()+">");
	            for(Player r : Bukkit.getOnlinePlayers()){
		         if(r.hasPermission(ConfigProperties.STAFF_CHAT_GET)){
			       r.sendMessage(ChatColor.GRAY+"[SOFTMUTED: "+p.getName()+"]"+e.getMessage());
			     }
		        }
		       e.setCancelled(true);
		       return;
	           }
	          if(e.getMessage().charAt(0) == '#' && e.getPlayer().hasPermission(ConfigProperties.STAFF_CHAT_SEND)){
		      for(Player r : Bukkit.getOnlinePlayers()){
		         if(r.hasPermission(ConfigProperties.STAFF_CHAT_GET)){
			       r.sendMessage(ChatColor.GRAY+"["+ChatColor.DARK_RED+"#!STAFF"+ChatColor.GRAY+"]" + p.getDisplayName() + ": " + e.getMessage().substring(1));
			     }
		        }
		     e.setCancelled(true);
		     return;
		     }
     for (Player u : Bukkit.getOnlinePlayers()) {
       if (e.getMessage().startsWith(u.getName() + ":")) {
         MythPlayer run = new MythPlayer(u.getUniqueId().toString());
         ChatChannel Crun = new ChatChannel(run.getChannel());
         String dump = e.getMessage().substring(u.getName().length() + 1);
         Crun.push(e.getPlayer(), dump);
         e.getPlayer().sendMessage(ConfigProperties.PREFIX + "Sent message to channel: " + run.getChannel() + ".");
         return;
       }
     }
     ChatChannel tmp = new ChatChannel();
     HashMap<String, String> map = tmp.getMap();
     if (map.containsKey(e.getMessage().substring(0, 1))) {
       ChatChannel sen = new ChatChannel((String)map.get(e.getMessage().substring(0, 1)));
       if (!sen.canUse(e.getPlayer())) { e.getPlayer().sendMessage("<<No permission echo there.>>");return; }
       sen.push(e.getPlayer(), e.getMessage().substring(1));
       return;
     }
     ChatChannel engine = new ChatChannel(playerClass.getChannel());
     if (!engine.exists()) {
       e.getPlayer().sendMessage("<<could not send message to non existant channel>>");
       return;
     }
     if (!engine.canUse(e.getPlayer())) { e.getPlayer().sendMessage("<<No permission to send here.>>");System.out.println(engine.getNode());return; }
     engine.push(e.getPlayer(), e.getMessage());
   }
 }


