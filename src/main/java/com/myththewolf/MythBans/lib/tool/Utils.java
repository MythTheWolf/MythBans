package com.myththewolf.MythBans.lib.tool;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class Utils {
	public static String makeString(String[] args, int index) {
		String myString = ""; // we're going to store the arguments here

		for (int i = index; i < args.length; i++) { // loop threw all the
													// arguments
			String arg = args[i] + " "; // get the argument, and add a space so
										// that the words get spaced out
			myString = myString + arg; // add the argument to myString
		}
		if(myString == null || myString.equals(" ") || myString.equals(""))
		{
			myString = ConfigProperties.DEFAULT_BAN_REASON;
		}
		return myString;
	}
}
