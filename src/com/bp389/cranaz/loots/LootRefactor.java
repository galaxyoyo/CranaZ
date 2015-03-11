package com.bp389.cranaz.loots;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R1.EntityArmorStand;

import com.bp389.cranaz.Util.MathUtil;
import com.shampaggon.crackshot.CSUtility;

public final class LootRefactor {

	public LootRefactor() {}

	public static final CSUtility csu = new CSUtility();
	private static JavaPlugin plugin;
	public static World main;
	public static boolean spawnsOk = false;
	public static final Random random = new Random();
	public static CraftServer mainServer;
	//public static volatile Hashtable<Location, ThreadSpawner> loots = new Hashtable<Location, ThreadSpawner>();
	public static volatile HashMap<EntityArmorStand, ThreadSpawner> loots = new HashMap<EntityArmorStand, ThreadSpawner>();
	public static final int PACK = 0, LOOT = 1;
	public static final String packLoc = "plugins/CranaZ/database/chests/", lootLoc = "plugins/CranaZ/database/loots/";

	public static void init(final World world, final JavaPlugin jp, final Server serv) {
		LootRefactor.main = world;
		LootRefactor.plugin = jp;
		LootRefactor.mainServer = (CraftServer)serv;
	}

	/**
	 * 
	 * @param Le
	 *            nom du pack insensible à la casse
	 * @return Un objet constant représentant le pack
	 */
	public EnumPacks parsePack(final String packName) {
		switch(packName.toLowerCase()){
			case "chambre":
				return EnumPacks.CHAMBRE;
			case "ferme":
				return EnumPacks.FERME;
			case "cuisine":
				return EnumPacks.CUISINE;
			case "superette":
				return EnumPacks.SUPERETTE;
			case "militaire":
				return EnumPacks.MILITAIRE;
			case "hopital":
				return EnumPacks.HOPITAL;
			default:
				return EnumPacks.NULL;
		}
	}

