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
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.shampaggon.crackshot.CSUtility;

public final class LootRefactor {
	public LootRefactor(){}
	public static final CSUtility csu = new CSUtility();
	private static JavaPlugin plugin;
	private static World main;
	public static boolean spawnsOk = false;
	protected static final Random random = new Random();
	public static volatile Hashtable<Location, ThreadSpawner> loots = new Hashtable<Location, ThreadSpawner>();
	public static final int PACK = 0, LOOT = 1;
	public static final String packLoc = "plugins/CranaZ/PacksAndLoots/PackPoints/", lootLoc = "plugins/CranaZ/PacksAndLoots/LootPoints/";
	public static void init(final World world, final JavaPlugin jp){
		main = world;
		plugin = jp;
	}
	/**
	 * 
	 * @param Le nom du pack insensible à la casse
	 * @return Un objet constant représentant le pack
	 */
	public EnumPacks parsePack(final String packName)
	{
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
	 * Démarre le spawn de tous les coffres et loots
	 * Appelé la toute première fois qu'un joueur rejoint le serveur, après un reboot
	 */
	public void startSpawns()
	{
		//Un peu compliqué :-)
		//Il suffit d'analyser avec concentration
		File[] packs = new File(packLoc).listFiles(), loots = new File(lootLoc).listFiles();
		if(packs.length == 0 && loots.length == 0)
			return;
		ArrayList<String[]> args = new ArrayList<String[]>(), args2 = new ArrayList<String[]>();
		ArrayList<double[]> locs = new ArrayList<double[]>(), locs2 = new ArrayList<double[]>();
		for(int i = 0;i < packs.length;i++)
			args.add(packs[i].getName().split("_"));
		for(int i = 0; i < loots.length;i++)
			args2.add(loots[i].getName().split("_"));
		for(int i = 0;i < args.size();i++)
		{
			double[] tmp = new double[3];
			tmp[0] = Double.valueOf(args.get(i)[1]).doubleValue();
			tmp[1] = Double.valueOf(args.get(i)[2]).doubleValue();
			tmp[2] = Double.valueOf(args.get(i)[3]).doubleValue();
			locs.add(tmp);
		}
		for(int i = 0;i < args2.size();i++)
		{
			double[] tmp = new double[3];
			tmp[0] = Double.valueOf(args2.get(i)[1]).doubleValue();
			tmp[1] = Double.valueOf(args2.get(i)[2]).doubleValue();
			tmp[2] = Double.valueOf(args2.get(i)[3]).doubleValue();
			locs2.add(tmp);
		}
		for(int i = 0;i < args.size();i++)
		{
			startSpawn(parsePack(args.get(i)[0]), PACK, locs.get(i)[0], locs.get(i)[1], locs.get(i)[2]);
			spawnPackChest(new Location(main, locs.get(i)[0], locs.get(i)[1], locs.get(i)[2]), parsePack(args.get(i)[0]));
		}
		if(loots.length == 0)
			return;
		if(args2.size() > 0 && locs2.size() > 0){
			for(int i = 0;i < args2.size();i++){
				startSpawn(parsePack(args2.get(i)[0]), LOOT, locs2.get(i)[0], locs2.get(i)[1], locs2.get(i)[2]);
			}
		}
	}
	/*public void stopAllThreads()
	{
		for(int i = 0;i < threads.size();i++)
		{
			threads.get(i).kill();
			threads.remove(i);
		}
	}*/
	/**
	 * Démarre le spawn d'un loot/pack
	 * @param pack Le pack
	 * @param packType Le type de pack, {@link #LOOT} ou {@link #PACK}
	 * @param x La position X
	 * @param y Y
	 * @param z Z
	 */
	@SuppressWarnings("static-access")
    public void startSpawn(EnumPacks pack, int packType, double x, double y, double z)
	{
		if(packType == PACK || packType == LOOT){
			ThreadSpawner ts = new ThreadSpawner(pack, packType, new Location(main, x, y, z));
			ts.runTaskTimer(this.plugin, 1, pack.delay() * 20);
			this.loots.put(new Location(main, x, y, z), ts);
		}
	}
	/**
	 * Forme le chemin d'accès sur HDD d'un pack/loot
	 * @param packType Le type de pack
	 * @param loc La position
	 * @param pack Le pack
	 * @return Le chemin d'accès
	 */
	public String formPath(final int packType, final Location loc, final EnumPacks pack)
	{
		String tmp = "";
		if(packType == PACK)
		{
			tmp += (packLoc + pack.toString() + "_");
			tmp += (String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getZ()).intValue()));
		}
		else if(packType == LOOT)
		{
			tmp += (lootLoc + pack.toString() + "_");
			tmp += (String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getZ()).intValue()));
		}
		return tmp;
	}
	/**
	 * 
	 * @param packType Le type de pack
	 * @param loc Sa position
	 * @param pack Le pack
	 * @return True si un pack/loot existe à cette position, false sinon
	 */
	public boolean pointExist(final int packType, final Location loc, final EnumPacks pack)
	{
		String tmp = formPath(packType, loc, pack);
		return new File(tmp).exists();
	}
	/**
	 * 
	 * @param pack Le pack
	 * @param maxSize La taille maximale du pack
	 * @return Un tableau d'ItemStack contenant aléatoirement des objets du pack
	 * @see #randomSort(EnumPacks)
	 */
	public ItemStack[] randomToTab(EnumPacks pack, int maxSize)
	{
		final ArrayList<ItemStack> tmp = randomSort(pack);
		final int i2 = tmp.size() > maxSize ? maxSize : tmp.size();
		ItemStack[] tmp2 = new ItemStack[i2];
		for(int i = 0;i < i2;i++)
			tmp2[i] = tmp.get(i);
		return tmp2;
	}
	/**
	 * 
	 * @param pack Le pack
	 * @return Une liste d'ItemStack contenant aléatoirement des objets du pack -> taille indéfinie
	 * @see #randomToTab(EnumPacks, int)
	 */
	@SuppressWarnings("static-access")
    public ArrayList<ItemStack> randomSort(EnumPacks pack)
	{
		if(pack != EnumPacks.NULL)
		{
			ArrayList<ItemStack> tmp = new ArrayList<ItemStack>();ArrayList<Packs> tmp2 = pack.items();
			for(int i = 0;i < tmp2.size();i++){
				if(this.random.nextInt(tmp2.get(i).rare()) < 1){
					tmp.add(tmp2.get(i).item());
				}
				if(this.random.nextInt(1) == 0){
					tmp.add(new ItemStack(Material.AIR));
				}
			}
			return tmp;
		}
		return null;
	}
	/**
	 * 
	 * @param loc La position
	 * @param pack Le pack
	 * @return True si c'est un bloc relatif à un loot
	 */
	public boolean isLootBlock(Location loc, EnumPacks pack)
	{
		String temp = (lootLoc + pack.toString() + "_" + String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getZ()).intValue()));
		return new File(temp).exists();
	}
	//
	public Object[] toTab(final ArrayList<? extends Object> from)
	{
		Object[] tmp = new Object[from.size()];
		for(int i = 0;i < from.size();i++)
			tmp[i] = from.get(i);
		return tmp;
	}
	/**
	 * Fait spawner un coffre contenant un inventaire aléatoire du pack
	 * @param loc La position
	 * @param pack Le pack
	 * @return True si succès
	 */
	public boolean spawnPackChest(Location loc, EnumPacks pack)
	{
		World temp = loc.getWorld();
		Location loc2 = loc;
		boolean result = false;
		if(pack != EnumPacks.NULL)
		{
			if(temp.getBlockAt(loc).getType() == Material.CHEST)
			{
				Chest chest = (Chest)temp.getBlockAt(loc2).getState();
				chest.getBlockInventory().setContents(randomToTab(pack, 27));
				result = true;
			}
			else
			{
				Block block = temp.getBlockAt(loc2);
				block.setType(Material.CHEST);
				if(block.getType() == Material.CHEST){
					Chest chest = (Chest)block.getState();
					chest.getBlockInventory().setContents(randomToTab(pack, 27));
				}
				result = true;
			}
		}
		return result;
	}
	/**
	 * 
	 * @param loc La position
	 * @return Le pack relatif au loot à la position donnée
	 * @see #getPackAt(Location, int)
	 */
	public EnumPacks getLootAt(final Location loc)
	{
		String path = lootLoc, locs = (String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getZ()).intValue()));
		File[] fs = new File(path).listFiles();
		String[] temp = new String[]{"NULL"};
		for(int i = 0;i < fs.length;i++)
		{
			if(fs[i].getName().contains(locs)){
				temp = fs[i].getName().split("_");
				break;
			}
		}
		return parsePack(temp[0]);
	}
	/**
	 * 
	 * @param loc La position
	 * @param packType Le type de pack
	 * @return Le pack à la position donnée
	 */
	public EnumPacks getPackAt(final Location loc, final int packType)
	{
		if(packType == PACK){
			String path = packLoc, locs = (String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getZ()).intValue()));
			File[] fs = new File(path).listFiles();
			String[] temp = new String[]{"NULL"};
			for(int i = 0;i < fs.length;i++)
			{
				if(fs[i].getName().contains(locs)){
					temp = fs[i].getName().split("_");
					break;
				}
			}
			return parsePack(temp[0]);
		}
		else if(packType == LOOT){
			String path = lootLoc, locs = (String.valueOf(Double.valueOf(loc.getX()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getY()).intValue()) + "_" + String.valueOf(Double.valueOf(loc.getZ()).intValue()));
			File[] fs = new File(path).listFiles();
			String[] temp = new String[]{"NULL"};
			for(int i = 0;i < fs.length;i++)
			{
				if(fs[i].getName().contains(locs)){
					temp = fs[i].getName().split("_");
					break;
				}
			}
			return parsePack(temp[0]);
		}
		return EnumPacks.NULL;
	}
	/**
	 * Casse un coffre sans en disperser le contenu
	 * @param loc La position
	 */
	public void breakChest(Location loc)
	{
		Block block = loc.getWorld().getBlockAt(loc);
		if(block.getType() == Material.CHEST)
		{
			Chest chest = (Chest)block.getState();
			chest.getBlockInventory().clear();
			chest.getBlock().breakNaturally(null);
		}
	}
	//
	public void delLoot(Location loc, UnpickableItem i){
		i.die();
	}
	/**
	 * Supprime un point de spawn
	 * @param packType Le type
	 * @param loc La position
	 * @return True si succès
	 */
	@SuppressWarnings("static-access")
    public boolean deleteSpawnPoint(final int packType, final Location loc)
	{
		if(packType == PACK)
		{
			EnumPacks temp = getPackAt(loc, PACK);
			if(temp.toString().equalsIgnoreCase("NULL"))
				return false;
			String thePath = (formPath(packType, loc, temp));
			breakChest(loc);
			return new File(thePath).delete();
		}
		else if(packType == LOOT)
		{
			EnumPacks temp = getPackAt(loc, LOOT);
			if(temp.toString().equalsIgnoreCase("NULL"))
				return false;
			String thePath = (formPath(packType, loc, temp));
			delLoot(loc, this.loots.get(loc).getItemOf());
			return new File(thePath).delete();
		}
		return false;
	}
	/**
	 * Lâche un UnpickableItem (objet non ramassable et non stackable) à une position
	 * @param loc La position
	 * @param item L'objet <<icône>>
	 * @return L'objet lâché pour d'éventuelles modifications
	 */
	public UnpickableItem dropUItem(Location loc, ItemStack item)
	{
		UnpickableItem i = new UnpickableItem(((CraftWorld)loc.getWorld()).getHandle(), loc.getX() + 0.5D, loc.getY(), loc.getZ() + 0.5D, CraftItemStack.asNMSCopy(item));
	    ((CraftWorld)loc.getWorld()).getHandle().addEntity(i, SpawnReason.CUSTOM);
	    return i;
	}
	/**
	 * 
	 * @param pack Le pack
	 * @return Un inventaire prêt à l'usage contenant aléatoirement des objets du loot
	 */
	public Inventory randomInvLoot(EnumPacks pack)
	{
		Inventory tmp = Bukkit.createInventory(null, InventoryType.CHEST, "LOOT");
		tmp.setContents(randomToTab(pack, 27));
		return tmp;
	}
	/**
	 * 
	 * @param c Le chunk
	 * @return Une liste contenant les éventuels processus de respawn relatifs aux loots du chunk
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
    public List<ThreadSpawner> hasLoots(Chunk c){
		ArrayList<ThreadSpawner> al = new ArrayList<ThreadSpawner>();
		Hashtable<Location, ThreadSpawner> loots = (Hashtable<Location, ThreadSpawner>)this.loots.clone();
		boolean b = false;
		for(Entry<Location, ThreadSpawner> e : loots.entrySet()){
			if(c.equals(e.getKey().getChunk()) && e.getValue().getPackType() == LOOT){
				b = true;
				al.add(e.getValue());
			}
		}
		return b ? al : null;
	}
	//
	public boolean defineSpawnPoint(final int packType, final Location loc, final EnumPacks pack)
	{
		String tmp = formPath(packType, loc, pack);
		try {
	        new File(tmp).createNewFile();
	        if(packType == PACK){
	        	spawnPackChest(loc, pack);
		        startSpawn(pack, packType, loc.getX(), loc.getY(), loc.getZ());
	        }
	        else if(packType == LOOT){
	        	startSpawn(pack, packType, loc.getX(), loc.getY(), loc.getZ());
	        }
	        	
        } catch (IOException e) {
	        return false;
        }
		return true;
	}
	public UnpickableItem spawnPackLoot(final Location loc, final ItemStack pack)
	{
		if(pack != null && pack.getType() != Material.AIR){
			return dropUItem(loc, pack);
		}
		return null;
	}
	public final ItemStack randomIcon(final Inventory loot){
		ItemStack is = new ItemStack(Material.ARROW);
		for(int i = 0; i < loot.getContents().length;i++){
			if(loot.getContents()[i] != null && loot.getContents()[i].getType() != Material.AIR){
				is = loot.getContents()[i];
				break;
			}
		}
		return is;
	}
	public class ThreadSpawner extends BukkitRunnable
	{
		private EnumPacks pack;
		private int packType;
		private Location loc;
		private boolean running = true;
		private Inventory inv = null;
		private UnpickableItem i = null;
		private Chunk c;
		public ThreadSpawner(EnumPacks pack, int packType, Location loc){
			this.pack = pack;
			this.packType = packType;
			this.loc = loc;
			c = loc.getChunk();
		}
		@Override
		public void run() {
					if(!pointExist(packType, loc, pack) || pack == EnumPacks.NULL || !running){
						if(i != null)
							i.die();
						loots.remove(loc);
						cancel();
						return;
					}
					if(packType == LOOT){
	                	if(i != null)
	                		i.die();
	                	inv = randomInvLoot(pack);
	                	i = spawnPackLoot(loc, randomIcon(inv));
	                }
					if(!pointExist(packType, loc, pack) || pack == EnumPacks.NULL || !running){
						if(i != null)
							i.die();
						loots.remove(loc);
						cancel();
						return;
					}
		                if(packType == PACK)
		                	spawnPackChest(loc, pack);
		}
		public UnpickableItem getItemOf(){
			return this.i;
		}
		public Inventory getInventoryOf(){
			return this.inv;
		}
		public Location getLocationOf(){
			return this.loc;
		}
		public EnumPacks getPackOf(){
			return this.pack;
		}
		public int getPackType(){
			return this.packType;
		}
		public void respawnItem(){
			if(i != null)
				i.die();
			i = dropUItem(loc, pack.items().get(random.nextInt(pack.items().size())).item());
		}
		public Chunk getChunkOf(){
			return this.c;
		}
		public void kill()
		{
			running = false;
		}
	}
}
