package com.bp389.cranaz.ia;

import static com.bp389.PluginMethods.alert;
import static com.bp389.PluginMethods.gsay;
import static com.bp389.PluginMethods.info;
import static com.bp389.PluginMethods.mdestroy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.server.v1_7_R3.Packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.Loader;
import com.bp389.cranaz.events.GEvent;
import com.bp389.cranaz.ia.entities.CustomEntityType;
import com.bp389.cranaz.ia.entities.EnhancedZombie;
import com.bp389.cranaz.items.Items;
import com.shampaggon.crackshot.CSUtility;

public class ZIA extends Loadable
{
	public static HashMap<Player, Player> ps = new HashMap<Player, Player>();
	public static final CSUtility csu = new CSUtility();
	@Override
	public void onEnable() {
		info("CranaZombieIA enregistre les entités...");
		new File("plugins/CranaZ/spawners/").mkdirs();
		new File("plugins/CranaZ/Divers/spawns/").mkdirs();
		if(!new File("plugins/CranaZ/res_packs.yml").exists()){
			getConfig().set("resources.packs.light", "https://dl.dropboxusercontent.com/u/79959333/Dev/CranaZ/Light.zip");
			getConfig().set("resources.packs.heavy", "https://dl.dropboxusercontent.com/u/79959333/Dev/CranaZ/Ultra.zip");
			try {
	            new File("plugins/CranaZ/res_packs.yml").createNewFile();
	            getConfig().save(new File("plugins/CranaZ/res_packs.yml"));
            } catch (IOException e) {}
		}
		
		CustomEntityType.registerEntities();
		new GoMenu();
		new ResPackMenu();
		info("CranaZombieIA demarré avec succès");
	}
	@Override
	public void onDisable() {
		info("CranaZombieIA efface les entités...");
		CustomEntityType.unregisterEntities();
		for(VirtualSpawner vs : VirtualSpawner.al){
			vs.saveAll();
		}
		info("CranaZombieIA éteint avec succès");
		mdestroy();
	}
	@SuppressWarnings("deprecation")
    @Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	    if(command.getName().equalsIgnoreCase("cranaz"))
	    {
	    	if(args.length > 0)
	    	{
	    		if(args[0].equalsIgnoreCase("reload"))
		    	{
		    		/*if(sender.hasPermission("cranaz.reload") || sender.isOp())
		    		{
		    			this.getServer().getPluginManager().disablePlugin(this);
			    		this.getServer().getPluginManager().enablePlugin(this);
			    		if(sender instanceof Player)
			    			gsay((Player)sender, "CranaZ IA a été rechargé avec succès.");
			    		else
			    			sender.sendMessage("CranaZ IA a été rechargé avec succès.");
		    		}
		    		else
		    			alert((Player) sender, "Vous n'avez pas la permission pour recharger CranaZ IA");*/
		    	}
	    		else if(args[0].equalsIgnoreCase("accept")){
	    			if(sender instanceof Player){
	    				Player pt = (Player)sender;
	    				if(ps.containsKey(pt)){
	    					pt.teleport(ps.get(pt), TeleportCause.PLUGIN);
	    					GEvent.playings.add(pt);
	    					pt.getInventory().clear();
	    					ItemStack is = Items.customSSword();
	    					is.setDurability(Integer.valueOf(85).shortValue());
	    					pt.getInventory().addItem(Items.customWater(), Items.genTShirt(new ItemStack(Material.LEATHER_HELMET)), is, csu.generateWeapon("Smith"), Items.getAmmoStack(new ItemStack(Material.SLIME_BALL, 3)));
	    					ps.remove(pt);
	    				}
	    				else
	    					pt.sendMessage("§r§cVous n'avez pas de requête.");
	    			}
	    			else
	    				sender.sendMessage("Veuillez vous connecter en tant que joueur.");
	    		}
	    		else if(args[0].equalsIgnoreCase("decline")){
	    			if(sender instanceof Player){
	    				Player pt = (Player)sender;
	    				if(ps.containsKey(pt)){
	    					ps.remove(pt);
	    					pt.sendMessage("Demande refusée.");
	    				}
	    				else
	    					pt.sendMessage("§r§cVous n'avez pas de requête.");
	    			}
	    			else
	    				sender.sendMessage("Veuillez vous connecter en tant que joueur.");
	    		}
	    		else if(args[0].equalsIgnoreCase("state")){
	    			if(!(sender instanceof Player)){
	    				return true;
	    			}
	    			Player p = (Player)sender;
	    			VirtualSpawner vs = VirtualSpawner.getNearbySpawner(p.getLocation(), 50);
	    			if(vs == null){
	    				sender.sendMessage("§r§cAucun spawner trouvé dans les 50 blocs autour de vous.");
	    				return true;
	    			}
	    			sender.sendMessage((String[])Arrays.asList("Position: " + vs.getExactLocation().toString(),
	    					"Compteur: " + vs.getCountString() + " / 25",
	    					"Actif: " + (vs.getCountString().equalsIgnoreCase("25") ? "non" : "oui")).toArray());
	    		}
	    		else if(args[0].equalsIgnoreCase("zombie"))
	    		{
	    			if(sender.hasPermission("cranaz.spawn_zombie") || sender.isOp())
	    			{
	    				if(sender instanceof Player)
	    				{
	    					Player player = (Player)sender;
	    					if(args.length > 1)
	    					{
	    						for(int i = 0;i < Integer.valueOf(args[1]);i++)
	    						{
	    	    					spawnZombie((CraftWorld)player.getWorld(), (CraftPlayer)player);
	    						}
	    					}
	    					else
	    					{
		    					spawnZombie((CraftWorld)player.getWorld(), (CraftPlayer)player);
	    					}
	    					gsay(player, "Zombie(s) modifié(s) spawné(s) sur votre position.");
	    				}
	    				else
	    					sender.sendMessage("Veuillez vous connecter en tant que joueur.");
	    			}
	    			else
	    				alert((Player) sender, "Vous n'avez pas la permission.");
	    		}
	    		else if(args[0].equalsIgnoreCase("utils"))
	    		{
	    			if(sender instanceof Player){
    					Items.Diaries.Utils.giveUtils((Player)sender);
    				}
    				else
    					sender.sendMessage("Veuillez vous connecter en tant que joueur.");
	    		}
	    		else if(args[0].equalsIgnoreCase("spawn")){
	    			if(sender instanceof Player){
	    				if(sender.hasPermission("cranaz.define.spawn_point")){
	    					RandomSpawns.setSpawnLoc(((Player)sender).getTargetBlock(null, 50).getLocation());
	    					gsay((Player)sender, "Point de spawn aléatoire défini");
	    				}
	    				else
	    					alert((Player)sender, "Vous n'avez pas la permission !");
	    			}
	    			else
	    				sender.sendMessage("Veuillez vous connecter en tant que joueur.");
	    		}
	    		else if(args[0].equalsIgnoreCase("spawner")){
	    			if(sender instanceof Player){
	    				if(sender.hasPermission("cranaz.define.spawn_point")){
	    					VirtualSpawner vs = new VirtualSpawner(((Player)sender).getLocation(), Loader.plugin);
	    					vs.setRunning(true);
	    				}
	    				else
	    					alert((Player)sender, "Vous n'avez pas la permission !");
	    			}
	    			else
	    				sender.sendMessage("Veuillez vous connecter en tant que joueur.");
	    		}
		    	else
		    	{
		    		sender.sendMessage("/cranaz reload - Recharger CranaZ");
		    		sender.sendMessage("/cranaz zombie [X] - Spawner X zombies modifiés");
		    		sender.sendMessage("/cranaz utils - Obtenir les utilitaires(règles...");
		    		sender.sendMessage("/cranaz spawner - Définir une zone de spawn virtuelle.");
		    		sender.sendMessage("/cranaz state - Obtenir l'etat du spawner le plus proche dans 50 blocs.");
		    		sender.sendMessage("/cranaz spawn - Définir un point de spawn aléatoire.");
		    	}
	    	}
	    	else
	    	{
	    		sender.sendMessage("/cranaz reload - Recharger CranaZ");
	    		sender.sendMessage("/cranaz zombie [X] - Spawner X zombies modifiés");
	    		sender.sendMessage("/cranaz utils - Obtenir les utilitaires(règles...");
	    		sender.sendMessage("/cranaz spawner - Définir une zone de spawn virtuelle.");
	    		sender.sendMessage("/cranaz state - Obtenir l'etat du spawner le plus proche dans 50 blocs.");
	    		sender.sendMessage("/cranaz spawn - Définir un point de spawn aléatoire.");
	    	}
	    }
	    return true;
	}
	/**
	 * Spawne un zombie modifié
	 * @param l  La position
	 * @return Le zombie
	 */
	public static EnhancedZombie spawnZombie(Location l){
		EnhancedZombie customEntity = new EnhancedZombie(((CraftWorld)l.getWorld()).getHandle());
	    customEntity.setLocation(l.getX(), l.getY() + 1D, l.getZ(), l.getYaw(), l.getPitch());
	    ((CraftWorld)l.getWorld()).getHandle().addEntity(customEntity, SpawnReason.CUSTOM);
	    return customEntity;
	}
	/**
	 * Spawne un zombie sur la position d'un joueur
	 * @param world Le monde CraftBukkit
	 * @param player Le joueur CraftBukkit
	 */
	public static void spawnZombie(CraftWorld world, CraftPlayer player)
	{
		Location loc = player.getTargetBlock(null, 50).getLocation();
		EnhancedZombie customEntity = new EnhancedZombie(world.getHandle());
	    customEntity.setLocation(loc.getX(), loc.getY() + 1D, loc.getZ(), loc.getYaw(), loc.getPitch());
	    world.getHandle().addEntity(customEntity, SpawnReason.CUSTOM);
	}
	public static class Utils
	{
		public static final int LIGHT = 0, HEAVY = 1;
		private static JavaPlugin jp;
		public static void ini(JavaPlugin jp){
			Utils.jp = jp;
		}
		/**
		 * 
		 * @param x Le nombre x
		 * @param y Le nombre y
		 * @return Le multiple de y juste supérieur à x
		 */
		public static double math_supMultiplier(double x, double y){
			return Math.ceil((x / y)) * y;
		}
		/**
		 * 
		 * @return Le nombre de cases d'inventaires (x9) nécessaire pour contenir une case par joueur
		 */
		public static int iSize(){
			double x = Integer.valueOf(Bukkit.getServer().getOnlinePlayers().length).doubleValue(), y = 9D;
			return Double.valueOf(math_supMultiplier(x, y)).intValue();
		}
		/*
		 * 
		 */
		public static void noSPK_tp(Player p){
			Location l = ZIA.RandomSpawns.randomLoc();
			l.setY(l.getY() + 1D);
			p.teleport(l);
			p.getInventory().clear();
			ItemStack is = Items.customSSword();
			is.setDurability(Integer.valueOf(85).shortValue());
			p.getInventory().addItem(Items.customWater(), Items.genTShirt(new ItemStack(Material.LEATHER_HELMET)), is, csu.generateWeapon("Smith"), Items.getAmmoStack(new ItemStack(Material.SLIME_BALL, 3)));
		}
		public static void sendPacket(Player p, Packet packet){
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
		}
		public static void sendPacketPos(Location l, int radius, Packet p, Player excluded){
			for(Player pl : Bukkit.getServer().getOnlinePlayers()){
				if(excluded != null && pl.equals(excluded))
					continue;
				int dist = Double.valueOf(l.distance(pl.getLocation())).intValue();
				if(dist <= radius)
					sendPacket(pl, p);
			}
		}
		public static String getPackLink(int type){
			FileConfiguration fc = jp.getConfig();
			String s = "https://dl.dropboxusercontent.com/u/79959333/Dev/CranaZ/Light.zip";
			try {
	            fc.load(new File("plugins/CranaZ/res_packs.yml"));
	            s = type == LIGHT ? fc.getString("resources.packs.light", "https://dl.dropboxusercontent.com/u/79959333/Dev/CranaZ/Light.zip") : fc.getString("resources.packs.heavy", "https://dl.dropboxusercontent.com/u/79959333/Dev/CranaZ/Ultra.zip");
            } catch (IOException | InvalidConfigurationException e) {}
			return s;
		}
	}
	
	public static class RandomSpawns
	{
		private static ArrayList<Location> locs;
		private static JavaPlugin jp;
		private static org.bukkit.World w;
		public static boolean init = false;
		public static void init(JavaPlugin plugin, org.bukkit.World world){
			jp = plugin;
			w = world;
			try {
	            locs = getSpawnPoints();
            } catch (Exception e) {}
		}
		public static void setSpawnLoc(Location l){
			int i = new File("plugins/CranaZ/Divers/spawns/").listFiles().length;
			String s;
			if(i == 0){
				s = "spawn0";
			}
			else
				s = ("spawn" + String.valueOf(i + 1));
			try {
	            new File("plugins/CranaZ/Divers/spawns/" + s + ".yml").createNewFile();
	            FileConfiguration fc = jp.getConfig();
	            fc.set("coords.locX", l.getX());
	            fc.set("coords.locY", l.getY());
	            fc.set("coords.locZ", l.getZ());
	            fc.save(new File("plugins/CranaZ/Divers/spawns/" + s + ".yml"));
            } catch (IOException e) {}
		}
		public static Location randomLoc(){
			if(locs == null)
				return null;
			return locs.get(new Random().nextInt(locs.size()));
		}
        public static ArrayList<Location> getSpawnPoints() throws Exception{
			FileConfiguration temp = jp.getConfig();
			File f = new File("plugins/CranaZ/Divers/spawns/");
			if(f.listFiles().length <= 0)
				return null;
			ArrayList<Location> all = new ArrayList<Location>();
			for(File f1 : f.listFiles()){
				temp.load(f1);
				all.add(new Location(w, temp.getDouble("coords.locX"), temp.getDouble("coords.locY"), temp.getDouble("coords.locZ")));
			}
			return all;
		}
	}
}
