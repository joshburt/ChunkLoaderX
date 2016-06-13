package de.ersterstreber.resources.chunkloaderx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

public class Manager {
	
	private static Manager m = new Manager();
	
	public static Manager getManager() {
		return m;
	}
	
	/**
	 * @return all chunks bought by command
	 */
	public List<KMCChunk> getBoughtChunks() {
		List<String> list = Main.getInstance().getChunkConfig().getStringList("chunks");
		List<KMCChunk> chunks = new ArrayList<KMCChunk>();
		for (String s : list) {
			String[] split = s.split(";");
			try {
				chunks.add(new KMCChunk(split[4], UUID.fromString(split[0]), new Location(Bukkit.getWorld(split[1]), Integer.parseInt(split[2]), 128, Integer.parseInt(split[3])).getChunk()));
			} catch (NumberFormatException e) {
				Main.getInstance().getLogger().severe("Error in chunks.yml [ERROR #101]");
				Main.getInstance().getLogger().severe("Disabling plugin.");
				Bukkit.getPluginManager().disablePlugin(Main.getInstance());
				return null;
			}
		}
		
		return chunks;
	}
	
	/**
	 * @return all currently placed ChunkLoaders
	 */
	public List<ChunkLoader> getChunkLoaders() {
		List<ChunkLoader> list = new ArrayList<ChunkLoader>();
		for (String s : Main.getInstance().getChunkLoaderConfig().getStringList("chunkloaders")) {
			String[] split = s.split(";");
			list.add(new ChunkLoader(split[5], 
					UUID.fromString(split[0]), 
					new Location(Bukkit.getWorld(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]))));
		}
		
