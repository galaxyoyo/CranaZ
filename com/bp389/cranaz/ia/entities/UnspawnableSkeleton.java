package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_7_R3.EntitySkeleton;
import net.minecraft.server.v1_7_R3.World;

public class UnspawnableSkeleton extends EntitySkeleton{
	public UnspawnableSkeleton(World world) {
	    super(world);
    }
	@Override
	public boolean canSpawn() {
	    return false;
	}
}
