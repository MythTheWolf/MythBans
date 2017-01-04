package com.myththewolf.MythBans.lib.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Date {
	private SimpleDateFormat f =  new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	public java.util.Date getNewDate()
	{
		try
		{
			return f.parse(f.format(new java.util.Date()));
		} catch (ParseException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public java.util.Date parseDate(String in)
	{
		try
		{
			return f.parse(in);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public String formatDate(java.util.Date  in)
	{
		return f.format(in).toString();
	}
}
