package com.bp389.cranaz.effects;

import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.server.v1_7_R3.PacketPlayOutBed;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Packets {
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
	public static void sendRevivePacket(Player p){
		//TODO
		Packets.broadcastPacket(new PacketPlayOutBed(((CraftPlayer)p).getHandle(), p.getLocation().getBlockX(),
				p.getEyeLocation().getBlockY() - 1, p.getLocation().getBlockY()), null);
	}
}
