package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_8_R1.EntityCreeper;
import net.minecraft.server.v1_8_R1.World;

public class UnspawnableCreeper extends EntityCreeper {

	public UnspawnableCreeper(final World world) {
		super(world);
	}

	@Override
	public boolean bQ() {
		return false;
	}
}
