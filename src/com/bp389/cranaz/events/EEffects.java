package com.bp389.cranaz.events;

import java.awt.image.BufferedImage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.effects.Bleed;
import com.bp389.cranaz.effects.Effects;
import com.bp389.cranaz.effects.WeaponAim;
import com.bp389.cranaz.items.Items;
import com.shampaggon.crackshot.events.WeaponShootEvent;

import javax.imageio.ImageIO;

public final class EEffects extends GEvent implements Listener {

	public EEffects(final JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	public Class<? extends Loadable> getRelativePlugin() {
		return Effects.class;
	}

	/*
	 * 
	 */

	@EventHandler
	public void playerBleed(final EntityDamageEvent e) {
		if(e.getEntityType() == EntityType.PLAYER)
			// Player p = (Player)e.getEntity();
			// Damageable d = (Damageable)p;
			// double d0 = d.getHealth() - e.getDamage();
			/*
			 * if(rh.isHemoragic(p)){ e.setCancelled(true); return; }
			 */
			if(e.getEntity() instanceof LivingEntity) {
				boolean b = false;
				switch(e.getCause()) {
					case ENTITY_ATTACK:
						b = true;
						break;
					case CONTACT:
						b = true;
						break;
					case FALL:
						b = true;
						break;
					case THORNS:
						b = true;
						break;
					case PROJECTILE:
						b = true;
						break;
					case BLOCK_EXPLOSION:
						b = true;
						break;
					case ENTITY_EXPLOSION:
						b = true;
						break;
				}
				if(b)
					Bleed.bleedEffect((LivingEntity) e.getEntity());
			}
		/*
		 * if(d0 <= 0D){ e.setCancelled(true);
		 * rh.hemoragize((Player)e.getEntity()); return; } else if(d0 < 9D){
		 * if(!BloodCycle.ps.contains(p)) new BloodCycle(p).runTaskTimer(jp, 0L,
		 * 8L * 20L); }
		 */
	}

	@EventHandler
	public void weaponAim(final WeaponShootEvent e) {
		WeaponAim.handleAim(e.getWeaponTitle(), e.getPlayer());
	}

	@EventHandler
	public void playerAchieved(final EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof LivingEntity)
			Bleed.bleedEffect((LivingEntity) e.getEntity());
	}

	@EventHandler
	public void playerPing(final ServerListPingEvent e) {
		e.setMotd("§r§l§1CranaZ §kPost-apocalyptique zombi.\n§r§4§lVenez §r§4§mmourir§r §4§lSURVIVRE !");
		try {
			final BufferedImage bi = ImageIO.read(EEffects.class.getResource("cranaz.png"));
			e.setServerIcon(Bukkit.getServer().loadServerIcon(bi));
		} catch(final Exception e1) {}
	}

	@EventHandler
	public void playerInt(final PlayerInteractEntityEvent e) {
		// rh.hemoragize(e.getPlayer());
		if(e.getRightClicked().getType() != EntityType.PLAYER)
			return;
		final Player p = (Player) e.getRightClicked();
		if(e.getPlayer().getItemInHand().getType() == Material.PUMPKIN_PIE || e.getPlayer().getItemInHand().getType() == Material.APPLE
		        || e.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE || e.getPlayer().getItemInHand().getType() == Material.PAPER)
			if(Items.Subs.Poison.NEUROTOXIC.compareTo(e.getPlayer().getItemInHand())) {
				Bleed.neurtoxicPoison(p);
				if(e.getPlayer().getItemInHand().getAmount() == 1)
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			} else if(Items.Subs.Drugs.AMPHETAMIN.compareTo(e.getPlayer().getItemInHand())) {
				Bleed.amphet(p);
				if(e.getPlayer().getItemInHand().getAmount() == 1)
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			} else if(Items.Subs.Poison.ARTERIAL.compareTo(e.getPlayer().getItemInHand())) {
				Bleed.arterialPoison(p);
				if(e.getPlayer().getItemInHand().getAmount() == 1)
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			} else if(e.getPlayer().getItemInHand().getType() == Material.APPLE) {
				/*
				 * if(rh.isHemoragic(p)){ rh.revive(p, Material.APPLE);
				 * e.getPlayer().sendMessage("§r§bVous avez réanimé " +
				 * p.getDisplayName() + ".§r");
				 * if(e.getPlayer().getItemInHand().getAmount() == 1){
				 * e.getPlayer().getInventory().setItemInHand(new
				 * ItemStack(Material.AIR, 1)); } else
				 * e.getPlayer().getItemInHand
				 * ().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				 * return; }
				 */
				Bleed.blood(0, p);
				if(e.getPlayer().getItemInHand().getAmount() == 1)
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			} else if(e.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE) {
				/*
				 * if(rh.isHemoragic(p)){ rh.revive(p, Material.GOLDEN_APPLE);
				 * e.getPlayer().sendMessage("§r§bVous avez réanimé " +
				 * p.getDisplayName() + ".§r");
				 * if(e.getPlayer().getItemInHand().getAmount() == 1){
				 * e.getPlayer().getInventory().setItemInHand(new
				 * ItemStack(Material.AIR, 1)); } else
				 * e.getPlayer().getItemInHand
				 * ().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				 * return; }
				 */
				Bleed.blood(1, p);
				if(e.getPlayer().getItemInHand().getAmount() == 1)
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			} else if(e.getPlayer().getItemInHand().getType() == Material.PAPER) {
				Bleed.bandages(p);
				if(e.getPlayer().getItemInHand().getAmount() == 1)
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			}
	}

	@EventHandler
	public void playerDrug(final PlayerInteractEvent e) {
		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
			if(e.getPlayer().getItemInHand().getType() == Material.PUMPKIN_PIE || e.getPlayer().getItemInHand().getType() == Material.APPLE
			        || e.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE || e.getPlayer().getItemInHand().getType() == Material.PAPER)
				if(Items.Subs.Drugs.AMPHETAMIN.compareTo(e.getPlayer().getItemInHand())) {
					Bleed.amphet(e.getPlayer());
					if(e.getPlayer().getItemInHand().getAmount() == 1)
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				} else if(e.getPlayer().getItemInHand().getType() == Material.RED_MUSHROOM) {
					Bleed.mush(e.getPlayer());
					if(e.getPlayer().getItemInHand().getAmount() == 1)
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				} else if(e.getPlayer().getItemInHand().getType() == Material.APPLE) {
					Bleed.blood(0, e.getPlayer());
					if(e.getPlayer().getItemInHand().getAmount() == 1)
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				} else if(e.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE) {
					Bleed.blood(1, e.getPlayer());
					if(e.getPlayer().getItemInHand().getAmount() == 1)
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				} else if(e.getPlayer().getItemInHand().getType() == Material.PAPER) {
					Bleed.bandages(e.getPlayer());
					if(e.getPlayer().getItemInHand().getAmount() == 1)
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				}
	}
}
