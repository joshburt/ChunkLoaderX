package de.ersterstreber.chunkloaderx.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import de.ersterstreber.chunkloaderx.Main;
import de.ersterstreber.chunkloaderx.Manager;
import de.ersterstreber.chunkloaderx.item.ChunkLoaderItem;

public class ExplosionListener implements Listener {

	private Manager m = Manager.getManager();
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onExplode(BlockExplodeEvent e) {
		for (Block b : e.blockList()) {
			if (m.existsChunkLoader(b.getLocation())) {
				m.removeChunkLoader(m.getChunkLoader(b.getLocation()));
				if (Main.getInstance().dropOnBreak()) {
					b.getWorld().dropItemNaturally(b.getLocation(), ChunkLoaderItem.getItem());
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent e) {
		for (Block b : e.blockList()) {
			if (m.existsChunkLoader(b.getLocation())) {
				m.removeChunkLoader(m.getChunkLoader(b.getLocation()));
				if (Main.getInstance().dropOnBreak()) {
					b.getWorld().dropItemNaturally(b.getLocation(), ChunkLoaderItem.getItem());
				}
			}
		}
	}
	
}