	/**
	 * Démarre le spawn de tous les coffres et loots Appelé la toute première
	 * fois qu'un joueur rejoint le serveur, après un reboot
	 */
	public void startSpawns() {
		// Un peu compliqué :-)
		// Il suffit d'analyser avec concentration
		new File(packLoc).mkdirs();new File(lootLoc).mkdirs();
		final ArrayList<File> files = new ArrayList<File>();
		final File[] packs = new File(LootRefactor.packLoc).listFiles(), loots = new File(LootRefactor.lootLoc).listFiles();
		if(packs.length == 0 && loots.length == 0)
			return;
		final ArrayList<String[]> args = new ArrayList<String[]>(), args2 = new ArrayList<String[]>();
		final ArrayList<double[]> locs = new ArrayList<double[]>(), locs2 = new ArrayList<double[]>();
		for(final File pack2 : packs)
			args.add(pack2.getName().split("_"));
		for(final File loot2 : loots){
			args2.add(loot2.getName().split("_"));
			files.add(loot2);
		}
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
			this.startSpawn(this.parsePack(args.get(i)[0]), LootRefactor.PACK, locs.get(i)[0], locs.get(i)[1], locs.get(i)[2], null);
			this.spawnPackChest(new Location(LootRefactor.main, locs.get(i)[0], locs.get(i)[1], locs.get(i)[2]), this.parsePack(args.get(i)[0]));
		}
		if(loots.length == 0)
			return;
		if(args2.size() > 0 && locs2.size() > 0)
			for(int i = 0; i < args2.size(); i++)
				this.startSpawn(this.parsePack(args2.get(i)[0]), LootRefactor.LOOT, locs2.get(i)[0], locs2.get(i)[1], locs2.get(i)[2], files.get(i));
	}

	/*
	 * public void stopAllThreads() { for(int i = 0;i < threads.size();i++) {
	 * threads.get(i).kill(); threads.remove(i); } }
	 */
	/**
	 * Démarre le spawn d'un loot/pack
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
	public void startSpawn(final EnumPacks pack, final int packType, final double x, final double y, final double z, final File rll) {
		if(packType == LootRefactor.PACK || packType == LootRefactor.LOOT) {
			final ThreadSpawner ts = new ThreadSpawner(pack, packType, new Location(LootRefactor.main, x, y, z), rll);
			ts.runTaskTimer(this.plugin, 1L, pack.delay() * 20L);
			//this.loots.put(new Location(LootRefactor.main, x, y, z), ts);
		}
	}

	/**
	 * Forme le chemin d'accès sur HDD d'un pack/loot
	 * 
	 * @param packType
	 *            Le type de pack
	 * @param loc
	 *            La position
	 * @param pack
	 *            Le pack
	 * @return Le chemin d'accès
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
	 * @return True si un pack/loot existe à cette position, false sinon
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
	 * @return Un tableau d'ItemStack contenant aléatoirement des objets du pack
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
	 * @return Une liste d'ItemStack contenant aléatoirement des objets du pack
	 *         -> taille indéfinie
	 * @see #randomToTab(EnumPacks, int)
	 */
	@SuppressWarnings("static-access")
	public ArrayList<ItemStack> randomSort(final EnumPacks pack) {
		if(pack != EnumPacks.NULL) {
			final ArrayList<ItemStack> tmp = new ArrayList<ItemStack>();
			final ArrayList<LootItems> tmp2 = pack.items();
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
	 * @return True si c'est un bloc relatif à un loot
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
	 * Fait spawner un coffre contenant un inventaire aléatoire du pack
	 * 
	 * @param loc
	 *            La position
	 * @param pack
	 *            Le pack
	 * @return True si succès
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
	 * @return Le pack relatif au loot à la position donnée
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
	 * @return Le pack à la position donnée
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
	public void delLoot(final Location loc, final ArmorStand i) {
		((CraftArmorStand)i).getHandle().die();
	}

	/**
	 * Supprime un point de spawn
	 * 
	 * @param packType
	 *            Le type
	 * @param loc
	 *            La position
	 * @return True si succès
	 */
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
			final HashMap<Integer, ArmorStand> alas = new HashMap<Integer, ArmorStand>();
			for(Entity e2 : loc.getChunk().getEntities()){
				if(e2.getType() == EntityType.ARMOR_STAND)
					alas.put(Double.valueOf(e2.getLocation().distance(loc)).intValue(), (ArmorStand)e2);
			}
			if(alas.isEmpty())
				return false;

			this.delLoot(loc, alas.get(MathUtil.smallest((Integer[])alas.keySet().toArray())));
			return new File(thePath).delete();
		}
		return false;
	}

	public boolean deleteLootPoint(final EntityArmorStand eas){
		return LootRefactor.loots.get(eas).delete_loot();
	}

	public LootableArmorStand dropLoot(final Location loc, final ItemStack icon, final ThreadSpawner ts){
		final LootableArmorStand las = new LootableArmorStand(LootRefactor.mainServer, LootableArmorStand.genLootable(loc), icon);
		las.spawnThis();
		LootRefactor.loots.put(las.getHandle(), ts);
		return las;
	}

	/*/**
	 * Lâche un UnpickableItem (objet non ramassable et non stackable) à une
	 * position
	 * 
	 * @param loc
	 *            La position
	 * @param item
	 *            L'objet <<icône>>
	 * @return L'objet lâché pour d'éventuelles modifications
	 *\/
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
	}*/

	/**
	 * 
	 * @param pack
	 *            Le pack
	 * @return Un inventaire prêt à l'usage contenant aléatoirement des objets
	 *         du loot
	 */
	public Inventory randomInvLoot(final EnumPacks pack) {
		final Inventory tmp = Bukkit.createInventory(null, InventoryType.CHEST, "LOOT");
		tmp.setContents(this.randomToTab(pack, 27));
		return tmp;
	}

	/*/**
	 * 
	 * @param c
	 *            Le chunk
	 * @return Une liste contenant les éventuels processus de respawn relatifs
	 *         aux loots du chunk
	 *\/
	@SuppressWarnings({ "unchecked", "static-access" })
	public List<ThreadSpawner> hasLoots(final Chunk c) {
		final ArrayList<ThreadSpawner> al = new ArrayList<ThreadSpawner>();
		//final Hashtable<Location, ThreadSpawner> loots = (Hashtable<Location, ThreadSpawner>) this.loots.clone();
		final Hashtable<Entity, ThreadSpawner> loots2 = (Hashtable<Entity, ThreadSpawner>) this.loots.clone();
		boolean b = false;
		for(final Entry<Entity, ThreadSpawner> e : loots2.entrySet())
			if(c.equals(e.getKey().getBukkitEntity().getLocation().getChunk()) && e.getValue().getPackType() == LootRefactor.LOOT) {
				b = true;
				al.add(e.getValue());
			}
		return b ? al : null;
	}*/

	//
	public boolean defineSpawnPoint(final int packType, final Location loc, final EnumPacks pack) {
		final File tmp = new File(this.formPath(packType, loc, pack));
		try {
			tmp.createNewFile();
			if(packType == LootRefactor.PACK) {
				this.spawnPackChest(loc, pack);
				this.startSpawn(pack, packType, loc.getX(), loc.getY(), loc.getZ(), null);
			} else if(packType == LootRefactor.LOOT)
				this.startSpawn(pack, packType, loc.getX(), loc.getY(), loc.getZ(), tmp);

		} catch(final IOException e) {
			return false;
		}
		return true;
	}

	public LootableArmorStand spawnPackLoot(final Location loc, final ItemStack pack, final ThreadSpawner ts) {
		if(pack != null && pack.getType() != Material.AIR)
			return this.dropLoot(loc, pack, ts);
		return null;
	}

	public final ItemStack randomIcon(final EnumPacks es) {
		ItemStack is = null;
		for(LootItems p : es.items()){
			if(LootRefactor.random.nextInt(p.rare()) == 0){
				is = p.item();
				break;
			}
		}
		if(is == null)
			is = es.items().get(LootRefactor.random.nextInt(es.items().size())).item();
		if(is.getType() == Material.ARROW || is.getType() == Material.BLAZE_POWDER)
			is.setAmount(LootRefactor.random.nextInt(10) + 1);
		return is;
	}

	public class ThreadSpawner extends BukkitRunnable {

		private final EnumPacks pack;
		private final int packType;
		private final Location loc;
		private boolean running = true;
		//private EntityItem i = null;
		private final Chunk c;
		private LootableArmorStand las = null;
		private File related_loot = null;
		private boolean b = true;

		public ThreadSpawner(final EnumPacks pack, final int packType, final Location loc, final File related_loot) {
			this.pack = pack;
			this.packType = packType;
			this.loc = loc;
			this.c = loc.getChunk();
			this.related_loot = related_loot;
		}

		@Override
		public void run() {
			if(!LootRefactor.this.pointExist(this.packType, this.loc, this.pack) || this.pack == EnumPacks.NULL || !this.running) {
				if(this.las != null)
					this.las.die();
				LootRefactor.loots.remove(this.las);
				this.cancel();
				return;
			}
			if(this.packType == LootRefactor.LOOT) {
				if(this.las != null)
					this.las.die();
				LootRefactor.loots.remove(this.las);
				this.las = LootRefactor.this.spawnPackLoot(this.loc, LootRefactor.this.randomIcon(this.pack), this);
				if(b){
					for(Entity e2 : las.getNearbyEntities(1, 5, 1)){
						if(e2.getType() == EntityType.ARMOR_STAND){
							if(MathUtil.locationEquals(las.getLocation(), e2.getLocation(), 0.1D))
								((CraftEntity)e2).getHandle().die();
						}
					}
					b = false;
				}
			}
			if(!LootRefactor.this.pointExist(this.packType, this.loc, this.pack) || this.pack == EnumPacks.NULL || !this.running) {
				if(this.las != null)
					this.las.die();
				LootRefactor.loots.remove(this.las);
				this.cancel();
				return;
			}
			if(this.packType == LootRefactor.PACK)
				LootRefactor.this.spawnPackChest(this.loc, this.pack);
		}

		public LootableArmorStand getStand() {
			return this.las;
		}

		public Location getLocationOf() {
			return this.las.getLocation();
		}

		public EnumPacks getPackOf() {
			return this.pack;
		}

		public boolean delete_loot(){
			final boolean b = this.related_loot.delete();
			this.run();
			return b;
		}

		public int getPackType() {
			return this.packType;
		}

		public void respawnItem() {
			if(this.las != null)
				this.las.die();
			LootRefactor.loots.remove(this.las);
			this.las = LootRefactor.this.dropLoot(this.loc, this.pack.items().get(LootRefactor.random.nextInt(this.pack.items().size())).item(), this);
		}

		public Chunk getChunkOf() {
			return this.c;
		}

		public void kill() {
			this.running = false;
		}
	}
}
