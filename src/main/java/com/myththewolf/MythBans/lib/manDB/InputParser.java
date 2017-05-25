package com.myththewolf.MythBans.lib.manDB;
import java.text.ParseException;

public class InputParser {
	private String raw;

	public InputParser(String parse) {
		raw = parse;
	}

	public String get(String key) throws ParseException {

		if (raw.indexOf(key) < 0) {
			throw new ParseException("Key not found: "+ key, 0);
		}
		String cut = raw.substring((raw.indexOf(key) + key.length()), raw.indexOf(key) + key.length() + 1);
		int after_key = raw.indexOf(key) + key.length() + 2;
		if (!cut.equals("{")) {
			throw new ParseException("Expected '{" + "', but got '" + cut + "'.", 0);
		}
		String fin = "NOP";
		for (int i = after_key; i < raw.length(); i++) {
			
			if (raw.charAt(i) == '}' && raw.charAt(i-1) != '\\'){
				
				fin = raw.substring(after_key-1, i);
				break;
			}else if(raw.charAt(i) == '}' && raw.charAt(i-1) == '\\'){
				StringBuilder SB = new StringBuilder(raw);
				SB.deleteCharAt(SB.indexOf("\\"));
				raw = SB.toString();
			}
			
		}
		if(fin.equals("NOP")){
			throw new ParseException("Expected '}', but got to end of file.", 0);
		}
		return fin;
	}
	
}
