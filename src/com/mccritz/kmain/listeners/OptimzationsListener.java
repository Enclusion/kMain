package com.mccritz.kmain.listeners;

import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mccritz.kmain.Legacy;

public class OptimzationsListener implements Listener {

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent e) {
	Player p = (Player) e.getEntity();

	if (e.getFoodLevel() < p.getFoodLevel()
		&& Legacy.getInstance().getSpawnManager().hasSpawnProt(p.getUniqueId())) {
	    e.setCancelled(true);
	    return;
	}

	if (e.getFoodLevel() < p.getFoodLevel() && new Random().nextInt(100) > 4) {
	    e.setCancelled(true);
	}
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
	e.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
	if (e.getInventory().getType() == InventoryType.BREWING) {
	    if (e.getSlot() == 0 || e.getSlot() == 1 || e.getSlot() == 2) {
		if (e.getClick() == ClickType.SHIFT_LEFT && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY
			|| e.getClick() == ClickType.SHIFT_RIGHT
				&& e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
		    e.setCancelled(true);
		}
	    }
	}
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
	if (event.getDamager() instanceof Player) {
	    Player player = (Player) event.getDamager();
	    if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
		for (PotionEffect effect : player.getActivePotionEffects()) {
		    if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
			int level = effect.getAmplifier() + 1;

			double newDamage = event.getDamage(EntityDamageEvent.DamageModifier.BASE)
				/ (level * 1.3D + 1.0D) + 0.1D * level;
			double damagePercent = newDamage / event.getDamage(EntityDamageEvent.DamageModifier.BASE);

			try {
			    event.setDamage(EntityDamageEvent.DamageModifier.ARMOR,
				    event.getDamage(EntityDamageEvent.DamageModifier.ARMOR) * damagePercent);
			} catch (Exception ignored) {
			}

			try {
			    event.setDamage(EntityDamageEvent.DamageModifier.MAGIC,
				    event.getDamage(EntityDamageEvent.DamageModifier.MAGIC) * damagePercent);
			} catch (Exception ignored) {
			}

			try {
			    event.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE,
				    event.getDamage(EntityDamageEvent.DamageModifier.RESISTANCE) * damagePercent);
			} catch (Exception ignored) {
			}

			try {
			    event.setDamage(EntityDamageEvent.DamageModifier.BLOCKING,
				    event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) * damagePercent);
			} catch (Exception ignored) {
			}

			event.setDamage(EntityDamageEvent.DamageModifier.BASE, newDamage);
			break;
		    }
		}
	    }
	}
    }

    @EventHandler
    public void onEntityDeathEvent(final EntityDeathEvent event) {
	if (event.getEntity().getKiller() != null) {
	    if (event.getEntity().getKiller().getInventory().getItemInHand()
		    .getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) != 0) {
		int lootingLevel = event.getEntity().getKiller().getInventory().getItemInHand()
			.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
		double droppedXp = event.getDroppedExp();
		droppedXp *= 1.0 + lootingLevel / 5.0 + Math.pow(lootingLevel, 2.0) / 10.0
			+ (Math.random() * 0.5 - 0.2);
		droppedXp *= 0.8;
		event.setDroppedExp((int) droppedXp);
	    }
	}
    }

    @EventHandler
    public void onBlockBreakEvent(final BlockBreakEvent event) {
	if (event.getPlayer() != null) {
	    int fortuneLevel = event.getPlayer().getInventory().getItemInHand()
		    .getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
	    double droppedXp = event.getExpToDrop();
	    droppedXp *= 1.0 + fortuneLevel / 5.0 + Math.pow(fortuneLevel, 2.0) / 10.0 + (Math.random() * 0.5 - 0.2);
	    droppedXp *= 0.8;
	    event.setExpToDrop((int) droppedXp);
	}
    }
}
