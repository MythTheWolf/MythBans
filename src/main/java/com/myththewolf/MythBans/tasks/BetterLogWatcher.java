package com.myththewolf.MythBans.tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.myththewolf.MythBans.lib.discord.MythDiscordBot;

public class BetterLogWatcher implements Runnable {

	public BetterLogWatcher() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		int sleepTime;
		String currentLine = null;
		sleepTime = 1 * 1000;

		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader("logs/latest.log"));
			while (true) {

				if ((currentLine = input.readLine()) != null) {
					MythDiscordBot.getBot().appendConsole("\n" + currentLine);
					continue;
				}

				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}

			}
			input.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void clearLog() {
		try {
			MythDiscordBot.getBot().getConsoleThread().edit("--Log cleared--");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
