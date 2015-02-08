package com.bp389.cranaz.ia;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.cranaz.Util;
import com.bp389.cranaz.YamlObj;

/**
 * Représente un spawner virtual faisant appraître des zombies à différentes
 * intervalles
 * 
 * @author BlackPhantom
 * 
 */
public final class VirtualSpawner {

	// public static Hashtable<EnhancedZombie, VirtualSpawner> ht = new
	// Hashtable<EnhancedZombie, VirtualSpawner>();
	public static ArrayList<VirtualSpawner> al = new ArrayList<VirtualSpawner>();
	public static boolean init = false;
	private final Location exactLoc;
	private int currentCount;
	private final int secondsToRespawn;
	private RecSpawn rs;
	private int count;
	private boolean isRun = true;
	private final JavaPlugin jp;
	private final Random r = new Random();

	public VirtualSpawner(final Location l, final JavaPlugin plugin) {
		this(l, 300, plugin);
	}

	public VirtualSpawner(final Location l, final int str, final JavaPlugin plugin) {
		this.exactLoc = l;
		this.secondsToRespawn = str;
		this.jp = plugin;
		VirtualSpawner.al.add(this);
		this.go();
	}

	/**
	 * 
	 * @param from
	 *            La position
	 * @param radius
	 *            Le rayon
	 * @return Le premier spawner dans le rayon donné autour de la position
	 */
	public static VirtualSpawner getNearbySpawner(final Location from, final int radius) {
		for(final VirtualSpawner vs : VirtualSpawner.al){
			if(from.distance(vs.getExactLocation()) <= Integer.valueOf(radius).doubleValue())
				return vs;
		}
		return null;
	}

	public String getCountString() {
		return String.valueOf(this.currentCount);
	}

	public int getCount() {
		return this.currentCount;
	}

	public boolean isRunning() {
		return this.isRun;
	}

	public void countPlus() {
		++this.currentCount;
		if(currentCount >= 25 && isRunning()){
			rs.st_reload();
		}
	}

	public Location getExactLocation() {
		return this.exactLoc;
	}

	protected File getFile() {
		return new File("plugins/CranaZ/database/spawners/" + String.valueOf(this.exactLoc.getBlockX()) + "_" + String.valueOf(this.exactLoc.getBlockY()) + "_"
				+ String.valueOf(this.exactLoc.getBlockZ()) + ".yml");
	}

	/**
	 * Démarre tous les spawners
	 * 
	 * @param w
	 *            Le monde
	 * @param jpl
	 *            Le plugin principal
	 */
	public static void startupAll(final World w, final JavaPlugin jpl) {
		if(new File("plugins/CranaZ/database/spawners/").listFiles().length == 0)
			return;
		for(final File f : new File("plugins/CranaZ/database/spawners/").listFiles()) {
			final String[] ss = f.getName().split("_", 3);
			@SuppressWarnings("unused")
			final VirtualSpawner vs = new VirtualSpawner(new Location(w, Double.valueOf(ss[0]).doubleValue(), Double.valueOf(ss[1]).doubleValue(), Double
					.valueOf(ss[2].substring(0, ss[2].indexOf("."))).doubleValue()), jpl);// WTF ? :)
		}
	}

	public void saveAll() {
		Util.saveToYaml(getFile(), new YamlObj("spawner.count", this.currentCount),
				new YamlObj("spawner.locX", this.exactLoc.getX()),
				new YamlObj("spawner.locY", this.exactLoc.getY()),
				new YamlObj("spawner.locZ", this.exactLoc.getZ()));
	}

	public void go() {
		setRunning(true);
		if(!this.getFile().exists()){
			Util.saveToYaml(getFile(), new YamlObj("spawner.count", this.currentCount),
					new YamlObj("spawner.locX", this.exactLoc.getX()),
					new YamlObj("spawner.locY", this.exactLoc.getY()),
					new YamlObj("spawner.locZ", this.exactLoc.getZ()),
					new YamlObj("spawner.countdown", -1));
			this.currentCount = 0;
		}
		else
			this.currentCount = (int)Util.getFromYaml(getFile(), "spawner.count", 0);
		new RecSpawn(this).runTaskTimer(this.jp, 1L, Integer.valueOf(this.secondsToRespawn * 20).longValue());
	}

	public void setRunning(final boolean b) {
		this.isRun = b;
	}

	/*
	 * 
	 */
	class Reload extends BukkitRunnable {

		private final VirtualSpawner vs;

		public Reload(final VirtualSpawner vs) {
			this.vs = vs;
		}

		@Override
		public void run() {
			VirtualSpawner.this.currentCount = 0;
			VirtualSpawner.this.count = 0;
			setRunning(true);
			new RecSpawn(this.vs).runTaskTimer(VirtualSpawner.this.jp, 1L, Integer.valueOf(VirtualSpawner.this.secondsToRespawn * 20).longValue());
		}
	}

	public class RecSpawn extends BukkitRunnable {

		private final VirtualSpawner vs;

		public RecSpawn(final VirtualSpawner vs) {
			this.vs = vs;
			rs = this;
		}

		public void st_reload(){
			setRunning(false);
			new Reload(this.vs).runTaskLater(VirtualSpawner.this.jp, 3600L * 20L);
			this.cancel();
		}

		@Override
		public void run() {
			++VirtualSpawner.this.count;
			if(VirtualSpawner.this.count >= 25 || !VirtualSpawner.this.isRun || VirtualSpawner.this.currentCount >= 25) {
				st_reload();
				return;
			}
			// Rayon de spawn aleatoire autour du spawner ~6 blocs x-z
			final double d0 = Integer.valueOf(VirtualSpawner.this.exactLoc.getBlockX() + VirtualSpawner.this.r.nextInt(6)).doubleValue(), d1 = Integer.valueOf(
					VirtualSpawner.this.exactLoc.getBlockZ() + VirtualSpawner.this.r.nextInt(6)).doubleValue();
			final Location loc = new Location(VirtualSpawner.this.exactLoc.getWorld(), d0, VirtualSpawner.this.exactLoc.getY(), d1);
			ZIA.spawnZombie(loc);
		}
	}
}
