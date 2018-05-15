package com.kNoAPP.Reputation.aspects;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface SpecialItem {
	
	public static enum StaticItem implements SpecialItem {
		PLACE_HOLDER(ChatColor.GREEN + "<>", 1, (byte)5, Material.STAINED_GLASS_PANE,
				null, null, null, false);
		
		private ItemStack is;
		private String name;
		private int count;
		private byte data;
		private Material m;
		private String[] lores;
		private Enchantment[] enchants;
		private ItemFlag[] itemFlags;
		private boolean inv;
		
		private StaticItem(String name, int count, byte data, Material m, String[] lores, Enchantment[] enchants, ItemFlag[] itemFlags, boolean inv) {
			this.name = name;
			this.count = count;
			this.data = data;
			this.m = m;
			this.lores = lores;
			this.enchants = enchants;
			this.itemFlags = itemFlags;
			this.inv = inv;
		}
		
		public ItemStack getItem() {
			if(this.is != null) return this.is;
			else {
				ItemStack is = new ItemStack(m, count, data);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(name);
				
				ArrayList<String> finalLore = new ArrayList<String>();
				if(lores != null) for(String l : lores) finalLore.add(l);
				im.setLore(finalLore);
				
				if(enchants != null) for(Enchantment e : enchants) im.addEnchant(e, 1, false);
				if(itemFlags != null) for(ItemFlag iF : itemFlags) im.addItemFlags(iF);
				
				if(inv) im.spigot().setUnbreakable(true);
				
				is.setItemMeta(im);
				this.is = is;
				return getItem();
			}
		}
		
		public static ItemStack unbreakable(ItemStack is) {
			ItemMeta im = is.getItemMeta();
			im.spigot().setUnbreakable(true);
			is.setItemMeta(im);
			return is;
		}
	}
	
	public static enum DynamicItem implements SpecialItem {
		
		PLACE_HOLDER;
		
		private DynamicItem() {}
		
		public ItemStack getItem() {
			return null;
		}
	}
}
