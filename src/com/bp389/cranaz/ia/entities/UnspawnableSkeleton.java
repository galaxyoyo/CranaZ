package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.World;

public class UnspawnableSkeleton extends EntitySkeleton {

	public UnspawnableSkeleton(final World world) {
		super(world);
	}

	@Override
	public boolean bQ() {
		return false;
	}
}
