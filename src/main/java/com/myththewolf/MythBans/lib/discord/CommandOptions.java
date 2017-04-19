package com.myththewolf.MythBans.lib.discord;

public @interface CommandOptions {
	boolean requiresRoot() default false;
	boolean requiresLinked() default false;
}
