package com.bp389.cranaz.loots;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityItem;

import com.shampaggon.crackshot.CSUtility;

public final class LootRefactor {

	public LootRefactor() {}

	public static final CSUtility csu = new CSUtility();
	private static JavaPlugin plugin;
	private static World main;
	public static boolean spawnsOk = false;
	public static final Random random = new Random();
	public static volatile Hashtable<Location, ThreadSpawner> loots = new Hashtable<Location, ThreadSpawner>();
	public static final int PACK = 0, LOOT = 1;
	public static final String packLoc = "plugins/CranaZ/PacksAndLoots/PackPoints/", lootLoc = "plugins/CranaZ/PacksAndLoots/LootPoints/";

	public static void init(final World world, final JavaPlugin jp) {
		LootRefactor.main = world;
		LootRefactor.plugin = jp;
	}

	/**
	 * 
	 * @param Le
	 *            nom du pack insensible � la casse
	 * @return Un objet constant repr�sentant le pack
	 */
	public EnumPacks parsePack(final String packName) {
		if(packName.equalsIgnoreCase("chambre"))
			return EnumPacks.CHAMBRE;
		else if(packName.equalsIgnoreCase("ferme"))
			return EnumPacks.FERME;
		else if(packName.equalsIgnoreCase("cuisine"))
			return EnumPacks.CUISINE;
		else if(packName.equalsIgnoreCase("superette"))
			return EnumPacks.SUPERETTE;
		else if(packName.equalsIgnoreCase("militaire"))
			return EnumPacks.MILITAIRE;
		else if(packName.equalsIgnoreCase("hopital"))
			return EnumPacks.HOPITAL;
		else
			return EnumPacks.NULL;
	}

	/**
	 * D�marre le spawn de tous les coffres et loots Appel� la toute premi�re
	 * fois qu'un joueur rejoint le serveur, apr�s un reboot
	 */
	public void startSpawns() {
		// Un peu compliqu� :-)
		// Il suffit d'analyser avec concentration
		final File[] packs = new File(LootRefactor.packLoc).listFiles(), loots = new File(LootRefactor.lootLoc).listFiles();
		if(packs.length == 0 && loots.length == 0)
			return;
		final ArrayList<String[]> args = new ArrayList<String[]>(), args2 = new ArrayList<String[]>();
		final ArrayList<double[]> locs = new ArrayList<double[]>(), locs2 = new ArrayList<double[]>();
		for(final File pack2 : packs)
			args.add(pack2.getName().split("_"));
		for(final File loot2 : loots)
			args2.add(loot2.getName().split("_"));
		for(int i = 0; i < args.size(); i++) {
			final double[] tmp = new double[3];
			tmp[0] = Double.valueOf(args.get(i)[1]).doubleValue();
			tmp[1] = Double.valueOf(args.get(i)[2]).doubleValue();
			tmp[2] = Double.valueOf(args.get(i)[3]).doubleValue();
			locs.add(tmp);
		}
		for(int i = 0; i < args2.size(); i++) {
			final double[] tmp = new double[3];
			tmp[0] = Double.valueOf(args2.get(i)[1]).doubleValue();
			tmp[1] = Double.valueOf(args2.get(i)[2]).doubleValue();
			tmp[2] = Double.valueOf(args2.get(i)[3]).doubleValue();
			locs2.add(tmp);
		}
		for(int i = 0; i < args.size(); i++) {
			this.startSpawn(this.parsePack(args.get(i)[0]), LootRefactor.PACK, locs.get(i)[0], locs.get(i)[1], locs.get(i)[2]);
			this.spawnPackChest(new Location(LootRefactor.main, locs.get(i)[0], locs.get(i)[1], locs.get(i)[2]), this.parsePack(args.get(i)[0]));
		}
		if(loots.length == 0)
			return;
		if(args2.size() > 0 && locs2.size() > 0)
			for(int i = 0; i < args2.size(); i++)
				this.startSpawn(this.parsePack(args2.get(i)[0]), LootRefactor.LOOT, locs2.get(i)[0], locs2.get(i)[1], locs2.get(i)[2]);
	}

