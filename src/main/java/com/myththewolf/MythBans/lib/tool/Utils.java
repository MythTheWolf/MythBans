package com.myththewolf.MythBans.lib.tool;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.myththewolf.MythBans.lib.DiscordConnection;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.impl.ImplMessage;

public class Utils {
	public static String makeString(String[] args, int index) {
		String myString = ""; // we're going to store the arguments here

		for (int i = index; i < args.length; i++) { // loop threw all the
													// arguments
			String arg = args[i] + " "; // get the argument, and add a space so
										// that the words get spaced out
			myString = myString + arg; // add the argument to myString
		}
		if (myString == null || myString.equals(" ") || myString.equals("")) {
			myString = ConfigProperties.DEFAULT_BAN_REASON;
		}
		return myString;
	}

	/**
	 * Converts a location to a simple string representation If location is
	 * null, returns empty string
	 * 
	 * @param l
	 * @return
	 */
	static public String serializeLocation(final Location l) {
		if (l == null) {
			return "";
		}
		return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
	}

	/**
	 * Converts a serialized location to a Location. Returns null if string is
	 * empty
	 * 
	 * @param s
	 *            - serialized location in format "world:x:y:z"
	 * @return Location
	 */
	static public Location parseLocation(final String s) {
		if (s == null || s.trim() == "") {
			return null;
		}
		final String[] parts = s.split(":");
		if (parts.length == 4) {
			final World w = Bukkit.getServer().getWorld(parts[0]);
			final int x = Integer.parseInt(parts[1]);
			final int y = Integer.parseInt(parts[2]);
			final int z = Integer.parseInt(parts[3]);
			return new Location(w, x, y, z);
		}
		return null;
	}
	public static String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	// This replicates the PHP sha1 so that we can authenticate the same users.
	public static String sha1(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return byteArray2Hex(MessageDigest.getInstance("SHA1").digest(s.getBytes("UTF-8")));
	}

	private static final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private static String byteArray2Hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (final byte b : bytes) {
			sb.append(hex[(b & 0xF0) >> 4]);
			sb.append(hex[b & 0x0F]);
		}
		return sb.toString();
	}
    public static Message getMessageById(String channelId, String messageId) {
        Message message = null;
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get("https://discordapp.com/api/channels/" + channelId + "/messages/" + messageId)
                    .header("authorization", DiscordConnection.getConnection().getToken()).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        try {
            ((ImplDiscordAPI) Javacord.getApi()).checkResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject messageResponse = response.getBody().getObject();
        message = new ImplMessage(messageResponse, (ImplDiscordAPI) Javacord.getApi(), null);
        return message;
    }
}
