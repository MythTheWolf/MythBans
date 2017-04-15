package com.myththewolf.MythBans.lib.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.PlayerLanguage;

public class LanguageGoverner {
	private File masterKey;
	private JavaPlugin MythPlugin;

	public LanguageGoverner(File governer, JavaPlugin PL) {
		masterKey = governer;
		MythPlugin = PL;
	}

	public boolean run() throws IOException {
		boolean RUN_RESULT = true;
		for (String LANG : ConfigProperties.LANGS) {
			BufferedReader BR = new BufferedReader(new FileReader(masterKey));
			PlayerLanguage PL = new PlayerLanguage(ConfigProperties.langMap.get(LANG));
			String line;

			baseWhile: while ((line = BR.readLine()) != null) {
				if (line.charAt(0) == '#') {
					continue baseWhile;
				}
				String[] parsed = line.split(":");
				if (!PL.languageList.containsKey(parsed[0])) {
					System.out.println("While Parsing Langage: " + LANG + " ERROR: Missing key-->" + parsed[0]);
					RUN_RESULT = false;
					continue baseWhile;
				}
				if (PL.languageList.get(parsed[0]) == null || PL.languageList.get(parsed[0]).equals(" ")
						|| PL.languageList.get(parsed[0]).equals("")) {

					System.out.println(
							"While Parsing Langage: " + LANG + " ERROR: Value can't be NULL or empty-->" + parsed[0]);
					RUN_RESULT = false;
					continue baseWhile;
				}

				String[] NEEDED_KEYS = parsed[1].split(";");
				if (NEEDED_KEYS.length > 0 && !NEEDED_KEYS[0].equals("NOP")) {
					for (String NEED : NEEDED_KEYS) {
						if (PL.languageList.get(parsed[0]).indexOf(NEED) < 0) {
							RUN_RESULT = false;
							continue baseWhile;
						}
					}
				}
				if (ConfigProperties.DEBUG) {
					System.out.println(LANG + "\\" + parsed[0] + ": OK");
				}
			}
			BR.close();

		}

		return RUN_RESULT;
	}

	public boolean fixDefaults() {
		boolean RUN_RESULT = true;
		for (String LANG : ConfigProperties.LANGS) {
			BufferedReader BR;
			try {
				BR = new BufferedReader(new FileReader(masterKey));
				PlayerLanguage PL = new PlayerLanguage(ConfigProperties.langMap.get(LANG));
				String line;
				FileConfiguration JAR_CONF = YamlConfiguration.loadConfiguration(
						Utils.convert2File("lang_temp_" + LANG, MythPlugin.getResource(LANG + ".yml")));
				baseWhile: while ((line = BR.readLine()) != null) {
					if (line.charAt(0) == '#') {
						continue baseWhile;
					}
					String[] parsed = line.split(":");
					if (PL.languageList.get(parsed[0]) == null || PL.languageList.get(parsed[0]).equals(" ")
							|| PL.languageList.get(parsed[0]).equals("")) {
						System.out.println("Fixing key in " + LANG + ": --> " + parsed[0]);
						String Convert = parsed[0].replaceAll("_", "-");

						ConfigProperties.langMap.get(LANG).set(Convert, JAR_CONF.getString(Convert));
						ConfigProperties.langMap.get(LANG).save(
								MythPlugin.getDataFolder() + File.separator + "lang" + File.separator + LANG + ".yml");
						continue baseWhile;
					}
				}
				BR.close();
			} catch (IOException e) {
				RUN_RESULT = false;
				e.printStackTrace();
			} finally {

			}
		}

		Utils.deleteTempFiles();
		return RUN_RESULT;
	}
}
