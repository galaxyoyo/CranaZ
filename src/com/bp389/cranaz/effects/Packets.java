package com.bp389.cranaz.effects;

import net.minecraft.server.v1_7_R3.Packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class Packets {
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
	public static void broadcastPacket(Packet p, Player excluded){
		for(Player pl : Bukkit.getServer().getOnlinePlayers()){
			if(excluded != null && pl.equals(excluded))
				continue;
			sendPacket(pl, p);
		}
	}
}
