package com.bp389.cranaz.FPS;

import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;


public final class TickedSignUpdater extends BukkitRunnable {
	private int i;
	private final int basic;
	private Sign s;
	private String[] lines = new String[4];
	private boolean finish = false;
	public TickedSignUpdater(int countdown, Sign s, String line0,
			String line1, String line2, String line3){
		this.i = countdown;
		this.basic = countdown;
		this.s = s;
		this.lines[0] = line0;
		this.lines[1] = line1;
		this.lines[2] = line2;
		this.lines[3] = line3;
	}
	@Override
    public void run() {
		for(int i2 = 0;i2 < lines.length;++i2){
			s.setLine(i2, lines[i2].replaceAll("<t>", String.valueOf(i)));
		}
		--i;
    }
	public boolean isFinished(){
		return finish;
	}
	public int getBasicCountdown(){
		return this.basic;
	}
	public int getActualValue(){
		return i;
	}
}
