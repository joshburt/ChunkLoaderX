package de.ersterstreber.resources.chunkloaderx;

import java.util.UUID;

import org.bukkit.Chunk;

public class KMCChunk {

	private String name;
	private UUID owner;
	private Chunk chunk;
	
	public KMCChunk(String name, UUID owner, Chunk chunk) {
		this.name = name;
		this.owner = owner;
		this.chunk = chunk;
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	public Chunk getChunk() {
		return chunk;
	}
	
	public void setName(String s) {
		name = s;
	}
	
}
