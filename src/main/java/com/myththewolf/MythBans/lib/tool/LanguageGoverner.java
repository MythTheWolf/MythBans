package com.myththewolf.MythBans.lib.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerLanguage;

public class LanguageGoverner {
	private File masterKey;

	public LanguageGoverner(File governer) {
		masterKey = governer;
	}

	public boolean run() throws IOException {
		for (String LANG : ConfigProperties.LANGS) {
			BufferedReader BR = new BufferedReader(new FileReader(masterKey));
			PlayerLanguage PL = new PlayerLanguage(ConfigProperties.langMap.get(LANG));
			String line;
			baseWhile : while ((line = BR.readLine()) != null) {
				if(line.charAt(0) == '#'){
					continue baseWhile;
				}
				String[] parsed = line.split(":");
				if (!PL.languageList.containsKey(parsed[0])) {
					System.out.println("While Parsing Langage: " + LANG + " ERROR: Missing key-->" + parsed[0]);
					BR.close();
					return false;
				}
				if (PL.languageList.get(parsed[0]) == null || PL.languageList.get(parsed[0]).equals(" ")
						|| PL.languageList.get(parsed[0]).equals("")) {

					BR.close();
					System.out.println(
							"While Parsing Langage: " + LANG + " ERROR: Value can't be NULL or empty-->" + parsed[0]);
					return false;
				}

				String[] NEEDED_KEYS = parsed[1].split(";");
				if (NEEDED_KEYS.length > 0 && !NEEDED_KEYS[0].equals("NOP")) {
					for (String NEED : NEEDED_KEYS) {
						if (PL.languageList.get(parsed[0]).indexOf(NEED) < 0) {
							BR.close();
							System.out.println("While Parsing Langage: " + LANG + " Missing Placeholder '" + NEED
									+ "' for key " + parsed[0]);
							return false;
						}
					}
				}
				if (ConfigProperties.DEBUG) {
					System.out.println(LANG + "\\" + parsed[0] + ": OK");
				}
			}
			BR.close();
		}

		return true;
	}

}
