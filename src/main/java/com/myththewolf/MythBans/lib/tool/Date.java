package com.myththewolf.MythBans.lib.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;

public class Date {
	private SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

	public java.util.Date getNewDate() {
		try {
			return f.parse(f.format(new java.util.Date()));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	public String getNewDateString(){
		return f.format(new java.util.Date());
	}
	public java.util.Date parseDate(String in) {
		try {
			return f.parse(in);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String formatDate(java.util.Date in) {
		return f.format(in);
	}

	public long getTimeDifference(java.util.Date in, java.util.Date now) {
		DateTime end = new DateTime(in);
		DateTime start = new DateTime(now);
		return start.getMillis() - end.getMillis();
	}

	public String convertToPd(long milliseconds) {
		int days = (int) ((milliseconds / (1000 * 60 * 60 * 24)) % 7);
		int weeks = (int) (milliseconds / (1000 * 60 * 60 * 24 * 7));
		int seconds = (int) (milliseconds / 1000) % 60;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
		return weeks + " Weeks," + days + " days, " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds ";
	}
	public java.util.Date addMili(long m, java.util.Date theDate){
		return this.parseDate(this.formatDate(new java.util.Date(theDate.getTime() + m)));
	}
}
