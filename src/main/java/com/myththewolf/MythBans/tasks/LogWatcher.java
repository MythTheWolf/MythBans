package com.myththewolf.MythBans.tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.myththewolf.MythBans.lib.discord.MythDiscordBot;

public class LogWatcher implements Runnable {
	private static String thread = "";

	private static BufferedReader buffered;

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

		FileReader reader = new FileReader(file);

		buffered = new BufferedReader(reader);

		looper: while (true) {

			String line = buffered.readLine();
			if (line == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue looper;
			}
			String tot = line;
			for (int i = 0; i < 500; i++) {
				String add = buffered.readLine();
				if (add == null) {
					break;
				} else {
					tot += "\n" + add;
				}
			}

			MythDiscordBot.getBot().appendConsole("\n" + tot);

		}

	}

	public static void clearLog() {
		thread = "";
	}

	public static String getThread() {
		return thread;

	}

	public static void closeReader() {
		try {
			buffered.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
