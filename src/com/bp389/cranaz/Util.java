package com.bp389.cranaz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.Packet;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;

import org.apache.commons.lang.Validate;

import com.bp389.PluginMethods;
import com.bp389.cranaz.FPS.FPSIO;
import com.bp389.cranaz.ia.ZIA;
import com.bp389.cranaz.items.Items;


public final class Util {

	public static final int LIGHT = 0, HEAVY = 1;


	public static final Object getFromYaml(File file, String path){
		return getFromYaml(file, path, null);
	}
	public static final Object getFromYaml(String file, String path){
		return getFromYaml(file, path, null);
	}
	public static final Object getFromYaml(String file, String path, Object def){
		return getFromYaml(new File(file), path, def);
	}
	public static final Object getFromYaml(File f, String path, Object def){
		final YamlConfiguration yc = new YamlConfiguration();
		try {
			yc.load(f);
			return yc.get(path, def);
		} catch(Exception e) {e.printStackTrace();}
		return def;
	}
	public static final Location loadValidLocation(File f, String path){
		if(!f.exists() || f == null || path == null || path.isEmpty())
			return null;

		Object oX = getFromYaml(f, path + ".x"), 
				oY = getFromYaml(f, path + ".y"),
				oZ = getFromYaml(f, path + ".z"), 
				oYaw = getFromYaml(f, path + ".yaw"), 
				oPitch = getFromYaml(f, path + ".pitch");
		Validate.noNullElements(new Object[]{oX, oY, oZ, oYaw, oPitch});
		double x = MathUtil.parseLocationObject_double(oX), 
				y = MathUtil.parseLocationObject_double(oY), 
				z = MathUtil.parseLocationObject_double(oZ);
		float yaw = MathUtil.parseLocationObject_float(oYaw), 
				pitch = MathUtil.parseLocationObject_float(oPitch);
		return new Location(FPSIO.w, x, y, z, yaw, pitch);
	}





	public static final void saveToYaml(String file, String path, Object toSave){
		saveToYaml(new File(file), path, toSave);
	}
	public static final void saveToYaml(File file, String path, Object toSave){
		final YamlConfiguration yc = new YamlConfiguration();
		try {
			yc.set(path, toSave);
			if(!file.exists())
				file.createNewFile();
			yc.save(file);
		} catch(Exception e) {e.printStackTrace();}
	}
	public static final void saveToYaml(String file, YamlObj... yos){
		saveToYaml(new File(file), yos);
	}
	public static final void saveToYaml(File file, YamlObj... yos){
		final YamlConfiguration yc = new YamlConfiguration();
		try {
			for(YamlObj yo : yos)
				yc.set(yo.path, yo.obj);
			if(!file.exists())
				file.createNewFile();
			yc.save(file);
		} catch(Exception e) {e.printStackTrace();}
	}
	public static final void saveLocations(final File f, YamlObj... yamlObjs){
		PluginMethods.strWriteTo(f, saveLocations(yamlObjs), false);
	}
	public static final String saveLocations(YamlObj... yamlObjs){
		return getYamlLocations(yamlObjs).saveToString();
	}
	public static final YamlConfiguration getYamlLocations(YamlObj...yamlObjs){
		final YamlConfiguration yc = new YamlConfiguration();
		for(YamlObj yo : yamlObjs){

			if(!(yo.obj instanceof Location))
				continue;
			final Location l = (Location)yo.obj;
			yc.set(yo.path + ".x", l.getX());
			yc.set(yo.path + ".y", l.getY());
			yc.set(yo.path + ".z", l.getZ());
			yc.set(yo.path + ".yaw", l.getYaw());
			yc.set(yo.path + ".pitch", l.getPitch());
		}
		return yc;
	}
	public static final void saveArena(final File f, long wL, YamlObj... yamlObjs){
		final YamlConfiguration yc = getYamlLocations(yamlObjs);
		yc.set(FPSIO.LOBBY_WAIT, wL);
		PluginMethods.strWriteTo(f, yc.saveToString(), false);
	}





	public static ItemStack[] fromList(List<ItemStack> from){
		ItemStack[] ot = new ItemStack[from.size()];
		for(int i = 0;i < from.size();++i)
			ot[i] = from.get(i);
		return ot;
	}
	public static Inventory copyPlayerInventory(PlayerInventory from){
		final Inventory i = Bukkit.createInventory(null, InventoryType.PLAYER);
		i.addItem(from.getContents());
		i.addItem(from.getArmorContents());
		return i;
	}
	public static Inventory genPlayerInventory(Player owner, ItemStack...stacks){
		return genSimpleInventory(owner, InventoryType.PLAYER, stacks);
	}
	public static Inventory genSimpleInventory(InventoryHolder holder, InventoryType type, ItemStack... stacks){
		final Inventory i = Bukkit.createInventory(holder, type);
		i.setContents(stacks);
		return i;
	}
	public static String getPackLink(final int type) {
		final YamlConfiguration fc = new YamlConfiguration();
		String s = "https://dl.dropboxusercontent.com/u/79959333/Dev/CranaZ/Light.zip";
		try {
			fc.load(new File("plugins/CranaZ/res_packs.yml"));
			s = type == LIGHT ? fc.getString("resources.packs.light", "https://dl.dropboxusercontent.com/u/79959333/Dev/CranaZ/Light.zip") : fc
					.getString("resources.packs.heavy", "https://dl.dropboxusercontent.com/u/79959333/Dev/CranaZ/Ultra.zip");
		} catch(IOException | InvalidConfigurationException e) {}
		return s;
	}
	/**
	 * 
	 * @return Le nombre de cases d'inventaires (x9) nécessaire pour
	 *         contenir une case par joueur
	 */
	@SuppressWarnings("deprecation")
	public static int iSize() {
		final double x = Integer.valueOf(Bukkit.getServer().getOnlinePlayers().length).doubleValue(), y = 9D;
		return Double.valueOf(MathUtil.supMultiplier(x, y)).intValue();
	}

