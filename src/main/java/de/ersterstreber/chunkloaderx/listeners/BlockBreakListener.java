package de.ersterstreber.chunkloaderx.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.ersterstreber.chunkloaderx.ChunkLoader;
import de.ersterstreber.chunkloaderx.Main;
import de.ersterstreber.chunkloaderx.Manager;
import de.ersterstreber.chunkloaderx.item.ChunkLoaderItem;

public class BlockBreakListener implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent e) {
		if (e.getBlock().getType() != Material.GOLD_BLOCK) return;
		Player p = e.getPlayer();
		Location loc = e.getBlock().getLocation();
		Manager m = Manager.getManager();
		
		if (!m.existsChunkLoader(loc)) return;
		ChunkLoader cl = m.getChunkLoader(loc);
		
		if (!cl.getOwner().equals(p.getUniqueId())) {
			p.sendMessage("§cYou don't own this ChunkLoader.");
			e.setCancelled(true);
			return;
		}
		
		e.getBlock().getDrops().clear();
		if (Main.getInstance().dropOnBreak()) {
			loc.getWorld().dropItemNaturally(loc, ChunkLoaderItem.getItem());
		}
		
		m.removeChunkLoader(cl);
		
		p.sendMessage("§2ChunkLoader has been removed.");
	}
	
}
