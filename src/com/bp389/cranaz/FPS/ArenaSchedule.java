package com.bp389.cranaz.FPS;

import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;


public final class ArenaSchedule extends BukkitRunnable {
	private Arena a;
	public static HashMap<Arena, ArenaSchedule> sch = new HashMap<Arena, ArenaSchedule>();

	public ArenaSchedule(Arena arena){
		a = arena;
		if(sch.containsKey(arena)){
			try{
				cancel();
			}catch(IllegalStateException e){}
		}
		else
			sch.put(arena, this);
	}
	@Override
	public void run() {
		a.getNextRun();
	}
}
