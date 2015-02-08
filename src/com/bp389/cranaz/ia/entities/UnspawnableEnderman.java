package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_8_R1.EntityEnderman;
import net.minecraft.server.v1_8_R1.World;

public class UnspawnableEnderman extends EntityEnderman {

	public UnspawnableEnderman(final World world) {
		super(world);
	}

	@Override
	public boolean bQ() {
		return false;
	}
}