	/*
	 * public void stopAllThreads() { for(int i = 0;i < threads.size();i++) {
	 * threads.get(i).kill(); threads.remove(i); } }
	 */
	/**
	 * D�marre le spawn d'un loot/pack
	 * 
	 * @param pack
	 *            Le pack
	 * @param packType
	 *            Le type de pack, {@link #LOOT} ou {@link #PACK}
	 * @param x
	 *            La position X
	 * @param y
	 *            Y
	 * @param z
	 *            Z
	 */
	@SuppressWarnings("static-access")
	public void startSpawn(final EnumPacks pack, final int packType, final double x, final double y, final double z) {
		if(packType == LootRefactor.PACK || packType == LootRefactor.LOOT) {
			final ThreadSpawner ts = new ThreadSpawner(pack, packType, new Location(LootRefactor.main, x, y, z));
			ts.runTaskTimer(this.plugin, 1, pack.delay() * 20);
			this.loots.put(new Location(LootRefactor.main, x, y, z), ts);
		}
	}

	/**
	 * Forme le chemin d'acc�s sur HDD d'un pack/loot
	 * 
	 * @param packType
	 *            Le type de pack
	 * @param loc
	 *            La position
	 * @param pack
	 *            Le pack
	 * @return Le chemin d'acc�s
	 */
	public String formPath(final int packType, final Location loc, final EnumPacks pack) {
		String tmp = "";
		if(packType == LootRefactor.PACK) {
			tmp += LootRefactor.packLoc + pack.toString() + "_";
			tmp += String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_"
			        + String.valueOf(Double.valueOf(loc.getZ()).intValue());
		} else if(packType == LootRefactor.LOOT) {
			tmp += LootRefactor.lootLoc + pack.toString() + "_";
			tmp += String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_"
			        + String.valueOf(Double.valueOf(loc.getZ()).intValue());
		}
		return tmp;
	}

	/**
	 * 
	 * @param packType
	 *            Le type de pack
	 * @param loc
	 *            Sa position
	 * @param pack
	 *            Le pack
	 * @return True si un pack/loot existe � cette position, false sinon
	 */
	public boolean pointExist(final int packType, final Location loc, final EnumPacks pack) {
		final String tmp = this.formPath(packType, loc, pack);
		return new File(tmp).exists();
	}

	/**
	 * 
	 * @param pack
	 *            Le pack
	 * @param maxSize
	 *            La taille maximale du pack
	 * @return Un tableau d'ItemStack contenant al�atoirement des objets du pack
	 * @see #randomSort(EnumPacks)
	 */
	public ItemStack[] randomToTab(final EnumPacks pack, final int maxSize) {
		final ArrayList<ItemStack> tmp = this.randomSort(pack);
		final int i2 = tmp.size() > maxSize ? maxSize : tmp.size();
		final ItemStack[] tmp2 = new ItemStack[i2];
		for(int i = 0; i < i2; i++)
			tmp2[i] = tmp.get(i);
		return tmp2;
	}

	/**
	 * 
	 * @param pack
	 *            Le pack
	 * @return Une liste d'ItemStack contenant al�atoirement des objets du pack
	 *         -> taille ind�finie
	 * @see #randomToTab(EnumPacks, int)
	 */
	@SuppressWarnings("static-access")
	public ArrayList<ItemStack> randomSort(final EnumPacks pack) {
		if(pack != EnumPacks.NULL) {
			final ArrayList<ItemStack> tmp = new ArrayList<ItemStack>();
			final ArrayList<Packs> tmp2 = pack.items();
			for(int i = 0; i < tmp2.size(); i++) {
				if(this.random.nextInt(tmp2.get(i).rare()) < 1)
					tmp.add(tmp2.get(i).item());
				if(this.random.nextInt(1) == 0)
					tmp.add(new ItemStack(Material.AIR));
			}
			return tmp;
		}
		return null;
	}

