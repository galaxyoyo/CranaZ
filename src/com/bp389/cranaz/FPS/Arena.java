package com.bp389.cranaz.FPS;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R1.Packet;

import org.apache.commons.lang.Validate;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loader;
import com.bp389.cranaz.Util;
import com.bp389.cranaz.Util.MathUtil;
import com.bp389.cranaz.FPS.classes.GameClass;


public final class Arena {
	private Location lobby, arena, aObj, bObj, aStart, bStart, eLoc;
	private long matchDuration;
	private String name;
	private ArrayList<Sign> signs = new ArrayList<Sign>();
	public static HashMap<Player, Arena> hasJoined = new HashMap<Player, Arena>();
	public static HashMap<Player, Inventory> saves = new HashMap<Player, Inventory>();
	private ArrayList<Player> teamA = new ArrayList<Player>(),
			teamB = new ArrayList<Player>(),
			players = new ArrayList<Player>(),
			playing = new ArrayList<Player>(); 
	@SuppressWarnings("unused")
	private boolean active = true, running = false;
	private int size = 20;
	public static HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	public static HashMap<Location, Arena> objectives = new HashMap<Location, Arena>();
	public static final int NOT = -1, A = 0, B = 1;
	public Arena(final Location lobby, final long waitLobby, final Location arena,
			final Location aObj, final Location bObj, final String name, final Location aS, final Location bS,
			final Location eLoc){
		Validate.noNullElements(new Object[]{lobby, waitLobby, arena, aObj, bObj, name, aS, bS});
		this.lobby = lobby;
		this.arena = arena;
		this.aObj = aObj;
		this.bObj = bObj;
		this.eLoc = eLoc;
		this.matchDuration = waitLobby;
		this.aStart = aS;
		this.bStart = bS;
		this.name = name;
		Arena.objectives.put(aObj, this);
		Arena.objectives.put(bObj, this);
		this.running = true;
	}
	public Location getLobbyLocation(){
		return this.lobby;
	}
	public Location getArenaLocation(){
		return this.arena;
	}
	public Location get_aObjLocation(){
		return this.aObj;
	}
	public Location getTeamAStartLocation(){
		return this.aStart;
	}
	public Location getTeamBStartLocation(){
		return this.bStart;
	}
	public Location get_bObjLocation(){
		return this.bObj;
	}
	public long getMatchDelay(){
		return this.matchDuration;
	}
	public int getMatchDelay_integer(){
		return Long.valueOf(matchDuration).intValue();
	}
	public String getName(){
		return this.name;
	}
	public void updateSigns(){
		for(Sign s : signs){
			s.setLine(2, String.valueOf(players.size()) + "/" + String.valueOf(size) + " dans le lobby.");
			s.setLine(3, String.valueOf(playing.size()) + "/" + String.valueOf(size) + " en jeu.");
		}
	}
	public void addPlayer(Player p, Sign s1){
		if(s1 != null)
			signs.add(s1);
		updateSigns();
		players.add(p);
		hasJoined.put(p, this);
		saves.put(p, Util.copyPlayerInventory(p.getInventory()));
		p.teleport(lobby);
	}
	public void addPlayer(Player p){
		addPlayer(p, null);
	}
	public boolean isLobby(Player p){
		return players.contains(p);
	}
	public boolean isInGame(Player p){
		return playing.contains(p);
	}
	public static boolean isInArena(Player p){	
		return hasJoined.containsKey(p);
	}
	@SuppressWarnings("deprecation")
    public void forceRemove(Player p, boolean game, boolean exit){
		if((!playing.contains(p) && game) && (!players.contains(p) && !game))
			return;
		p.setHealth(20D);
		if(exit){
			p.getInventory().setContents(saves.get(p).getContents());
			p.updateInventory();
			saves.remove(p);
			hasJoined.remove(p);
		}
		p.teleport(exit ? eLoc : lobby);
		if(!exit)
			players.add(p);
		else
			players.remove(p);
		playing.remove(p);
		updateSigns();
	}
	public void getNextRun(){
		finishCurrent();
		broadcastAll(ChatColor.BOLD + "Le prochain match débute dans 20 secondes !" + ChatColor.RESET);
		final Arena a = this;
		new BukkitRunnable(){
			@Override
			public void run() {
				int i = 0;
				for(Player p : players){
					if(i >= size)
						break;
					playing.add(p);
					if(i % 2 == 0){
						teamA.add(p);
						p.teleport(aStart);
						p.getInventory().addItem(new ItemStack(Material.COMPASS));
						p.setCompassTarget(bObj);
						PluginMethods.gsay(p, "Tu es dans l'équipe A, tu attaques !");
					}
					else{
						teamB.add(p);
						p.teleport(bStart);
						PluginMethods.gsay(p, "Tu es dans l'équipe B, tu défends !");
					}
					GameClass.handlePlayerClass(p);
					players.remove(p);
					++i;
				}
				new ArenaSchedule(a).runTaskLater(Loader.plugin, getMatchDelay() * 20L);
				updateSigns();

			}
		}.runTaskLater(Loader.plugin, 20L * 20L);
	}
	public void finishCurrent(){
		broadcastAll("Le match actuel est terminé !");
		regen();
		if(!running || playing.size() <= 0)
			return;
		for(Player p : playing){
			p.sendMessage(ChatColor.DARK_GREEN + "Tu peux rejouer si tu le veux." + ChatColor.RESET);
			forceRemove(p, true, false);
		}
		teamA.clear();
		teamB.clear();
		playing.clear();
	}
	private void regen(){
		bObj.getWorld().getBlockAt(bObj).breakNaturally(null);
		bObj.getWorld().getBlockAt(bObj).setType(Material.REDSTONE_BLOCK);
		aObj.getWorld().getBlockAt(aObj).breakNaturally(null);
		aObj.getWorld().getBlockAt(aObj).setType(Material.REDSTONE_BLOCK);
		updateSigns();
	}
	public void broadcastLobby(String message){
		for(Player p : this.players)
			p.sendMessage(message);
	}
	public void broadcastGame(String message){
		for(Player p : this.playing)
			p.sendMessage(message);
	}
	public void broadcastAll(String message){
		broadcastLobby(message);
		broadcastGame(message);
	}
	public int getObjectivePos(Location l){
		return Arena.objectives.containsKey(l) ? (MathUtil.locationEquals(l, aObj, 0.1D) ? A : B) : NOT;
	}
	public void turnOff(){
		active = false;
		running = false;
	}
	public void manuallyPutSign(Sign s){
		signs.add(s);
	}
	public int getTeam(Player p){
		if(!playing.contains(p))
			return NOT;
		return teamA.contains(p) ? A : B;
	}

	public void objectiveRayTrace(){
		MathUtil.particleRay(bObj, new Location(bObj.getWorld(), bObj.getX(), 
				bObj.getY() + 50D, bObj.getZ(), bObj.getYaw(), bObj.getPitch()));
	}
	public void broadcastPacket(boolean lobby, boolean playing, Packet p){
		if(lobby){
			for(Player p0 : this.players){
				((CraftPlayer)p0).getHandle().playerConnection.sendPacket(p);
			}
		}
		if(playing){
			for(Player p1 : this.playing){
				((CraftPlayer)p1).getHandle().playerConnection.sendPacket(p);
			}
		}
	}
	@Override
	public String toString() {
		String s = "Arène "
				+ this.name
				+ ". Position globale: "
				+ this.arena.toString();
		return s;
	}
}
