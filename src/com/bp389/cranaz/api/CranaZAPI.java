package com.bp389.cranaz.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;


abstract class CranaZAPI {
	protected JavaPlugin jp;
	private Logger logger;
	public CranaZAPI(JavaPlugin plugin){
		this.jp = plugin;
		this.logger = jp.getLogger();
	}
	
	protected void console(Level level, String msg){
		logger.log(level, "[API]" + msg);
	}
	
	protected Entity fromNMS(net.minecraft.server.v1_8_R1.Entity from){
		return from.getBukkitEntity();
	}
	
	protected net.minecraft.server.v1_8_R1.Entity fromBukkit(Entity from){
		return ((CraftEntity)from).getHandle();
	}
}
