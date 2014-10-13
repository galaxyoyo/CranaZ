package com.bp389.cranaz.ia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Représente un spawner virtual faisant appraître des zombies à différentes intervalles
 * @author BlackPhantom
 *
 */
public final class VirtualSpawner {
	//public static Hashtable<EnhancedZombie, VirtualSpawner> ht = new Hashtable<EnhancedZombie, VirtualSpawner>();
	public static ArrayList<VirtualSpawner> al = new ArrayList<VirtualSpawner>();
	public static boolean init = false;
	private Location exactLoc;
	@SuppressWarnings("unused")
    private int currentCount, taskID, secondsToRespawn, count;
	private boolean isRun = true, reloading = false;
	private JavaPlugin jp;
	private Random r = new Random();
	public VirtualSpawner(Location l, JavaPlugin plugin){
		this(l, 300, plugin);
	}
	public VirtualSpawner(Location l, int str, JavaPlugin plugin){
		this.exactLoc = l;
		this.secondsToRespawn = str;
		this.jp = plugin;
		al.add(this);
		go();
	}
	/**
	 * 
	 * @param from La position
	 * @param radius Le rayon
	 * @return Le premier spawner dans le rayon donné autour de la position
	 */
	public static VirtualSpawner getNearbySpawner(Location from, int radius){
		for(VirtualSpawner vs : al){
			if(Double.valueOf(vs.getExactLocation().distance(from)).intValue() <= radius){
				return vs;
			}
		}
		return null;
	}
	public String getCountString(){
		return String.valueOf(currentCount);
	}
	public int getCount(){
		return currentCount;
	}
	public boolean isRunning(){
		return !getCountString().equalsIgnoreCase("25");
	}
	public boolean isReloading(){
		return this.reloading;
	}
	public void countPlus(){
		++currentCount;
	}
	public Location getExactLocation(){
		return this.exactLoc;
	}
	protected File getFile(){
		return new File("plugins/CranaZ/spawners/" + String.valueOf(exactLoc.getBlockX()) + "_" + String.valueOf(exactLoc.getBlockY()) + "_" + String.valueOf(exactLoc.getBlockZ()) + ".yml");
	}
	/**
	 * Démarre tous les spawners
	 * @param w Le monde
	 * @param jpl Le plugin principal
	 */
	public static void startupAll(World w, JavaPlugin jpl){
		if(new File("plugins/CranaZ/spawners/").listFiles().length == 0)
			return;
		for(File f : new File("plugins/CranaZ/spawners/").listFiles()){
			String[] ss = f.getName().split("_", 3);
			VirtualSpawner vs = new VirtualSpawner(new Location(w, Double.valueOf(ss[0]).doubleValue(), Double.valueOf(ss[1]).doubleValue(), Double.valueOf(ss[2].substring(0, ss[2].indexOf("."))).doubleValue()), jpl);//WTF ? :)
			vs.setRunning(true);
		}
	}
	public void saveAll(){
		FileConfiguration fc = jp.getConfig();
        fc.set("spawner.count", currentCount);
        fc.set("spawner.locX", exactLoc.getX());
        fc.set("spawner.locY", exactLoc.getY());
        fc.set("spawner.locZ", exactLoc.getZ());
        try {
	        fc.save(getFile());
        } catch (IOException e) {}
	}
	public void go(){
		FileConfiguration fc = jp.getConfig();
		if(!getFile().exists()){
			try {
	            getFile().createNewFile();
	            fc.set("spawner.count", currentCount);
	            fc.set("spawner.locX", exactLoc.getX());
	            fc.set("spawner.locY", exactLoc.getY());
	            fc.set("spawner.locZ", exactLoc.getZ());
	            fc.set("spawner.countdown", -1);
	            fc.save(getFile());
            } catch (IOException e) {}finally{currentCount = 0;}
		}
		else{
			try {
	            fc.load(getFile());
	            currentCount = fc.getInt("spawner.count");
            } catch (IOException | InvalidConfigurationException e) {currentCount = 0;}
		}
		new RecSpawn(this).runTaskTimer(jp, 1L, Integer.valueOf(secondsToRespawn * 20).longValue());
	}
	public void setRunning(boolean b){
		this.isRun = b;
	}
	/*
	 * 
	 */
	class Reload extends BukkitRunnable
	{
		private VirtualSpawner vs;
		public Reload(VirtualSpawner vs){this.vs = vs;}
		@Override
        public void run() {
			currentCount = 0;
			count = 0;
			reloading = false;
			new RecSpawn(this.vs).runTaskTimer(jp, 1L, Integer.valueOf(secondsToRespawn * 20).longValue());
        }
	}
	public class RecSpawn extends BukkitRunnable
	{
		private VirtualSpawner vs;
		public RecSpawn(VirtualSpawner vs){
			this.vs = vs;
		}
		@Override
        public void run() {
			++count;
			if(count >= 25 || !isRun || currentCount >= 25){
				reloading = true;
				new Reload(vs).runTaskLater(jp, 7200L * 20L);
				cancel();
			    return;
			}
			//Rayon de spawn aleatoire autour du spawner ~6 blocs x-z
			double d0 = Integer.valueOf(exactLoc.getBlockX() + r.nextInt(6)).doubleValue(), d1 = Integer.valueOf(exactLoc.getBlockZ() + r.nextInt(6)).doubleValue();
			Location loc = new Location(exactLoc.getWorld(), d0, exactLoc.getY(), d1);
			ZIA.spawnZombie(loc);
        }
	}
}
