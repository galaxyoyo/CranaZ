package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_8_R1.EntityWitch;
import net.minecraft.server.v1_8_R1.World;

public class UnspawnableWitch extends EntityWitch {

	public UnspawnableWitch(final World arg0) {
		super(arg0);
	}

	@Override
	public boolean bQ() {
		return false;
	}
}
