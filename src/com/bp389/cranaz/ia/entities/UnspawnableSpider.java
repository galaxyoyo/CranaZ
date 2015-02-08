package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_8_R1.EntitySpider;
import net.minecraft.server.v1_8_R1.World;

public class UnspawnableSpider extends EntitySpider {

	public UnspawnableSpider(final World world) {
		super(world);
	}

	@Override
	public boolean bQ() {
		return false;
	}
}
