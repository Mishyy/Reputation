package com.kNoAPP.Reputation.aspects;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Actions implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		new PlayerReputation(p);
		join(p);
	}
	
	public static void join(Player p) {
		new PlayerData(p);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		quit(p);
	}
	
	public static void quit(Player p) {
		PlayerData pd = PlayerData.getPlayerData(p.getUniqueId());
		pd.remove();
	}
	
	/*
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			PlayerData pd = PlayerData.getPlayerData(p.getUniqueId());
			
			if(e.getDamager() instanceof Player) {
				Player d = (Player) e.getDamager();
				PlayerData dd = PlayerData.getPlayerData(d.getUniqueId());
				pd.setKiller(dd);
			}
			if(e.getDamager() instanceof Projectile) {
				Projectile proj = (Projectile) e.getDamager();
				if(proj.getShooter() != null && proj.getShooter() instanceof Player) {
						Player d = (Player) proj.getShooter();
						PlayerData dd = PlayerData.getPlayerData(d.getUniqueId());
						pd.setKiller(dd);
					} else e.setCancelled(true);
				}
			}
			if(e.getDamager() instanceof TNTPrimed) {
				TNTPrimed tp = (TNTPrimed) e.getDamager();
				if(tp.hasMetadata("EmpoweredTNT")) {
					if(g.getGameState() != GameState.COLLECTION) {
						String name = tp.getMetadata("EmpoweredTNT").get(0).asString();
						Player d = Bukkit.getPlayer(name);
						if(d != null && d.isOnline()) {
							PlayerData dd = PlayerData.getPlayerData(d.getUniqueId());
							if(dd == null || dd.getMode() == Mode.BUILD) return;
							
							if(dd.hasEmpoweredAttack()) {
								e.setDamage(e.getDamage() + 4);
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1F, 0.8F);
								p.getWorld().spawnParticle(Particle.SLIME, p.getLocation().clone().add(0, 0.5, 0), 4, 0.5F, 0.25F, 0.5F, 0.01);
								d.removePotionEffect(PotionEffectType.INVISIBILITY);
								d.removePotionEffect(PotionEffectType.SPEED);
								d.getInventory().setArmorContents(pd.getArmorStorage());
								dd.setArmorStorage(new ItemStack[]{});
							}
							if(dd.getMysterySubclass() == MysterySubclass.EXECUTIONER && p.getHealth() <= (dd.isBuffed() ? 4 : (dd.isDebuffed() ? 2 : 3))) 
								e.setDamage(50);
							if(dd.getMysterySubclass() == MysterySubclass.VAMPIRE)
								d.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, dd.isDebuffed() ? 20 : 40, dd.isBuffed() ? 1 : 0, false, false));
							
							if(g.getGameState() != GameState.PREGAME && 
									pd.getMode() == Mode.PLAY && !pd.isSpectating() && dd.getMode() == Mode.PLAY && !dd.isSpectating())
								pd.setKiller(dd);
						}
					} else e.setCancelled(true);
				}
			}
		}
	}
	*/
}
