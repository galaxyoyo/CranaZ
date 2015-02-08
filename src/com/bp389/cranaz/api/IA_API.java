package com.bp389.cranaz.api;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.ia.VirtualSpawner;
import com.bp389.cranaz.ia.ZIA;


public final class IA_API extends CranaZAPI {
	public IA_API(JavaPlugin plugin) {
	    super(plugin);
    }
	/**
	 * 
	 * @param location Le point de spawn du zombie
	 * @return Le zombie, sous sa forme encapsulee
	 */
	public EZombie_API spawnAZombie(final Location location){
		return new EZombie_API(ZIA.spawnZombie(location));
	}
	/**
	 * 
	 * @param position La position de recherche
	 * @return Une instance du premier spawner dans les 80 blocs, ou null si aucun
	 * @see #getSpawnerNearby(Location, int)
	 */
	public VirtualSpawner getSpawnerNearby(final Location position){
		return getSpawnerNearby(position, 80);
	}
	/**
	 * 
	 * @param position La position de recherche
	 * @param radius La distance de recherche
	 * @return Le premier spawner dans le rayon de recherche, ou null si aucun
	 */
	public VirtualSpawner getSpawnerNearby(final Location position, int radius){
		return VirtualSpawner.getNearbySpawner(position, radius);
	}
}