		return list;
	}
	
	/**
	 * @param uuid
	 * @return all chunks of player
	 */
	public List<KMCChunk> getBoughtChunks(UUID uuid) {
		List<KMCChunk> chunks = new ArrayList<KMCChunk>();
		for (KMCChunk c : getBoughtChunks()) {
			if (c.getOwner().equals(uuid)) {
				chunks.add(c);
			}
		}
		
		return chunks;
	}
	
	/**
	 * @param uuid
	 * @return all ChunkLoaders a player has
	 */
	public List<ChunkLoader> getChunkLoaders(UUID uuid) {
		List<ChunkLoader> list = new ArrayList<ChunkLoader>();
		for (ChunkLoader c : getChunkLoaders()) {
			if (c.getOwner().equals(uuid)) {
				list.add(c);
			}
		}
		
		return list;
	}
	
	/**
	 * @param c
	 * @return chunk converted to KMCChunk
	 */
	public KMCChunk getChunk(Chunk c) {
		for (KMCChunk chunk : getBoughtChunks()) {
			if (chunk.getChunk().equals(c)) return chunk;
		}
		return null;
	}
	
	/**
	 * @param uuid
	 * @param name
	 * @return Chunk by name
	 */
	public KMCChunk getChunk(UUID uuid, String name) {
		for (KMCChunk chunk : getBoughtChunks(uuid)) {
			if (chunk.getName().equalsIgnoreCase(name)) return chunk;
		}
		return null;
	}
	/**
	 * @param loc
	 * @return ChunkLoader by location
	 */
	public ChunkLoader getChunkLoader(Location loc) {
		for (ChunkLoader cl : getChunkLoaders()) {
			if (cl.getLocation().equals(loc)) return cl;
		}
		return null;
	}
	
	/**
	 * @param chunk
	 * @return ChunkLoader by chunk
	 */
	public ChunkLoader getChunkLoader(Chunk chunk) {
		for (ChunkLoader cl : getChunkLoaders()) {
			if (cl.getLocation().getChunk().equals(chunk)) return cl;
		}
		return null;
	}
	
	/**
	 * @param uuid
	 * @param name
	 * @return ChunkLoader by name
	 */
	public ChunkLoader getChunkLoader(UUID uuid, String name) {
		if (!existsChunkLoader(uuid, name)) return null;
		for (ChunkLoader cl : getChunkLoaders(uuid)) {
			if (cl.getName().equalsIgnoreCase(name)) {
				return cl;
			}
		}
		return null;
	}
	
	/**
	 * @return all chunks that are currently being loaded
	 */
	private List<Chunk> getAllLoadedChunks() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		for (KMCChunk c : getBoughtChunks()) {
			chunks.add(c.getChunk());
		}
		for (ChunkLoader c : getChunkLoaders()) {
			chunks.add(c.getLocation().getChunk());
		}
		
		return chunks;
	}
	
	/**
	 * @return all chunks that are currently being loaded
	 */
	public List<Chunk> getAllLoadedChunks(UUID uuid) {
		List<Chunk> chunks = new ArrayList<Chunk>();
		for (KMCChunk c : getBoughtChunks()) {
			if (c.getOwner().equals(uuid)) {
				chunks.add(c.getChunk());
			}
		}
		for (ChunkLoader c : getChunkLoaders()) {
			if (c.getOwner().equals(uuid)) {
				chunks.add(c.getLocation().getChunk());
			}
		}
		
		return chunks;
	}
	
	/**
	 * @param chunk
	 * @return true if chunk is being loaded
	 */
	public boolean isLoaded(Chunk chunk) {
		for (Chunk c : getAllLoadedChunks()) {
			if (c.equals(chunk)) return true;
		}
		return false;
	}
	
	/**
	 * @param uuid
	 * @param name
	 * @return true if there is anything with that name
	 */
	public boolean exists(UUID uuid, String name) {
		for (ChunkLoader cl : getChunkLoaders(uuid)) {
			if (cl.getName().equalsIgnoreCase(name)) return true;
		}
		for (KMCChunk c : getBoughtChunks(uuid)) {
			if (c.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}
	
	/**
	 * @param cl
	 * @return true if ChunkLoader exists
	 */
	public boolean existsChunkLoader(ChunkLoader cl) {
		for (ChunkLoader chunkLoader : getChunkLoaders()) {
			if (chunkLoader.getName().equalsIgnoreCase(cl.getName()) && chunkLoader.getOwner().equals(cl.getOwner()) && chunkLoader.getLocation().equals(cl.getLocation())) return true;
		}
		return false;
	}
	
	/**
	 * @param loc
	 * @return true if a ChunkLoader exists at this Location
	 */
	public boolean existsChunkLoader(Location loc) {
		for (ChunkLoader cl : getChunkLoaders()) {
			if (cl.getLocation().equals(loc)) return true;
		}
		return false;
	}
	
	/**
	 * @param c
	 * @return true if a ChunkLoader exists in this chunk
	 */
	public boolean existsChunkLoader(Chunk c) {
		for (ChunkLoader cl : getChunkLoaders()) {
			if (cl.getLocation().getChunk().equals(c)) return true;
		}
		return false;
	}
	
	/**
	 * @param uuid
	 * @param name
	 * @return true if player owns a ChunkLoader with that name
	 */
	public boolean existsChunkLoader(UUID uuid, String name) {
		for (ChunkLoader cl : getChunkLoaders(uuid)) {
			if (cl.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}
	
	/**
	 * @param uuid
	 * @param chunk
	 * @return true if player bought chunk
	 */
	public boolean ownsChunk(UUID uuid, Chunk chunk) {
		if (!isLoaded(chunk)) return false;
		for (KMCChunk c : getBoughtChunks(uuid)) {
			if (c.getChunk().equals(chunk)) return true;
		}
		return false;
	}
	
	/**
	 * @param name
	 * @param uuid
	 * @return true if player has a chunk with that name
	 */
	public boolean ownsChunk(String name, UUID uuid) {
		for (KMCChunk c : getBoughtChunks(uuid)) {
			if (c.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}
	
	/**
	 * @param chunk
	 * Buy a chunk
	 */
	public void addChunk(KMCChunk chunk) {
		if (isLoaded(chunk.getChunk())) return;
		if (exists(chunk.getOwner(), chunk.getName())) return;
		
		List<String> list = Main.getInstance().getChunkConfig().getStringList("chunks");
		String world = chunk.getChunk().getWorld().getName();
		int x = chunk.getChunk().getX() * 16;
		int z = chunk.getChunk().getZ() * 16;
		list.add(chunk.getOwner().toString() + ";" + world + ";" + x + ";" + z + ";" + chunk.getName());
		
		Main.getInstance().getChunkConfig().set("chunks", list);
		Main.getInstance().saveChunkConfig();
	}
	
	/**
	 * @param cl
	 * create new ChunkLoader
	 */
	public void addChunkLoader(ChunkLoader cl) {
		if (existsChunkLoader(cl)) return;
		if (exists(cl.getOwner(), cl.getName())) return;
		List<String> list = Main.getInstance().getChunkLoaderConfig().getStringList("chunkloaders");
		list.add(cl.getOwner().toString() + ";" 
				+ cl.getLocation().getWorld().getName() + ";" 
				+ cl.getLocation().getBlockX() + ";" 
				+ cl.getLocation().getBlockY() + ";" 
				+ cl.getLocation().getBlockZ() + ";"
				+ cl.getName());
		Main.getInstance().getChunkLoaderConfig().set("chunkloaders", list);
		Main.getInstance().saveChunkLoaderConfig();
	}
	
	/**
	 * @param chunk
	 * Remove a chunk
	 */
	public void removeChunk(KMCChunk chunk) {
		List<String> list = Main.getInstance().getChunkConfig().getStringList("chunks");
		String world = chunk.getChunk().getWorld().getName();
		int x = chunk.getChunk().getX() * 16;
		int z = chunk.getChunk().getZ() * 16;
		list.remove(chunk.getOwner().toString() + ";" + world + ";" + x + ";" + z + ";" + chunk.getName());
		
		Main.getInstance().getChunkConfig().set("chunks", list);
		Main.getInstance().saveChunkConfig();
	}
	
	/**
	 * @param cl
	 * remove ChunkLoader
	 */
	public void removeChunkLoader(ChunkLoader cl) {
		if (!existsChunkLoader(cl)) return;
		List<String> list = Main.getInstance().getChunkLoaderConfig().getStringList("chunkloaders");
		list.remove(cl.getOwner().toString() + ";" 
				+ cl.getLocation().getWorld().getName() + ";" 
				+ cl.getLocation().getBlockX() + ";" 
				+ cl.getLocation().getBlockY() + ";" 
				+ cl.getLocation().getBlockZ() + ";"
				+ cl.getName());
		Main.getInstance().getChunkLoaderConfig().set("chunkloaders", list);
		Main.getInstance().saveChunkLoaderConfig();
	}
	
	/**
	 * @param c
	 * @param newName
	 * set new name of a chunk
	 */
	public void changeNameOfChunk(KMCChunk c, String newName) {
		removeChunk(c);
		
		c.setName(newName);
		
		addChunk(c);
	}
	
	/**
	 * @param cl
	 * @param newName
	 * set new name of a ChunkLoader
	 */
	public void changeNameOfChunkLoader(ChunkLoader cl, String newName) {
		removeChunkLoader(cl);
		
		cl.setName(newName);
		
		addChunkLoader(cl);
	}
	
	/**
	 * @param uuid
	 * @return random name for chunk
	 */
	public String getRandomName(UUID uuid) {
		List<String> names = new ArrayList<String>();
		for (ChunkLoader cl : getChunkLoaders(uuid)) {
			names.add(cl.getName());
		}
		for (KMCChunk c : getBoughtChunks(uuid)) {
			names.add(c.getName());
		}
		
		for (int i = 0 ; i < 5000 ; i++) {
			if (!names.contains("chunk-" + i)) {
				return "chunk-" + i;
			}
		}
		
		return "chunk-" + new Random().nextInt(500000);
	}
	
}
