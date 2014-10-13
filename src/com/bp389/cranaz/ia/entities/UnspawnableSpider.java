package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_7_R3.EntitySpider;
import net.minecraft.server.v1_7_R3.World;

public class UnspawnableSpider extends EntitySpider{
	public UnspawnableSpider(World world) {
	    super(world);
    }
	@Override
	public boolean canSpawn() {
	    return false;
	}
}
