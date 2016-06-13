package de.ersterstreber.resources.chunkloaderx.listeners;

import de.ersterstreber.resources.chunkloaderx.Main;
import de.ersterstreber.resources.chunkloaderx.Manager;
import de.ersterstreber.resources.chunkloaderx.ParticlePlayer;
import de.ersterstreber.resources.chunkloaderx.item.ChunkLoaderItem;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import de.ersterstreber.resources.chunkloaderx.ChunkLoader;

public class BlockPlaceListener implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent e) {
		ItemStack chunkLoader = ChunkLoaderItem.getItem();
		chunkLoader.setAmount(e.getPlayer().getItemInHand().getAmount());
		if (!e.getPlayer().getItemInHand().equals(chunkLoader)) return;
		
		Player p = e.getPlayer();
		
		Manager m = Manager.getManager();
		
		Chunk chunk = e.getBlockPlaced().getChunk();
		
		if (m.isLoaded(chunk)) {
			p.sendMessage("�cThis chunk is already being kept loaded.");
			e.setCancelled(true);
			return;
		}
		
		if (m.getAllLoadedChunks(p.getUniqueId()).size() >= Main.getInstance().getMaximalChunksPerUser()) {
			p.sendMessage("�cYou already own too many chunks!");
			e.setCancelled(true);
			return;
		}
		
		ChunkLoader cl = new ChunkLoader(m.getRandomName(p.getUniqueId()), p.getUniqueId(), e.getBlockPlaced().getLocation());
		
		p.sendMessage("�2You successfully placed a ChunkLoader.");
		m.addChunkLoader(cl);
		
		new ParticlePlayer(2).playParticles(p, chunk);
	}
	
}
