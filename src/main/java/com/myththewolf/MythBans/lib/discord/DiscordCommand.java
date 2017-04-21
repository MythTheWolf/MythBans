package com.myththewolf.MythBans.lib.discord;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DiscordCommand {
	boolean requiresRoot() default false;
	boolean requiresLinked() default false;
	boolean deleteTriggerMessage() default true;
}
