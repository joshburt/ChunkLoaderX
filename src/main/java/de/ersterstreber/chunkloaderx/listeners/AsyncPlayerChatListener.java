package de.ersterstreber.chunkloaderx.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.ersterstreber.chunkloaderx.ChunkLoader;
import de.ersterstreber.chunkloaderx.Main;
import de.ersterstreber.chunkloaderx.Manager;

public class AsyncPlayerChatListener implements Listener {

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent e) {
		if (!Main.getInstance().getRenaming().containsKey(e.getPlayer())) return;
		
		e.setCancelled(true);
		
		Player p = e.getPlayer();
		
		ChunkLoader cl = Main.getInstance().getRenaming().get(e.getPlayer());
		String newName = e.getMessage();
		
		Manager m = Manager.getManager();
		
		if (m.exists(p.getUniqueId(), newName)) {
			p.sendMessage("§cYou already own a chunk/ChunkLoader with that name.");
			p.sendMessage("§2Please choose another name.");
			return;
		}
		
		m.changeNameOfChunkLoader(cl, newName);
		
		e.getPlayer().sendMessage("§2Your ChunkLoader has been renamed to §f" + newName + "§2.");
		
		Main.getInstance().getRenaming().remove(e.getPlayer());
	}
	
}
