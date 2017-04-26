package com.myththewolf.MythBans.lib.feilds;

import java.util.HashMap;

public class AbstractMaps {
	public static HashMap<String, Integer> new_join_count;
	public static HashMap<String, Boolean> read_rules;
	public static String CONSOLE_TAIL_THREAD;
	public static void buildMaps(){
		new_join_count = new HashMap<>();
		read_rules = new HashMap<>();
	}
}
