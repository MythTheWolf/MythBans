package com.myththewolf.MythBans.tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.myththewolf.MythBans.lib.discord.MythDiscordBot;

import net.md_5.bungee.api.ChatColor;

public class LogWatcher implements Runnable{
	private static String thread = "";


	@Override
	public void run() {
		try {
			monitorFile("logs/latest.log");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void monitorFile(String file) throws IOException {
		final int POLL_INTERVAL = 1000;
		FileReader reader = new FileReader(file);
		BufferedReader buffered = new BufferedReader(reader);
		try {
			while (true) {
				String line = buffered.readLine();
				if (line == null) {
					// end of file, start polling
					Thread.sleep(POLL_INTERVAL);
				} else {
					MythDiscordBot.getBot().appendConsole(ChatColor.stripColor("\n"+line));
				}
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		buffered.close();
	}
	public static void clearLog(){
		thread = "";
	}
	public static String getThread(){
		return thread;
		
	}
}
