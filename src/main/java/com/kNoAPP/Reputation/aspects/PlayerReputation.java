package com.kNoAPP.Reputation.aspects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.kNoAPP.Reputation.data.MySQL;
import com.kNoAPP.Reputation.data.Table;

public class PlayerReputation {
	
	private UUID uuid;

	public PlayerReputation(Player p) {
		this.uuid = p.getUniqueId();
		
		if(MySQL.getString(Table.REP_STATS.getName(), "uuid", "uuid", uuid.toString()) != null) {
			MySQL.update(Table.REP_STATS.getName(), "name", p.getName(), "uuid", uuid.toString());
		} else {
			MySQL.insert(Table.REP_STATS.getName(), uuid.toString(), p.getName(), "500");
		}
	}
	
	public PlayerReputation(UUID uuid) {
		OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
		this.uuid = uuid;
		
		if(MySQL.getString(Table.REP_STATS.getName(), "uuid", "uuid", uuid.toString()) != null) {
			MySQL.update(Table.REP_STATS.getName(), "name", op.getName(), "uuid", uuid.toString());
		} else {
			MySQL.insert(Table.REP_STATS.getName(), uuid.toString(), op.getName(), "500");
		}
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public int getReputationBeforeVotes() {
		return MySQL.getInt(Table.REP_STATS.getName(), "rep", "uuid", uuid.toString());
	}
	
	public int getReputation() {
		int rep = MySQL.getInt(Table.REP_STATS.getName(), "rep", "uuid", uuid.toString());
		HashMap<UUID, String> received = getReceivedVotes();
		for(UUID giver : received.keySet()) {
			String vote = received.get(giver);
			if(vote.equals(UPVOTE)) rep += 2;
			if(vote.equals(DOWNVOTE)) rep -= 1;
		}
		return rep;
	}
	
	public ReputationTag getReputationTag() {
		return ReputationTag.getReputationTag(getReputation());
	}
	
	public static final String UPVOTE = "UP";
	public static final String DOWNVOTE = "DOWN";
	public String getVote(UUID receiver) {
		return MySQL.getString(Table.REP_VOTES.getName(), "type", "giver", uuid.toString(), "receiver", receiver.toString());
	}
	
	public HashMap<UUID, String> getGivenVotes() {
		HashMap<UUID, String> votes = new HashMap<UUID, String>();
		for(String receiver : MySQL.getStringList(Table.REP_VOTES.getName(), "receiver", "giver", uuid.toString())) 
			votes.put(UUID.fromString(receiver), MySQL.getString(Table.REP_VOTES.getName(), "type", "giver", uuid.toString(), "receiver", receiver));
		return votes;
	}
	
	public HashMap<UUID, String> getReceivedVotes() {
		HashMap<UUID, String> votes = new HashMap<UUID, String>();
		for(String giver : MySQL.getStringList(Table.REP_VOTES.getName(), "giver", "receiver", uuid.toString())) 
			votes.put(UUID.fromString(giver), MySQL.getString(Table.REP_VOTES.getName(), "type", "receiver", uuid.toString(), "giver", giver));
		return votes;
	}
	
	public void setVote(UUID receiver, String type) {
		if(getVote(receiver) != null) MySQL.update(Table.REP_VOTES.getName(), "type", type, "giver", uuid.toString(), "receiver", receiver.toString());
		else MySQL.insert(Table.REP_VOTES.getName(), uuid.toString(), receiver.toString(), type);
	}
	
	public void clearGivenVote(UUID receiver) {
		MySQL.delete(Table.REP_VOTES.getName(), "giver", uuid.toString(), "receiver", receiver.toString());
	}
	
	public void clearReceivedVote(UUID giver) {
		MySQL.delete(Table.REP_VOTES.getName(), "receiver", uuid.toString(), "giver", giver.toString());
		
		PlayerData pd = PlayerData.getPlayerData(uuid);
		if(pd != null) pd.refreshTag();
	}
	
	public void clearGivenVotes() {
		for(String receiver : MySQL.getStringList(Table.REP_VOTES.getName(), "receiver", "giver", uuid.toString()))
			MySQL.delete(Table.REP_VOTES.getName(), "giver", uuid.toString(), "receiver", receiver.toString());
	}
	
	public void clearReceivedVotes() {
		for(String giver : MySQL.getStringList(Table.REP_VOTES.getName(), "giver", "receiver", uuid.toString())) 
			MySQL.delete(Table.REP_VOTES.getName(), "giver", giver.toString(), "receiver", uuid.toString());
		
		PlayerData pd = PlayerData.getPlayerData(uuid);
		if(pd != null) pd.refreshTag();
	}
	
	public void setReputation(int rep, boolean clearVotes) {
		MySQL.update(Table.REP_STATS.getName(), "rep", rep, "uuid", uuid.toString());
		if(clearVotes) clearReceivedVotes();
		
		PlayerData pd = PlayerData.getPlayerData(uuid);
		if(pd != null) pd.refreshTag();
	}
	
	public static List<String> getLeaderboard() {
		List<String> leaderboard = new ArrayList<String>();
		leaderboard.add(Message.REP.getMessage("Here's the current rankings..."));
		int i = 1;
		for(String uuid : MySQL.getStringListOrdered(Table.REP_STATS.getName(), "uuid", "rep", "DESC")) {
			leaderboard.add(ChatColor.DARK_GREEN + "     #" + i + ChatColor.WHITE + " - " + ChatColor.YELLOW + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() 
					+ ChatColor.WHITE + " - " + ChatColor.LIGHT_PURPLE + new PlayerReputation(UUID.fromString(uuid)).getReputation());
			if(i < 10) i++;
			else break;
		}
		return leaderboard;
	}
	
	public static enum ReputationTag {
		PSYCHOTIC(ChatColor.DARK_RED + "Psychotic", Integer.MIN_VALUE, 0),
		INSANE(ChatColor.RED + "Insane", 1, 200),
		DANGEROUS(ChatColor.YELLOW + "Dangerous", 201, 400),
		NEUTRAL(ChatColor.GRAY + "Neutral", 401, 600),
		RESPECTABLE(ChatColor.GOLD + "Respectable", 601, 800),
		ADMIRABLE(ChatColor.DARK_GREEN + "Admirable", 801, 999),
		HONOURABLE(ChatColor.GREEN + "Honourable", 1000, Integer.MAX_VALUE);
		
		private String tag;
		private int low, high;
		
		private ReputationTag(String tag, int low, int high) {
			this.tag = tag;
			this.low = low;
			this.high = high;
		}
		
		public String getTag() {
			return tag;
		}
		
		public int getLow() {
			return low;
		}
		
		public int getHigh() {
			return high;
		}
		
		public static ReputationTag getReputationTag(int rep) {
			for(ReputationTag rt : values()) if(rt.getLow() <= rep && rep <= rt.getHigh()) return rt;
			return null;
		}
		
	}
}
