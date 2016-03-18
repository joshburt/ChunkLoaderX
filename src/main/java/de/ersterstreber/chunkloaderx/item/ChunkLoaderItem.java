package de.ersterstreber.chunkloaderx.item;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChunkLoaderItem {

	public static ItemStack getItem() {
		ItemStack item = new ItemStack(Material.GOLD_BLOCK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6ChunkLoader");
		meta.setLore(Arrays.asList(new String[]{"§2This block keeps the chunk it's in loaded."}));
		item.setItemMeta(meta);
		
		return item;
	}
	
}
