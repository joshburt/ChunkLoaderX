package de.ersterstreber.chunkloaderx.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ersterstreber.chunkloaderx.ChunkLoader;
import de.ersterstreber.chunkloaderx.Main;
import de.ersterstreber.chunkloaderx.Manager;
import de.ersterstreber.chunkloaderx.ParticlePlayer;

public class PlayerInteractListener implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Manager m = Manager.getManager();
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() != Material.NAME_TAG) {
			if (!m.existsChunkLoader(e.getClickedBlock().getLocation())) return;
			new ParticlePlayer(2).playParticles(e.getPlayer(), e.getClickedBlock().getChunk());
		} else {
			//Rename ChunkLoader
			if (!m.existsChunkLoader(e.getClickedBlock().getLocation())) return;
			ChunkLoader cl = m.getChunkLoader(e.getClickedBlock().getLocation());
			
			if (!cl.getOwner().equals(p.getUniqueId())) {
				p.sendMessage("§cYou don't own this ChunkLoader!");
				return;
			}
			
			p.sendMessage("§2Now type in the new name of your ChunkLoader.");
			Main.getInstance().getRenaming().put(p, cl);
			e.setCancelled(true);
		}
	}
}
