package de.ersterstreber.resources.chunkloaderx.listeners;

import java.util.ArrayList;
import java.util.List;

import de.ersterstreber.resources.chunkloaderx.Manager;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.ersterstreber.resources.chunkloaderx.ChunkLoader;
import de.ersterstreber.resources.chunkloaderx.KMCChunk;
import de.ersterstreber.resources.chunkloaderx.Main;

public class ChunkUnloadListener implements Listener {

	private List<Chunk> chunks = new ArrayList<Chunk>();
	
	@EventHandler
	public void onUnload(final ChunkUnloadEvent e) {
		//By command
		Manager m = Manager.getManager();
		for (KMCChunk c : m.getBoughtChunks()) {
			if (c.getChunk().equals(e.getChunk())) {
				if (!Main.getInstance().infiniteLoadedTimeCommand()) {
					//Loaded time not infinite
					if (!chunks.contains(e.getChunk())) {
						chunks.add(e.getChunk());
						
						new BukkitRunnable() {
							
							@Override
							public void run() {
								chunks.remove(e.getChunk());
							}
						}.runTaskLaterAsynchronously(Main.getInstance(), Main.getInstance().getLoadedTimeCommand() * 20L);
						e.setCancelled(true);
						return;
					} else {
						e.setCancelled(false);
					}
				} else {
					//Loaded time is infinite
					e.setCancelled(true);
					return;
				}
			}
		}
		
		//ChunkLoaders
		for (ChunkLoader cl : m.getChunkLoaders()) {
			if (cl.getLocation().getChunk().equals(e.getChunk())) {
				if (!Main.getInstance().infiniteLoadedTimeChunkLoader()) {
					//Not infinite
					if (!chunks.contains(e.getChunk())) {
						chunks.add(e.getChunk());
						
						new BukkitRunnable() {
							
							@Override
							public void run() {
								chunks.remove(e.getChunk());
							}
						}.runTaskLaterAsynchronously(Main.getInstance(), Main.getInstance().getLoadedTimeChunkLoader() * 20L);
						e.setCancelled(true);
						return;
					} else {
						e.setCancelled(false);
					}
				} else {
					//Infinite
					e.setCancelled(true);
					return;
				}
			}
		}
	}
}
