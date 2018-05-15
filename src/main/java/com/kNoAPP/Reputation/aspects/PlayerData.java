package com.kNoAPP.Reputation.aspects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerData {

	private static List<PlayerData> pds = new ArrayList<PlayerData>();
	
	private UUID uuid;
	private PlayerReputation pr;
	
	public PlayerData(Player p) {
		this.uuid = p.getUniqueId();
		this.pr = new PlayerReputation(p);
		
		refreshTag();
		
		pds.remove(this);
		pds.add(this);
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public PlayerReputation getReputation() {
		return pr;
	}
	
	public void refreshTag() {

	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
	
	public void remove() {
		pds.remove(this);
	}
	
	public static PlayerData getPlayerData(UUID uuid) {
		for(PlayerData pd : pds) if(pd.getUUID().equals(uuid)) return pd;
		return null;
	}
	
	public static List<PlayerData> getPlayerDatas() {
		return pds;
	}
}
