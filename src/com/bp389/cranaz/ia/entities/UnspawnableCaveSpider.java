package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_8_R1.EntityCaveSpider;
import net.minecraft.server.v1_8_R1.World;

public class UnspawnableCaveSpider extends EntityCaveSpider{
	public UnspawnableCaveSpider(World arg0) {
	    super(arg0);
    }
	@Override
	public boolean canSpawn() {
	    return false;
	}
}