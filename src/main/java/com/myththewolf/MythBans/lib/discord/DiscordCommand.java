package com.myththewolf.MythBans.lib.discord;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * This annotation class is for command parsing options
 * @author MythTheWolf
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DiscordCommand {
	/**
	 * If the command requires a root account
	 * @return true/fale
	 */
	boolean requiresRoot() default false;
	/**
	 * If the command requires a linked account
	 * @return true/false
	 */
	boolean requiresLinked() default false;
	/**
	 * Wether to delete the command message. E.X: !ping --> if true---> delete
	 * @return true/false
	 */
	boolean deleteTriggerMessage() default true;
}
