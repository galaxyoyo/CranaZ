package com.bp389.cranaz.loots;

import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityItem;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.World;

/**
 * Repr�sente une entit� objet non recup�rable et non stackable
 * @author BlackPhantom
 *
 */
public final class UnpickableItem extends EntityItem {
	public UnpickableItem(World world, double d0, double d1, double d2, ItemStack itemstack) {
	    super(world, d0, d1, d2, itemstack);
    }
	@Override
	public void h() {}
	@Override
	public void d(EntityHuman arg0) {}
}
