package com.myththewolf.MythBans.lib.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class Utils {
	public static List<File> tempFiles = new ArrayList<File>();
	public static final String EOL = System.getProperty("line.separator");

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
	 *            The location to convert
	 * @return The location in a serial string
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

	public static File convert2File(String tempName, InputStream IS) {
		File tempFile = null;
		try {
			tempFile = File.createTempFile(tempName, ".mythBansTemp");
			byte[] buffer = new byte[IS.available()];
			IS.read(buffer);
			OutputStream outStream = new FileOutputStream(tempFile);
			outStream.write(buffer);
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(tempFile.getPath());
		tempFiles.add(tempFile);
		return tempFile;
	}

	/**
	 * Pretty print the directory tree and its file names.
	 * 
	 * @param folder
	 *            must be a folder.
	 * @return
	 */

	public static String printDirectoryTree(File folder) {
		if (!folder.isDirectory()) {
			throw new IllegalArgumentException("folder is not a Directory");
		}
		int indent = 0;
		StringBuilder sb = new StringBuilder();
		printDirectoryTree(folder, indent, sb);
		return sb.toString();
	}

	private static void printDirectoryTree(File folder, int indent, StringBuilder sb) {
		if (!folder.isDirectory()) {
			throw new IllegalArgumentException("folder is not a Directory");
		}
		sb.append(getIndentString(indent));
		sb.append("+--");
		sb.append(folder.getName());
		sb.append("/");
		sb.append("\n");
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				printDirectoryTree(file, indent + 1, sb);
			} else {
				printFile(file, indent + 1, sb);
			}
		}

	}

	private static void printFile(File file, int indent, StringBuilder sb) {
		sb.append(getIndentString(indent));
		sb.append("+--");
		sb.append(file.getName());
		sb.append("\n");
	}

	private static String getIndentString(int indent) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append("|  ");
		}
		return sb.toString();
	}

	public static void deleteTempFiles() {
		for (File I : tempFiles) {
			I.delete();
		}
	}

	public static String readFile(String filename) throws IOException {
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			String nextLine = "";
			StringBuilder sb = new StringBuilder();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine); // note: BufferedReader strips the EOL
										// character
										// so we add a new one!
				sb.append(EOL);
			}
			return sb.toString();
		} finally {
			if (br != null)
				br.close();
			if (fr != null)
				fr.close();
		}
	}

	public static String[] splitStringEvery(String s, int interval) {
		int arrayLength = (int) Math.ceil(((s.length() / (double) interval)));
		String[] result = new String[arrayLength];

		int j = 0;
		int lastIndex = result.length - 1;
		for (int i = 0; i < lastIndex; i++) {
			result[i] = s.substring(j, j + interval);
			j += interval;
		} // Add the last bit
		result[lastIndex] = s.substring(j);

		return result;
	}

	public static void paginate(CommandSender sender, HashMap<Integer, String> map, int page, int pageLength,
			String prefix) {
		sender.sendMessage(ChatColor.YELLOW + prefix + "(" + String.valueOf(page) + " of "
				+ (((map.size() % pageLength) == 0) ? map.size() / pageLength : (map.size() / pageLength) + 1));
		int i = 0, k = 0;
		page--;
		for (final Entry<Integer, String> e : map.entrySet()) {
			k++;
			if ((((page * pageLength) + i + 1) == k) && (k != ((page * pageLength) + pageLength + 1))) {
				i++;
				sender.sendMessage(ChatColor.YELLOW + " - " + e.getValue());
			}
		}
	}
	public static boolean isNumber(String inte){
		try{
			Integer.parseInt(inte);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}

}
