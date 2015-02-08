package com.bp389.cranaz.FPS;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loader;
import com.bp389.cranaz.Util;
import com.bp389.cranaz.Util.MathUtil;
import com.bp389.cranaz.YamlObj;


public final class FPSIO {
	public static boolean init = false;
	public static World w;
	public static final String LOBBY_LOC = "arena.lobby.location", 
			LOBBY_WAIT = "arena.lobby.wait_time", 
			ARENA_LOC = "arena.location", 
			A_OBJ_LOC = "arena.a_obj_location", 
			B_OBJ_LOC = "arena.b_obj_location",
			A_START_LOC = "arena.a_start_loc",
			B_START_LOC = "arena.b_start_loc",
			EXIT_LOC = "arena.exit_loc",
			ARENAS_PATH = "plugins/CranaZ/database/arenas/";
	public static final File ARENAS = new File(ARENAS_PATH);

	public static final HashMap<Player, ArenaTemp> hm = new HashMap<Player, ArenaTemp>();

	public static void flushOut(final Location lobby, final long waitInSeconds, final Location arena, 
			final Location aObjective, final Location bObjective, final String name, final Location aS, 
			final Location bS, final Location eLoc, boolean ow) {
		final File f = getArenaFile(name);
		if(f.exists() && !ow)
			return;
		Util.saveArena(f, waitInSeconds, new YamlObj(LOBBY_LOC, lobby),
				new YamlObj(LOBBY_WAIT, waitInSeconds),
				new YamlObj(ARENA_LOC, arena),
				new YamlObj(A_OBJ_LOC, aObjective),
				new YamlObj(B_OBJ_LOC, bObjective),
				new YamlObj(A_START_LOC, aS),
				new YamlObj(B_START_LOC, bS),
				new YamlObj(EXIT_LOC, eLoc));
	}
	public static String formArenaPath(final String name){
		return ARENAS_PATH + name + ".yml";
	}
	public static File getArenaFile(final String name){
		return new File(formArenaPath(name));
	}
	public static Arena getNewArenaFromName(final String name){
		if(name.isEmpty() || name == null || !getArenaFile(name).exists()){
			return null;
		}
		final File f = getArenaFile(name);
		return new Arena(getLocation(LOBBY_LOC, f),
				(long)MathUtil.parseLocationObject_long(Util.getFromYaml(f, LOBBY_WAIT)),
				getLocation(ARENA_LOC, f),
				getLocation(A_OBJ_LOC, f),
				getLocation(B_OBJ_LOC, f),
				name,
				getLocation(A_START_LOC, f),
				getLocation(B_START_LOC, f),
				getLocation(EXIT_LOC, f)); 
	}

	public static Arena getArenaFromName(String name){
		if(Arena.arenas.containsKey(name)){
			return Arena.arenas.get(name);
		}
		return getNewArenaFromName(name);
	}
	public static String parseArenaFName(String n){
		return n.substring(0, n.lastIndexOf("."));
	}

	private static Location getLocation(String path, File f){
		return Util.loadValidLocation(f, path);
	}
	public static void startArenas(){
		final File[] ft = ARENAS.listFiles();
		if(ft == null || ft.length <= 0)
			return;
		for(File f : ft){
			final Arena a = getArenaFromName(parseArenaFName(f.getName()));
			Arena.arenas.put(a.getName(), a);
			new ArenaSchedule(a).runTaskLater(Loader.plugin, a.getMatchDelay() * 20L);
		}
	}

	public static void createAndStartArena(final ArenaTemp base){
		flushOut(base.lobby, base.del, base.mainArena, base.aObj, base.bObj,
				base.name, base.teamAStart, base.teamBStart, base.exitLoc, false);
		final Arena a = getNewArenaFromName(base.name);
		Arena.arenas.put(a.getName(), a);
		new ArenaSchedule(a).runTaskLater(Loader.plugin, a.getMatchDelay() * 20L);
	}
	public static void onCreationMode(Player p, long delay, String name){
		if(hm.containsKey(p))
			return;
		PluginMethods.gsay(p, "Clic droit pour définir, clic gauche pour annuler.");
		PluginMethods.gsay(p, "Définissez le lobby.");
		hm.put(p, new ArenaTemp(delay, name));
	}
	public static long getExplosionDelay(){
		return MathUtil.parseLocationObject_long(Util.getFromYaml(FPS.fps_config, FPS.GLOBAL_EXPLOSION_DELAY));
	}
}
