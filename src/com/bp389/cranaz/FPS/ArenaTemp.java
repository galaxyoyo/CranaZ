package com.bp389.cranaz.FPS;

import org.bukkit.Location;


public final class ArenaTemp {
	public Location teamAStart, teamBStart, aObj, bObj, lobby, mainArena, exitLoc;
	public long del;
	public String name;
	public int state = 0;
	public ArenaTemp(long del, String name){
		this.del = del;
		this.name = name;
	}
}
