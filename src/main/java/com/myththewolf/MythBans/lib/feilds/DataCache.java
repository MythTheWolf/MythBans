package com.myththewolf.MythBans.lib.feilds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.myththewolf.MythBans.lib.player.ChatChannel;
import com.myththewolf.MythBans.lib.player.MythPlayer;

public class DataCache {

    private static HashMap<String, MythPlayer> PlayerMap;
    private static List<ChatChannel> currentChannels;

    public static MythPlayer getPlayerInstance(String UUID) {
        if (!PlayerMap.containsKey(UUID)) {
            PlayerMap.put(UUID, new MythPlayer(UUID));
        }
        return PlayerMap.get(UUID);
    }

    public static List<ChatChannel> getAvailibleChannels() {
        return currentChannels;
    }

    public static void rebuildCaches() {

        Iterator<Entry<String, MythPlayer>> it = PlayerMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, MythPlayer> pair = it.next();
            PlayerMap.put(pair.getKey(), new MythPlayer(pair.getKey()));
            it.remove(); // avoids a ConcurrentModificationException
        }
        
    }

    public static void rebuildUser(String UUID) {
        PlayerMap.put(UUID, new MythPlayer(UUID));
    }

    public static void makeMap() {
        PlayerMap = new HashMap<String, MythPlayer>();
        currentChannels = new ArrayList<>();
        try {
            Connection con = com.myththewolf.MythBans.lib.SQL.MythSQLConnect.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM MythBans_Channels");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               currentChannels.add(new ChatChannel(rs.getString("name")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
