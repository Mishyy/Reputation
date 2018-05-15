package com.kNoAPP.Reputation.aspects;

import org.bukkit.ChatColor;

public enum Message {

	MISSING(ChatColor.GOLD + "Permission> "),
	ARGS(ChatColor.GOLD + "Missing Args> "),
	USAGE(ChatColor.GOLD + "Usage> "),
	INFO(ChatColor.GOLD + "Info> "),
	WARN(ChatColor.GOLD + "Warn> "),
	REP(ChatColor.GOLD + "Rep> "),
	CONSOLE(ChatColor.GOLD + "Console> ", ChatColor.GRAY + "This command may only be run by players."),
	
	HELP("  ");
	
	private String prefix, suffix;
	
	private Message() {}
	
	private Message(String prefix) {
		this.prefix = prefix;
	}
	
	private Message(String prefix, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public String getMessage(String s) {
		if(this == MISSING) return prefix + ChatColor.GRAY + "You are missing Node [" + ChatColor.DARK_AQUA + s + ChatColor.GRAY + "]!";
		if(this == ARGS || this == USAGE || this == INFO || this == REP || this == CONSOLE) return prefix + ChatColor.GRAY + s;
		if(this == WARN) return prefix + ChatColor.RED + s;
		if(this == HELP) return prefix + ChatColor.GOLD + s.replaceFirst(" -", ChatColor.GRAY + " -");
		return null;
	}
	
	public String getMessage() {
		if(this == CONSOLE) return prefix + suffix;
		return null;
	}
}
