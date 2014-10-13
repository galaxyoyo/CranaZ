package com.bp389.cranaz.loots;

import net.minecraft.server.v1_7_R3.EntityHuman;
import net.minecraft.server.v1_7_R3.EntityItem;
import net.minecraft.server.v1_7_R3.ItemStack;
import net.minecraft.server.v1_7_R3.World;

/**
 * Représente une entité objet non recupérable et non stackable
 * @author BlackPhantom
 *
 */
public class UnpickableItem extends EntityItem {
	public UnpickableItem(World world, double d0, double d1, double d2, ItemStack itemstack) {
	    super(world, d0, d1, d2, itemstack);
    }
	@Override
	public void h() {
	}
	@Override
	public void b_(EntityHuman arg0) {}
}
