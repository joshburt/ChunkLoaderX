package de.ersterstreber.resources.chunkloaderx.commands;

import java.util.UUID;

import de.ersterstreber.resources.chunkloaderx.Manager;
import de.ersterstreber.resources.chunkloaderx.ParticlePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ersterstreber.resources.chunkloaderx.ChunkLoader;
import de.ersterstreber.resources.chunkloaderx.KMCChunk;
import de.ersterstreber.resources.chunkloaderx.Main;
import de.ersterstreber.resources.chunkloaderx.item.ChunkLoaderItem;

public class KMCCommand implements CommandExecutor {
	
	private Manager m = Manager.getManager();
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("�cYou are not a player!");
			return true;
		}
		
		Player p = (Player) sender;
		
		//buy
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("buy")) {
				
				if (!p.hasPermission("cl.buy")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				String name = args[1];
				if (Main.getInstance().usingVault()) {
					//Using economy
					double balance = Main.getInstance().getEconomy().getBalance(p);
					if (balance >= Main.getInstance().getPrice()) {
						//Enough money
						
						if (m.isLoaded(p.getLocation().getChunk())) {
							p.sendMessage("�cThis chunk is already being kept loaded.");
							return true;
						}
						
						if (m.exists(p.getUniqueId(), name)) {
							p.sendMessage("�cYou already own a chunk/ChunkLoader with that name.");
							return true;
						}
						
						if (m.getAllLoadedChunks(p.getUniqueId()).size() >= Main.getInstance().getMaximalChunksPerUser()) {
							p.sendMessage("�cYou already own too many chunks!");
							return true;
						}
						
						KMCChunk chunk = new KMCChunk(name, p.getUniqueId(), p.getLocation().getChunk());
						
						Main.getInstance().getEconomy().withdrawPlayer(p, Main.getInstance().getPrice());
						
						m.addChunk(chunk);
						
						new ParticlePlayer(3).playParticles(p, chunk.getChunk());
						
						p.sendMessage("�2You successfully bought this chunk.");
						return true;
					} else {
						//Not enough money
						p.sendMessage("�cYou don't have enough money. (" + (balance - Main.getInstance().getPrice()) + Main.getInstance().getEconomy().currencyNamePlural() + ")");
						return true;
					}
				} else {
					//Not using economy
					
					if (m.isLoaded(p.getLocation().getChunk())) {
						p.sendMessage("�cThis chunk is already being kept loaded.");
						return true;
					}
					
					if (m.exists(p.getUniqueId(), name)) {
						p.sendMessage("�cYou already own a chunk/ChunkLoader with that name.");
						return true;
					}
					
					if (m.getAllLoadedChunks(p.getUniqueId()).size() >= Main.getInstance().getMaximalChunksPerUser()) {
						p.sendMessage("�cYou already own too many chunks!");
						return true;
					}
					
					KMCChunk chunk = new KMCChunk(name, p.getUniqueId(), p.getLocation().getChunk());
					
					m.addChunk(chunk);
					
					p.sendMessage("�2You successfully bought this chunk.");
					return true;
				}
			}
		}
		
		//chunkloader
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("chunkloader")) {
				
				if (!p.hasPermission("cl.chunkloader")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				if (!Main.getInstance().usingChunkLoader()) {
					p.sendMessage("�cYour server doesn't support this feature.");
					return true;
				}
				
				if (Main.getInstance().usingVault()) {
					//Using economy
					double balance = Main.getInstance().getEconomy().getBalance(p);
					if (balance >= Main.getInstance().getPrice()) {
						//Enough money
						
						Main.getInstance().getEconomy().withdrawPlayer(p, Main.getInstance().getPrice());
						
						p.getWorld().dropItem(p.getLocation(), ChunkLoaderItem.getItem());
						
						p.sendMessage("�2You successfully bought a ChunkLoader.");
						
						return true;
					} else {
						//Not enough money
						p.sendMessage("�cYou don't have enough money. (" + (balance - Main.getInstance().getPrice()) + Main.getInstance().getEconomy().currencyNamePlural() + ")");
						return true;
					}
				} else {
					p.getWorld().dropItem(p.getLocation(), ChunkLoaderItem.getItem());
					
					p.sendMessage("�2You successfully bought a ChunkLoader.");
				}
			}
		}
		
		//remove
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("remove")) {
				
				if (!p.hasPermission("cl.remove")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				if (!m.ownsChunk(p.getUniqueId(), p.getLocation().getChunk())) {
					p.sendMessage("�cYou don't own this chunk!");
					return true;
				}
				
				KMCChunk c = m.getChunk(p.getLocation().getChunk());
					
				m.removeChunk(c);
				
				p.sendMessage("�2The chunk has been removed successfully.");
				return true;
			}
		}
		
		//remove others
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("remove")) {
				
				if (!p.hasPermission("cl.remove.others")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				String name = args[1];
				String player = args[2];
				UUID uuid = Bukkit.getOfflinePlayer(player).getUniqueId();
				
				if (uuid == null) {
					p.sendMessage("�cThe player �f" + player + " �chas never played on this server before!");
					return true;
				}
				
				if (!m.ownsChunk(name, uuid)) {
					p.sendMessage("�cThe player �f" + player + " �cdoesn't own a chunk with that name.");
					return true;
				}
				
				KMCChunk c = m.getChunk(uuid, name);
				
				m.removeChunk(c);
				
				p.sendMessage("�2The chunk has been removed successfully.");
				return true;	
			}
		}
		
		//list
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				
				if (!p.hasPermission("cl.list")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				if (m.getBoughtChunks(p.getUniqueId()).size() == 0) {
					p.sendMessage("�cThere are no chunks which are being kept loaded for you.");
				} else {
					p.sendMessage("�2These chunks are being kept loaded for you:");
					for (KMCChunk c : m.getBoughtChunks(p.getUniqueId())) {
						p.sendMessage("  �2- �f[" + c.getName() + ", " + c.getChunk().getWorld().getName() + ", " + c.getChunk().getX() * 16 + ", " + c.getChunk().getZ() * 16 + "]");
						
						new ParticlePlayer(10).playParticles(p, c.getChunk());
					}
				}
				
				if (m.getChunkLoaders(p.getUniqueId()).size() == 0) {
					p.sendMessage("�cYou don't own any ChunkLoaders.");
				} else {
					p.sendMessage("�2You own these ChunkLoaders:");
					
					for (ChunkLoader cl : m.getChunkLoaders(p.getUniqueId())) {
						Chunk c = cl.getLocation().getChunk();
						p.sendMessage("  �2- �f[" + cl.getName() + ", " + c.getWorld().getName() + ", " + c.getX() * 16 + ", " + c.getZ() * 16 + "]");
						
						new ParticlePlayer(10).playParticles(p, c);
					}
				}
				return true;
			}
		}
		
		//list by name
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("list")) {
				
				if (!p.hasPermission("cl.list.others")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				String player = args[1];
				UUID uuid = Bukkit.getOfflinePlayer(player).getUniqueId();
				if (uuid == null) {
					p.sendMessage("�cThe player �f" + player + " �chas never played on this server before!");
					return true;
				}
				
				if (m.getBoughtChunks(uuid).size() == 0) {
					p.sendMessage("�2There are no chunks which are being kept loaded for �f" + player + "�2.");
				} else {
					p.sendMessage("�2These chunks are being kept loaded for �f" + player + "�2:");
					for (KMCChunk c : m.getBoughtChunks(uuid)) {
						p.sendMessage("  �2- �f[" + c.getName() + ", " + c.getChunk().getWorld().getName() + ", " + c.getChunk().getX() * 16 + ", " + c.getChunk().getZ() * 16 + "]");
						
						new ParticlePlayer(10).playParticles(p, c.getChunk());
					}
				}
				
				if (m.getChunkLoaders(uuid).size() == 0) {
					p.sendMessage(player + " �2doesn't own any ChunkLoaders.");
				} else {
					p.sendMessage(player + " �2owns these ChunkLoaders:");
					
					for (ChunkLoader cl : m.getChunkLoaders(uuid)) {
						Chunk c = cl.getLocation().getChunk();
						p.sendMessage("  �2- �f[" + cl.getName() + ", " + c.getWorld().getName() + ", " + c.getX() * 16 + ", " + c.getZ() * 16 + "]");
						
						new ParticlePlayer(10).playParticles(p, c);
					}
				}
				return true;
			}
		}
		
		//rename
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("rename")) {
				
				if (!p.hasPermission("cl.rename")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				String newName = args[1];
				
				if (!m.isLoaded(p.getLocation().getChunk())) {
					p.sendMessage("�cYou don't own this chunk.");
					return true;
				}
				
				KMCChunk chunk = m.getChunk(p.getLocation().getChunk());
				
				if (!chunk.getOwner().equals(p.getUniqueId())) {
					p.sendMessage("�cYou don't own this chunk.");
					return true;
				}
				
				
				if (chunk.getName().equals(newName)) {
					p.sendMessage("�cThat chunk already has that name.");
					return true;
				}
				
				m.changeNameOfChunk(chunk, newName);
				p.sendMessage("�2This chunk has been renamed to �f" + newName + "�2.");
				return true;
			}
		}
		
		//rename with name
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("rename")) {
				
				if (!p.hasPermission("cl.rename")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				String oldName = args[1];
				String newName = args[2];
				
				if (!m.ownsChunk(oldName, p.getUniqueId())) {
					p.sendMessage("�cYou don't own a chunk with that name.");
					return true;
				}
				
				if (oldName.equals(newName)) {
					p.sendMessage("�cThat chunk already has that name.");
					return true;
				}
				
				KMCChunk c = m.getChunk(p.getUniqueId(), oldName);
				
				if (!c.getOwner().equals(p.getUniqueId())) {
					p.sendMessage("�cYou don't own this chunk.");
					return true;
				}
				
				m.changeNameOfChunk(c, newName);
				
				p.sendMessage("�2This chunk has been renamed to �f" + newName + "�2.");
				return true;
			}
		}
		
		//rename with name other player
		if (args.length == 4) {
			if (args[0].equalsIgnoreCase("rename")) {
				
				if (!p.hasPermission("cl.rename.others")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				String player = args[3];
				UUID uuid = Bukkit.getOfflinePlayer(player).getUniqueId();
				if (uuid == null) {
					p.sendMessage("�cThe player �f" + player + " �chas never played on this server before!");
					return true;
				}
				
				String oldName = args[1];
				String newName = args[2];
				
				if (!m.ownsChunk(oldName, uuid)) {
					p.sendMessage("�cThat player doesn't own a chunk with that name.");
					return true;
				}
				
				if (oldName.equals(newName)) {
					p.sendMessage("�cThat chunk already has that name.");
					return true;
				}
				
				KMCChunk c = m.getChunk(uuid, oldName);
				
				m.changeNameOfChunk(c, newName);
				
				p.sendMessage("�2This chunk has been renamed to �f" + newName + "�2.");
				return true;
			}
		}
		
		//teleport
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("teleport")) {
				
				if (!p.hasPermission("cl.teleport")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				String name = args[1];
				if (!m.exists(p.getUniqueId(), name)) {
					p.sendMessage("�cYou don't own a ChunkLoader/chunk with that name.");
					return true;
				}
				Location toTeleport;
				if (m.existsChunkLoader(p.getUniqueId(), name)) {
					toTeleport = m.getChunkLoader(p.getUniqueId(), name).getLocation();
					toTeleport = toTeleport.getWorld().getHighestBlockAt(toTeleport).getLocation();
					toTeleport.setY(toTeleport.getY() + 1);
				} else {
					Chunk c = m.getChunk(p.getUniqueId(), name).getChunk();
					int x = c.getX() * 16;
					int y = 50;
					int z = c.getZ() * 16;
					Location loc = new Location(c.getWorld(), x, y, z);
					toTeleport = loc.getWorld().getHighestBlockAt(loc).getLocation();
					toTeleport.setY(toTeleport.getY() + 1);
				}
				
				p.teleport(toTeleport);
				
				p.sendMessage("�2You have been teleported.");
				return true;
			}
		}
		
		//help
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("help")) {
				
				if (!p.hasPermission("cl.help")) {
					p.sendMessage("�cYou don't have permission.");
					return true;
				}
				
				p.sendMessage("/cl buy <name> �2- Buy the chunk you are currently in.");
				p.sendMessage("/cl chunkloader �2- Buy a ChunkLoader.");
				p.sendMessage("/cl remove �2- Remove the chunk you are currently in.");
				p.sendMessage("/cl remove <name> [player] �2- Remove the chunk with this name.");
				p.sendMessage("/cl list [player] �2- List the chunks and ChunkLoaders a player owns.");
				p.sendMessage("/cl rename [oldName] <newName> �2- Rename a chunk.");
				p.sendMessage("/cl teleport <name> �2- Teleport to this chunk/ChunkLoader.");
				p.sendMessage("/cl border �2- Show the borders of the chunk you are in.");
				
				return true;
			}
			
			//border
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("border")) {
					
					if (!p.hasPermission("cl.border")) {
						p.sendMessage("�cYou don't have permission.");
						return true;
					}
					
					new ParticlePlayer(5).playParticles(p, p.getLocation().getChunk());
					return true;
				}
			}
		}
		return true;
	}
}