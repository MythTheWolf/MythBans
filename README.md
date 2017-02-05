## MythBans [![Build Status](https://travis-ci.org/MythTheWolf/MythBans-JavaPlugin.svg?branch=master)](https://travis-ci.org/MythTheWolf/MythBans-JavaPlugin)

Powerfull Craftbukkit plugin that allows you to manage bans VIA SQL in-game and through a web UI.

## Features

* SQL Storage of all bans, logs, and users
* Manage bans through UI, unban,ban,search users online and have it push to your server
* IP Management: Ban,kick,mute,etc supports IPs
* Manage Server messages & kick messages
* Custom permissions for each command
* [Planned] xenForo intergration that moves users to a group in which they can only appeal ban, if accepted, server will auto pardon.
* Much more to come!

## Installation

* Download the .JAR file from the Releases section. 
* Copy/Upload to /plugins
* Restart
* Enjoy!

## API Reference

You can access methods to give out every action. You just need to have the UUID of the player, command sender, and connection info.
Example:
```java
//Instaniate a Player cache Varible, so we can get players by username
private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());
//Instaniate DatabaseCommands, it holds all of the "action" methods
private DatabaseCommands dbc = new DatabaseCommands();
public void banThatUser(String name)
{
  OfflinePlayer p = pCache.getOfflinePlayerExact(name);
  if(p == null){
      //send a message saying that user does not exist
     return;
  }else{
    //banUser(UUID to ban, sender UUID use CONSOLE if from console, reason)
    dbc.banUser(p.getUniqueId().toString(), "CONSOLE", "Cuz I don't like you");
    return;
  }
}
```
## Contributors
I have to actually code the web UI now, and still have a lot to do. Want to help? Clone and send pull requests. Be sure that if you are doing Web UI, use the right branch.
## Motivation
I see lots of servers with many users, but can't remember names of who they ban. Lots of ban managers out there exist, but they make you use THEIR UI.
With my plugin, you can host it yourself. (As long as you have PHP of course :D )
## License

GNU General Public License v3.0
