package com.bp389.cranaz.FPS;

import java.util.HashMap;

import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.cranaz.Loader;


public final class ArenaSchedule extends BukkitRunnable {
	private Arena a;
	public static HashMap<Arena, ArenaSchedule> schedules = new HashMap<Arena, ArenaSchedule>();
	private HashMap<Sign, TickedSignUpdater> hm = new HashMap<Sign, TickedSignUpdater>();
	public ArenaSchedule(Arena a, Sign... signs){
		this.a = a;
		for(Sign s : signs){
			final TickedSignUpdater tsu = new TickedSignUpdater(a.getMatchDelay_integer(), s, s.getLine(0),
					s.getLine(1), s.getLine(2), s.getLine(3));
			tsu.runTaskTimerAsynchronously(Loader.plugin, 20L, 20L);
			hm.put(s, tsu);
		}
		if(schedules.containsKey(a))
			return;
		schedules.put(a, this);
	}
	@Override
	public void run() {
		a.getNextRun();
	}
	public void resetSigns(){
		
	}
}
