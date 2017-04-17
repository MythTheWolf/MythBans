package com.myththewolf.MythBans.tasks;

import com.myththewolf.MythBans.lib.discord.MythDiscordBot;

public class UpdateConsoleChannel implements Runnable {
	private MythDiscordBot myBot;

	public UpdateConsoleChannel(MythDiscordBot i) {
		myBot = i;
	}

	@Override
	public void run() {
		if (LogWatcher.getThread().length() >= 1990) {
			if (LogWatcher.getThread().length() >= 2000) {
				myBot.remakeConsoleThread("---reached max text size---");
			} else {
				myBot.remakeConsoleThread(LogWatcher.getThread());
			}
		}else{
			
		}
	}

}
