package com.bp389.cranaz.ia.entities;

import net.minecraft.server.v1_7_R3.EntityWitch;
import net.minecraft.server.v1_7_R3.World;

public class UnspawnableWitch extends EntityWitch{
	public UnspawnableWitch(World arg0) {
	    super(arg0);
    }
	@Override
	public boolean canSpawn() {
	    return false;
	}
}
