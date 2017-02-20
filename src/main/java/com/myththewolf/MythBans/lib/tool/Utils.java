package com.myththewolf.MythBans.lib.tool;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

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
}
