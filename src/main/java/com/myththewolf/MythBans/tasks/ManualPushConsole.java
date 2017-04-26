package com.myththewolf.MythBans.tasks;

import com.myththewolf.MythBans.lib.discord.MythDiscordBot;

public class ManualPushConsole implements Runnable {
	private LogFileTailer LT;
	public ManualPushConsole(LogFileTailer LFT) {
		LT = LFT;
	}

	@Override
	public void run() {
		MythDiscordBot.getBot().appendConsole(""+LT.buildString());
	}

}
