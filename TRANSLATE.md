> 

Translators Needed!
=======

Hello,all! MythBans is really growing. I am officially starting to allow for 100% message customization in the plugin. I am looking for translators to help add more language support


## How do I start? ##

To start, You will need to create a new `[language name].yml` to get started.

I would recommend duplicating [this default file](https://github.com/MythTheWolf/MythBans-JavaPlugin/blob/master/src/main/resources/lang/en_US.yml) to get started.

Once you have copied it's contents to `/main/resources/lang/[lang name].yml`, you need to add it to the available language array.

This is located in the [config properties class](https://github.com/MythTheWolf/MythBans-JavaPlugin/blob/master/src/main/java/com/myththewolf/MythBans/lib/feilds/ConfigProperties.java). To add your language, just look for `String[] LANGS = { "en_US" };`. 
You do not need to add the .YML extension.
(for example,I'll add a pirate language)
```java
	public static String SYSTEM_LOCALE = "en_US";
	public static final String VERSION = "2.6.1";
	public static final String[] LANGS = { "en_US","en_PIRATE" };
```
 
 ## Language File Structure ##
 
 The way MythBans parses the Lang files is very simple. It follows standard YAML syntax,and parses Minecraft color codes.
 For more advanced user feedback, `placeholders` are used. (Things like these: `{0},{1},{2},{3},etc`)
 MythBans has a chart of what it replaces these placeholders with in [this excel sheet](https://github.com/MythTheWolf/MythBans-JavaPlugin/blob/master/Message%20Varibles.xlsx).
 Just follow that, and you are good to go.
 
 ## Final Notes ##
* When you are done,send a pull request!
* I'll look at your changes,and if things seem to be OK, i'll accept. However I WILL NOT verify translation accuracy.
* Fun langs accepted :)
