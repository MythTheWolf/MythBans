package com.myththewolf.MythBans.commands;

import java.io.FileReader;
import java.util.Iterator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class importJSON implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
JSONParser parser = new JSONParser();
        
        try {
      
         Object obj = parser.parse(new FileReader("banned-players.json"));
      
         JSONArray ROOT = (JSONArray) obj;
        @SuppressWarnings("unchecked")
		Iterator<JSONObject> i = ROOT.iterator();
        long j = 0;
        while(i.hasNext())
        {
        	JSONObject object = i.next();
        	String UUID = object.get("uuid").toString();
        	String created = object.get("crated").toString();
        	String source = "CONSOLE";
        	String expires = object.get("expires").toString();
        	String reason = object.get("reason").toString();
        	j++;
        }
        arg0.sendMessage(Long.toString(j));
       }catch (Exception e){
           e.printStackTrace();
           return true;
       }
        return true;
	}

}
