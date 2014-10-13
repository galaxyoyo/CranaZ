package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_7_R3.EntityEnderman;
import net.minecraft.server.v1_7_R3.World;

public class UnspawnableEnderman extends EntityEnderman{
	public UnspawnableEnderman(World world) {
	    super(world);
    }
	@Override
	public boolean canSpawn() {
	    return false;
	}
}
