import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.MissingFormatArgumentException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Test;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.PlayerLanguage;

public class PlayerLangTest {
	boolean EXTRACT;
	JavaPlugin myth;
	public PlayerLangTest() {
		EXTRACT = true;
	}
	public void setJavaPlugin(JavaPlugin PL){
		EXTRACT = false;
		myth = PL;
	}
	@Test
	public void testTheThings() throws IOException {
		
		for (String theLang : ConfigProperties.LANGS) {
			System.out.println("***** Checking all Keys for " + theLang);
			PlayerLangTest PLT = new PlayerLangTest();
			ClassLoader CL = PLT.getClass().getClassLoader();
			File file = null;
			try {
				if(EXTRACT){
				file = new File(CL.getResource("lang/" + theLang + ".yml").getFile());
				}else{
					//file = new File(myth.getDataFolder()+file.separator+)
				}
			} catch (NullPointerException e) {
				throw new FileNotFoundException("Could not locate lang file for " + theLang);
			}

			PlayerLanguage PL = new PlayerLanguage(YamlConfiguration.loadConfiguration(file));
			String sCurrentLine;
			BufferedReader BR = new BufferedReader(new FileReader(CL.getResource("lang/expected_codes.txt").getFile()));
			while ((sCurrentLine = BR.readLine()) != null) {
				String[] parsed = sCurrentLine.split(":");
				if (!PL.languageList.containsKey(parsed[0])) {
					BR.close();
					throw new NullPointerException("While Parsing Langage: "+ theLang +" ERROR: Missing key-->" + parsed[0]);

				} else if (PL.languageList.get(parsed[0]) == null || PL.languageList.get(parsed[0]).equals(" ")
						|| PL.languageList.get(parsed[0]).equals("")) {

					BR.close();
					throw new NullPointerException("While Parsing Langage: "+ theLang + " ERROR: Value can't be NULL or empty-->" + parsed[0]);
				}

				String[] NEEDED_KEYS = parsed[1].split(";");
				if (NEEDED_KEYS.length > 0 && !NEEDED_KEYS[0].equals("NOP")) {
					for(String NEED : NEEDED_KEYS){
						if(PL.languageList.get(parsed[0]).indexOf(NEED) < 0){
							BR.close();
							throw new MissingFormatArgumentException("While Parsing Langage: "+ theLang +" Missing Placeholder '" + NEED+"' for key " + parsed[0]);
						}
					}
				}
				System.out.println(theLang+"\\"+parsed[0]+": OK");
			}
			
			BR.close();
		}
	}
}
