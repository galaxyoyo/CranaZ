package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_8_R1.EntitySlime;
import net.minecraft.server.v1_8_R1.World;

public class UnspawnableSlime extends EntitySlime {

	public UnspawnableSlime(final World world) {
		super(world);
	}

	@Override
	public boolean bQ() {
		return false;
	}
}
