package com.bp389.cranaz;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R1.Packet;

public final class Packets {

	public static void sendPacket(final Player p, final Packet packet) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@SuppressWarnings("deprecation")
	public static void sendPacketPos(final Location l, final int radius, final Packet p, final Player excluded) {
		for(final Player pl : Bukkit.getServer().getOnlinePlayers()) {
			if(excluded != null && pl.equals(excluded))
				continue;
			final int dist = Double.valueOf(l.distance(pl.getLocation())).intValue();
			if(dist <= radius)
				Packets.sendPacket(pl, p);
		}
	}

	@SuppressWarnings("deprecation")
	public static void broadcastPacket(final Packet p, final Player excluded) {
		for(final Player pl : Bukkit.getServer().getOnlinePlayers()) {
			if(excluded != null && pl.equals(excluded))
				continue;
			Packets.sendPacket(pl, p);
		}
	}
}
