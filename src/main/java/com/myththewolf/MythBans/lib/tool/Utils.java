package com.myththewolf.MythBans.lib.tool;

public class Utils {
	public static String makeString(String[] args, int index)
	{
		String myString = ""; //we're going to store the arguments here    

		for(int i = index; i < args.length; i++){ //loop threw all the arguments
		    String arg = args[i] + " "; //get the argument, and add a space so that the words get spaced out
		    myString = myString + arg; //add the argument to myString
		}
		return myString;
	}

}
