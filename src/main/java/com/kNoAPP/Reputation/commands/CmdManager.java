package com.kNoAPP.Reputation.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kNoAPP.Reputation.aspects.Message;
import com.kNoAPP.Reputation.aspects.PlayerReputation;
import com.kNoAPP.Reputation.utils.Tools;


public class CmdManager implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("rep")) {
			if(args.length == 0) {
				sender.sendMessage(Message.INFO.getMessage("Reputation - VindexCraft"));
				sender.sendMessage(ChatColor.DARK_GREEN + "------------------");
				sender.sendMessage(Message.HELP.getMessage("/rep help - Show Help"));
				if(sender.hasPermission("rep.leaderboard")) sender.sendMessage(Message.HELP.getMessage("/rep leaderboard - View leaderboard"));
				if(sender.hasPermission("rep.lookup")) sender.sendMessage(Message.HELP.getMessage("/rep lookup (player) - Lookup a player"));
				if(sender.hasPermission("rep.upvote")) sender.sendMessage(Message.HELP.getMessage("/rep upvote [player] - Upvote a player"));
				if(sender.hasPermission("rep.downvote")) sender.sendMessage(Message.HELP.getMessage("/rep downvote [player] - Downvote a player"));
				if(sender.hasPermission("rep.clear")) sender.sendMessage(Message.HELP.getMessage("/rep clear [player] - Clear player's votes"));
				if(sender.hasPermission("rep.set")) sender.sendMessage(Message.HELP.getMessage("/rep set [player] [#] - Set a reputation"));
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("help")) Bukkit.dispatchCommand(sender, "rep");
				if(args[0].equalsIgnoreCase("lookup")) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						if(p.hasPermission("rep.lookup")) {
							PlayerReputation pr = new PlayerReputation(p.getUniqueId());
							p.sendMessage(Message.REP.getMessage("Your reputation: " + pr.getReputationTag().getTag() 
									+ ChatColor.LIGHT_PURPLE + " [" + ChatColor.GRAY + pr.getReputation() + ChatColor.LIGHT_PURPLE + "]"));
							return true;
						} else p.sendMessage(Message.MISSING.getMessage("rep.lookup"));
					} else sender.sendMessage(Message.CONSOLE.getMessage());
				}
				if(args[0].equalsIgnoreCase("leaderboard")) {
					if(sender.hasPermission("rep.leaderboard")) {
						for(String s : PlayerReputation.getLeaderboard()) sender.sendMessage(s);
						return true;
					} else sender.sendMessage(Message.MISSING.getMessage("rep.leaderboard"));
				}
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("lookup")) {
					if(sender.hasPermission("rep.lookup")) {
						OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
						if(op != null) {
							PlayerReputation pr = new PlayerReputation(op.getUniqueId());
							sender.sendMessage(Message.REP.getMessage(op.getName() + "'s reputation: " + pr.getReputationTag().getTag() 
									+ ChatColor.LIGHT_PURPLE + " [" + ChatColor.GRAY + pr.getReputation() + ChatColor.LIGHT_PURPLE + "]"));
							return true;
						} else sender.sendMessage(Message.WARN.getMessage("Could not find that player."));
					} else sender.sendMessage(Message.MISSING.getMessage("rep.lookup"));
				}
				if(args[0].equalsIgnoreCase("upvote")) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						if(sender.hasPermission("rep.upvote")) {
							OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
							if(op != null) {
								PlayerReputation pr = new PlayerReputation(p);
								pr.setVote(op.getUniqueId(), PlayerReputation.UPVOTE);
								p.sendMessage(Message.REP.getMessage(ChatColor.GREEN + "You have upvoted " + ChatColor.YELLOW + op.getName() + ChatColor.GREEN + "."));
								return true;
							} else p.sendMessage(Message.WARN.getMessage("Could not find that player."));
						} else p.sendMessage(Message.MISSING.getMessage("rep.upvote"));
					} else sender.sendMessage(Message.CONSOLE.getMessage());
				}
				if(args[0].equalsIgnoreCase("downvote")) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						if(sender.hasPermission("rep.downvote")) {
							OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
							if(op != null) {
								PlayerReputation pr = new PlayerReputation(p);
								pr.setVote(op.getUniqueId(), PlayerReputation.DOWNVOTE);
								p.sendMessage(Message.REP.getMessage(ChatColor.RED + "You have downvoted " + ChatColor.YELLOW + op.getName() + ChatColor.RED + "."));
								return true;
							} else p.sendMessage(Message.WARN.getMessage("Could not find that player."));
						} else p.sendMessage(Message.MISSING.getMessage("rep.downvote"));
					} else sender.sendMessage(Message.CONSOLE.getMessage());
				}
				if(args[0].equalsIgnoreCase("clear")) {
					if(sender.hasPermission("rep.clear")) {
						OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
						if(op != null) {
							PlayerReputation pr = new PlayerReputation(op.getUniqueId());
							pr.clearReceivedVotes();
							sender.sendMessage(Message.REP.getMessage("Votes on " + ChatColor.YELLOW + op.getName() + ChatColor.GRAY + " have been cleared."));
							return true;
						} else sender.sendMessage(Message.WARN.getMessage("Could not find that player."));
					} else sender.sendMessage(Message.MISSING.getMessage("rep.clear"));
				}
			}
			if(args.length == 3) {
				if(args[0].equalsIgnoreCase("set")) {
					if(sender.hasPermission("rep.set")) {
						OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
						if(op != null) {
							Object[] toInt = Tools.canParseToInteger(args[2]);
							if((boolean)toInt[0]) {
								int mod = (int) toInt[1];
								PlayerReputation pr = new PlayerReputation(op.getUniqueId());
								pr.setReputation(mod, false);
								sender.sendMessage(Message.REP.getMessage("Successfully updated " + ChatColor.YELLOW + op.getName() + ChatColor.GRAY + "."));
								sender.sendMessage(Message.REP.getMessage("Votes may still be in effect. Clear with /rep clear [player]."));
								return true;
							} else sender.sendMessage(Message.WARN.getMessage("Please enter a valid number."));
						} else sender.sendMessage(Message.WARN.getMessage("Could not find that player."));
					} else sender.sendMessage(Message.MISSING.getMessage("rep.set"));
				}
			}
		}
		return false;
	}

}
