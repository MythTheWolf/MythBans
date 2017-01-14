package com.myththewolf.MythBans.commands;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.player.Player;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class importJSON implements CommandExecutor {
	private DatabaseCommands dbc = new DatabaseCommands();
	private PlayerCache pc = new PlayerCache(MythSQLConnect.getConnection());
	private com.myththewolf.MythBans.lib.player.Player pp = new Player();
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
JSONParser parser = new JSONParser();
        
        try {
      
         Object obj = parser.parse(new FileReader("banned-players.json"));
      
         JSONArray ROOT = (JSONArray) obj;
        @SuppressWarnings("unchecked")
		Iterator<JSONObject> i = ROOT.iterator();
        while(i.hasNext())
        {
        	JSONObject object = i.next();
        	String UUID2 = object.get("uuid").toString();
        	object.get("created").toString();
        	String name = object.get("name").toString();
        	String source = "CONSOLE";
        	String expires = object.get("expires").toString();
        	String reason = object.get("reason").toString();
        	ArrayList<String> checked = new ArrayList<String>();
        	if(!Bukkit.getOfflinePlayer(UUID.fromString(UUID2)).hasPlayedBefore()){
        		if(!checked.contains(UUID2)){
        			pp.processNewUser(UUID2, name);
            		
        		}
        	}
        	if(expires.equals("forever")){
        		sender.sendMessage("------------Importing------------");
        		sender.sendMessage("UUID: " + UUID2);
        		sender.sendMessage("NAME: " + name);
        		sender.sendMessage("EXPIRES: " + expires);
        		sender.sendMessage("REASON: " + reason);
        		sender.sendMessage("---------------------------------");
        		dbc.banUser(UUID2, source, reason);
        	}else{
        		sender.sendMessage("Skipping.");
        	}
        }
       }catch (Exception e){
           e.printStackTrace();
           return true;
       }
        return true;
	}

}
