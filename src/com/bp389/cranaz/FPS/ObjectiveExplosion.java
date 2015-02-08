package com.bp389.cranaz.FPS;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.cranaz.Loader;


public final class ObjectiveExplosion extends BukkitRunnable {
	private Block obj;
	private Arena a;
	public static HashMap<Location, ObjectiveExplosion> goingTo = new HashMap<Location, ObjectiveExplosion>();

	public ObjectiveExplosion(Block objective, Arena a){
		this.obj = objective;
		this.a = a;
		if(!goingTo.containsKey(objective.getLocation()))
			ObjectiveExplosion.goingTo.put(objective.getLocation(), this);
	}
	@Override
	public void run() {
		if(!goingTo.containsKey(obj.getLocation())){
			cancel();
			return;
		}
		obj.getWorld().createExplosion(obj.getLocation().getX(), obj.getLocation().getY(), 
				obj.getLocation().getZ(), 5F, false, false);
		obj.breakNaturally(null);
		a.broadcastGame("L'objectif " + (a.getObjectivePos(obj.getLocation()) == Arena.A ? "A" : "B") + " a explosé ! Le match prend fin dans 20 secondes !");
		new Restart().runTaskLater(Loader.plugin, 20L * 20L);
		if(ArenaSchedule.sch.containsKey(a)){
			ArenaSchedule.sch.get(a).cancel();
			ArenaSchedule.sch.remove(a);
		}
	}
	public final class Restart extends BukkitRunnable {
		@Override
		public void run() {
			a.getNextRun();
		}
	}
}
