package com.myththewolf.MythBans.lib.tool;

import java.text.SimpleDateFormat;

public class Date {
	private SimpleDateFormat f =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public String getNewDate()
	{
		return f.format(new java.util.Date());
	}
}
