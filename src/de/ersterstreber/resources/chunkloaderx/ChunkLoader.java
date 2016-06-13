package de.ersterstreber.resources.chunkloaderx;

import java.util.UUID;
import org.bukkit.Location;

public class ChunkLoader {

	private String name;
	private UUID owner;
	private Location loc;
	
	public ChunkLoader(String name, UUID owner, Location loc) {
		this.name = name;
		this.owner = owner;
		this.loc = loc;
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