	/**
	 * 
	 * @param loc
	 *            La position
	 * @param pack
	 *            Le pack
	 * @return True si c'est un bloc relatif � un loot
	 */
	public boolean isLootBlock(final Location loc, final EnumPacks pack) {
		final String temp = LootRefactor.lootLoc + pack.toString() + "_" + String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_"
		        + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getZ()).intValue());
		return new File(temp).exists();
	}

	//
	public Object[] toTab(final ArrayList<? extends Object> from) {
		final Object[] tmp = new Object[from.size()];
		for(int i = 0; i < from.size(); i++)
			tmp[i] = from.get(i);
		return tmp;
	}

	/**
	 * Fait spawner un coffre contenant un inventaire al�atoire du pack
	 * 
	 * @param loc
	 *            La position
	 * @param pack
	 *            Le pack
	 * @return True si succ�s
	 */
	public boolean spawnPackChest(final Location loc, final EnumPacks pack) {
		final World temp = loc.getWorld();
		final Location loc2 = loc;
		boolean result = false;
		if(pack != EnumPacks.NULL)
			if(temp.getBlockAt(loc).getType() == Material.CHEST) {
				final Chest chest = (Chest) temp.getBlockAt(loc2).getState();
				chest.getBlockInventory().setContents(this.randomToTab(pack, 27));
				result = true;
			} else {
				final Block block = temp.getBlockAt(loc2);
				block.setType(Material.CHEST);
				if(block.getType() == Material.CHEST) {
					final Chest chest = (Chest) block.getState();
					chest.getBlockInventory().setContents(this.randomToTab(pack, 27));
				}
				result = true;
			}
		return result;
	}

	/**
	 * 
	 * @param loc
	 *            La position
	 * @return Le pack relatif au loot � la position donn�e
	 * @see #getPackAt(Location, int)
	 */
	public EnumPacks getLootAt(final Location loc) {
		final String path = LootRefactor.lootLoc, locs = String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_"
		        + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getZ()).intValue());
		final File[] fs = new File(path).listFiles();
		String[] temp = new String[] { "NULL" };
		for(final File element : fs)
			if(element.getName().contains(locs)) {
				temp = element.getName().split("_");
				break;
			}
		return this.parsePack(temp[0]);
	}

	/**
	 * 
	 * @param loc
	 *            La position
	 * @param packType
	 *            Le type de pack
	 * @return Le pack � la position donn�e
	 */
	public EnumPacks getPackAt(final Location loc, final int packType) {
		if(packType == LootRefactor.PACK) {
			final String path = LootRefactor.packLoc, locs = String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_"
			        + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getZ()).intValue());
			final File[] fs = new File(path).listFiles();
			String[] temp = new String[] { "NULL" };
			for(final File element : fs)
				if(element.getName().contains(locs)) {
					temp = element.getName().split("_");
					break;
				}
			return this.parsePack(temp[0]);
		} else if(packType == LootRefactor.LOOT) {
			final String path = LootRefactor.lootLoc, locs = String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_"
			        + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getZ()).intValue());
			final File[] fs = new File(path).listFiles();
			String[] temp = new String[] { "NULL" };
			for(final File element : fs)
				if(element.getName().contains(locs)) {
					temp = element.getName().split("_");
					break;
				}
			return this.parsePack(temp[0]);
		}
		return EnumPacks.NULL;
	}

	/**
	 * Casse un coffre sans en disperser le contenu
	 * 
	 * @param loc
	 *            La position
	 */
	public void breakChest(final Location loc) {
		final Block block = loc.getWorld().getBlockAt(loc);
		if(block.getType() == Material.CHEST) {
			final Chest chest = (Chest) block.getState();
			chest.getBlockInventory().clear();
			chest.getBlock().breakNaturally(null);
		}
	}

	//
	public void delLoot(final Location loc, final EntityItem i) {
		i.die();
	}

	/**
	 * Supprime un point de spawn
	 * 
	 * @param packType
	 *            Le type
	 * @param loc
	 *            La position
	 * @return True si succ�s
	 */
	@SuppressWarnings("static-access")
	public boolean deleteSpawnPoint(final int packType, final Location loc) {
		if(packType == LootRefactor.PACK) {
			final EnumPacks temp = this.getPackAt(loc, LootRefactor.PACK);
			if(temp.toString().equalsIgnoreCase("NULL"))
				return false;
			final String thePath = this.formPath(packType, loc, temp);
			this.breakChest(loc);
			return new File(thePath).delete();
		} else if(packType == LootRefactor.LOOT) {
			final EnumPacks temp = this.getPackAt(loc, LootRefactor.LOOT);
			if(temp.toString().equalsIgnoreCase("NULL"))
				return false;
			final String thePath = this.formPath(packType, loc, temp);
			this.delLoot(loc, this.loots.get(loc).getItemOf());
			return new File(thePath).delete();
		}
		return false;
	}

	/**
	 * L�che un UnpickableItem (objet non ramassable et non stackable) � une
	 * position
	 * 
	 * @param loc
	 *            La position
	 * @param item
	 *            L'objet <<ic�ne>>
	 * @return L'objet l�ch� pour d'�ventuelles modifications
	 */
	public EntityItem dropUItem(final Location loc, final ItemStack item) {
		final EntityItem ei = new EntityItem(((CraftWorld) loc.getWorld()).getHandle(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5,
		        CraftItemStack.asNMSCopy(item)) {

			@Override
			public void d(final EntityHuman entityhuman) {}

			@Override
			public void s_() {}
			
			@Override
			public boolean damageEntity(DamageSource damagesource, float f) {
				return false;
			}
		};
		ei.world.addEntity(ei, SpawnReason.CUSTOM);
		return ei;
	}

	/**
	 * 
	 * @param pack
	 *            Le pack
	 * @return Un inventaire pr�t � l'usage contenant al�atoirement des objets
	 *         du loot
	 */
	public Inventory randomInvLoot(final EnumPacks pack) {
		final Inventory tmp = Bukkit.createInventory(null, InventoryType.CHEST, "LOOT");
		tmp.setContents(this.randomToTab(pack, 27));
		return tmp;
	}

	/**
	 * 
	 * @param c
	 *            Le chunk
	 * @return Une liste contenant les �ventuels processus de respawn relatifs
	 *         aux loots du chunk
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public List<ThreadSpawner> hasLoots(final Chunk c) {
		final ArrayList<ThreadSpawner> al = new ArrayList<ThreadSpawner>();
		final Hashtable<Location, ThreadSpawner> loots = (Hashtable<Location, ThreadSpawner>) this.loots.clone();
		boolean b = false;
		for(final Entry<Location, ThreadSpawner> e : loots.entrySet())
			if(c.equals(e.getKey().getChunk()) && e.getValue().getPackType() == LootRefactor.LOOT) {
				b = true;
				al.add(e.getValue());
			}
		return b ? al : null;
	}

	//
	public boolean defineSpawnPoint(final int packType, final Location loc, final EnumPacks pack) {
		final String tmp = this.formPath(packType, loc, pack);
		try {
			new File(tmp).createNewFile();
			if(packType == LootRefactor.PACK) {
				this.spawnPackChest(loc, pack);
				this.startSpawn(pack, packType, loc.getX(), loc.getY(), loc.getZ());
			} else if(packType == LootRefactor.LOOT)
				this.startSpawn(pack, packType, loc.getX(), loc.getY(), loc.getZ());

		} catch(final IOException e) {
			return false;
		}
		return true;
	}

	public EntityItem spawnPackLoot(final Location loc, final ItemStack pack) {
		if(pack != null && pack.getType() != Material.AIR)
			return this.dropUItem(loc, pack);
		return null;
	}

	public final ItemStack randomIcon(final Inventory loot) {
		ItemStack is = new ItemStack(Material.values()[LootRefactor.random.nextInt(Material.values().length)]);
		for(int i = 0; i < loot.getContents().length; i++)
			if(loot.getContents()[i] != null && loot.getContents()[i].getType() != Material.AIR) {
				is = loot.getContents()[i];
				break;
			}
		return is;
	}

	public class ThreadSpawner extends BukkitRunnable {

		private final EnumPacks pack;
		private final int packType;
		private final Location loc;
		private boolean running = true;
		private Inventory inv = null;
		private EntityItem i = null;
		private final Chunk c;

		public ThreadSpawner(final EnumPacks pack, final int packType, final Location loc) {
			this.pack = pack;
			this.packType = packType;
			this.loc = loc;
			this.c = loc.getChunk();
		}

		@Override
		public void run() {
			if(!LootRefactor.this.pointExist(this.packType, this.loc, this.pack) || this.pack == EnumPacks.NULL || !this.running) {
				if(this.i != null)
					this.i.die();
				LootRefactor.loots.remove(this.loc);
				this.cancel();
				return;
			}
			if(this.packType == LootRefactor.LOOT) {
				if(this.i != null)
					this.i.die();
				this.inv = LootRefactor.this.randomInvLoot(this.pack);
				this.i = LootRefactor.this.spawnPackLoot(this.loc, LootRefactor.this.randomIcon(this.inv));
			}
			if(!LootRefactor.this.pointExist(this.packType, this.loc, this.pack) || this.pack == EnumPacks.NULL || !this.running) {
				if(this.i != null)
					this.i.die();
				LootRefactor.loots.remove(this.loc);
				this.cancel();
				return;
			}
			if(this.packType == LootRefactor.PACK)
				LootRefactor.this.spawnPackChest(this.loc, this.pack);
		}

		public EntityItem getItemOf() {
			return this.i;
		}

		public Inventory getInventoryOf() {
			return this.inv;
		}

		public Location getLocationOf() {
			return this.loc;
		}

		public EnumPacks getPackOf() {
			return this.pack;
		}

		public int getPackType() {
			return this.packType;
		}

		public void respawnItem() {
			if(this.i != null)
				this.i.die();
			this.i = LootRefactor.this.dropUItem(this.loc, this.pack.items().get(LootRefactor.random.nextInt(this.pack.items().size())).item());
		}

		public Chunk getChunkOf() {
			return this.c;
		}

		public void kill() {
			this.running = false;
		}
	}
}
