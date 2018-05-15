package com.kNoAPP.Reputation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;

import com.kNoAPP.Reputation.aspects.Actions;
import com.kNoAPP.Reputation.aspects.CrackShotActions;
import com.kNoAPP.Reputation.aspects.PlayerData;
import com.kNoAPP.Reputation.aspects.PlayerReputation;
import com.kNoAPP.Reputation.commands.CmdManager;
import com.kNoAPP.Reputation.data.Data;
import com.kNoAPP.Reputation.data.MySQL;

//Copyright Alden "kNoAPP" Bansemer 2018
public class Reputation extends JavaPlugin {

	private static boolean failed = false;
	private static boolean crackshot = false;
	private static Objective obj;
	private static Reputation plugin;
	
	@Override
	public void onEnable() {
		long tStart = System.currentTimeMillis();
		plugin = this;
		register();
		importData();
		importAspects();
		long tEnd = System.currentTimeMillis();
		getPlugin().getLogger().info("Successfully Enabled! (" + (tEnd - tStart) + " ms)");
		
		if(failed) getPlugin().getPluginLoader().disablePlugin(this);
		
	}
	
	@Override
	public void onDisable() {
		long tStart = System.currentTimeMillis();
		exportAspects();
		exportData();
		long tEnd = System.currentTimeMillis();
		getPlugin().getLogger().info("Successfully Disabled! (" + (tEnd - tStart) + " ms)");
	}
	
	private void register() {
		crackshot = getServer().getPluginManager().getPlugin("CrackShot") != null;
		if(crackshot) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[" + getPlugin().getName() + "] Detected CrackShot! Utilizing... ");
			this.getServer().getPluginManager().registerEvents(new CrackShotActions(), this);
		}
		
		this.getServer().getPluginManager().registerEvents(new Actions(), this);
		this.getCommand("rep").setExecutor(new CmdManager());
	}
	
	public static void importData() {
		getPlugin().getLogger().info("Importing .yml Files...");
		for(Data d : Data.values()) {
			if(d != Data.CONFIG) {
				if(Data.CONFIG.getFileConfig().getBoolean("UseMainFolder") == true) d.setFile("");
				else d.setFile(Data.CONFIG.getFileConfig().getString("UseCustomFolder"));
			}
			d.createDataFile();
		}
		
		if(!MySQL.loadConnection()) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + getPlugin().getName() + "] Please fix your database settings and try again!");
			failed = true;
		}
	}
	
	public static void exportData() {
		getPlugin().getLogger().info("Exporting .yml Files...");
		for(Data d : Data.values()) d.logDataFile();
		if(!failed) MySQL.killConnection();
	}
	
	public static void importAspects() {
		getPlugin().getLogger().info("Importing Aspects...");
		for(Player pl : Bukkit.getOnlinePlayers()) Actions.join(pl);
		
		new BukkitRunnable() {
			public void run() {
				for(PlayerData pd : PlayerData.getPlayerDatas()) {
					PlayerReputation pr = pd.getReputation();
					pr.setReputation(pr.getReputation()+1, false);
				}
			}
		}.runTaskTimer(plugin, 0L, 18000L);
	}
	
	public static void exportAspects() {
		getPlugin().getLogger().info("Exporting Aspects...");
		for(Player pl : Bukkit.getOnlinePlayers()) Actions.quit(pl);
	}
	
	public static boolean usingCrackShot() {
		return crackshot;
	}
	
	public static Reputation getPlugin() {
		return plugin;
	}
	
	public static Objective getObjective() {
		return obj;
	}
}