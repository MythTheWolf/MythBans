package com.myththewolf.MythBans.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DisableDueToError implements Runnable{
	private JavaPlugin instance;
	public DisableDueToError(JavaPlugin PL) {
		instance = PL;
	}

	@Override
	public void run() {
		System.out.println("****** Disabling Plugin due to test failure ******");
		Bukkit.getPluginManager().disablePlugin(instance);
	}

}