	/*
	 * 
	 */
	public static void noSPK_tp(final Player p) {
		final Location l = ZIA.RandomSpawns.randomLoc();
		l.setY(l.getY() + 1D);
		p.teleport(l);
		p.getInventory().clear();
		final ItemStack is = Items.mass();
		is.setDurability(Integer.valueOf(85).shortValue());
		p.getInventory().addItem(Items.water(), Items.genTShirt(new ItemStack(Material.LEATHER_HELMET)), is,
				ZIA.csu.generateWeapon("Smith"), Items.getAmmoStack(new ItemStack(Material.SLIME_BALL, 3)));
	}

	public static void sendPacket(final Player p, final Packet packet) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@SuppressWarnings("deprecation")
	public static void sendPacketPos(final Location l, final int radius, final Packet p, final Player excluded) {
		for(final Player pl : Bukkit.getServer().getOnlinePlayers()) {
			if(excluded != null && pl.equals(excluded))
				continue;
			final int dist = Double.valueOf(l.distance(pl.getLocation())).intValue();
			if(dist <= radius)
				sendPacket(pl, p);
		}
	}
	public static final class MathUtil {
		public static final int NORTH = 0, SOUTH = 1, WEST = 2, EAST = 3;

		/**
		 * 
		 * @param x
		 *            Le nombre x
		 * @param y
		 *            Le nombre y
		 * @return Le multiple de y juste supérieur à x
		 */
		public static double supMultiplier(final double x, final double y) {
			return Math.ceil(x / y) * y;
		}

		public static boolean between(final double number, final double a, final double b) {
			return number < Math.max(a, b) && number > Math.min(a, b);
		}

		public static boolean approxEquals(final double a, final double b, final double approx) {
			return getDifference(a, b) <= approx;
		}
		
		public static double getDifference(final double a, final double b){
			return Math.max(a, b) - Math.min(a, b);
		}

		/**
		 * 
		 * @param from
		 *            L'angle en radian
		 * @return La valeur de l'angle convertie en degrés
		 */
		public static double radianToDegrees(final double from) {
			return 180 * from / Math.PI;
		}
		
		public static double positivize(double a){
			return Math.sqrt(a * a);
		}

		public static double degreesToRadians(final double from) {
			return from * Math.PI / 180;
		}

		public static double getIsocelLength(final double hypothenuse) {
			return Math.sqrt(hypothenuse * hypothenuse / 2);
		}

		public static boolean locationEquals(final Location a, final Location b, double epsilon) {
			return MathUtil.approxEquals(a.getX(), b.getX(), epsilon) && MathUtil.approxEquals(a.getY(), b.getY(), epsilon)
					&& MathUtil.approxEquals(a.getZ(), b.getZ(), epsilon);
		}
		public static Location trueLoc(final Location src){
			return new Location(src.getWorld(), src.getX(), src.getY() + 1D, src.getZ(), src.getYaw(), src.getPitch());
		}
		@SuppressWarnings("unchecked")
		public static ArrayList<Entity> get_NMS_optimizedEntities(Entity e, double x, double y, double z){
			return (ArrayList<Entity>)e.world.getEntities(e, e.getBoundingBox().grow(x, y, z));
		}

		public static int smallest(Integer... params){
			if(params.length == 1)
				return params[0].intValue();
			final boolean b = params.length % 2 == 0;
			final int iterat = b ? params.length / 2 : params.length / 2 + 1;
			int i0 = Math.min(params[0].intValue(), params[1].intValue()), temp = i0;
			for(int i = 2;i < iterat;++i){
				temp = Math.min(i0, params[i].intValue());
			}
			return temp;
		}

		public static double parseLocationObject_double(final Object o){
			if(o instanceof Double)
				return (double)o;
			else if(o instanceof Integer)
				return Integer.valueOf((int)o).doubleValue();
			else if(o instanceof Float)
				return Float.valueOf((float)o).doubleValue();
			return Double.NaN;
		}
		public static float parseLocationObject_float(final Object o){
			return Double.valueOf(parseLocationObject_double(o)).floatValue();
		}
		public static long parseLocationObject_long(final Object o){
			return Double.valueOf(parseLocationObject_double(o)).longValue();
		}
		
		public static float toFloat(double from){
			return Double.valueOf(from).floatValue();
		}
		
		public static void particleRay(final Location a, final Location b){
			final Location c = a.clone();
			Vector v = c.toVector().subtract(b.toVector()).normalize();
			c.add(v);
			Packets.broadcastPacket(new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, 
					false, toFloat(c.getX()), toFloat(c.getY()), toFloat(c.getZ()), 1, 1, 1, 0F, 10), null);
		}
	}
}
