package com.mccritz.kmain.listeners;

import com.mccritz.kmain.commands.ToggleDMCommand;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class OptimzationsListener implements Listener {

    private HashMap<Block, Integer> furnaces = new HashMap<>();
    private ArrayList<Block> blockList = new ArrayList<>();

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();

        if (e.getFoodLevel() < p.getFoodLevel() && kMain.getInstance().getSpawnManager().hasSpawnProt(p.getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        if ((e.getFoodLevel() < p.getFoodLevel()) && (new Random().nextInt(100) > 4)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRain(WeatherChangeEvent e) {
        World w = e.getWorld();

        if (!w.hasStorm()) {
            e.setCancelled(true);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (w.hasStorm()) {
                    w.setStorm(false);
                }
            }
        }.runTaskLater(kMain.getInstance(), 5L);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.hasBlock()) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType() == Material.HOPPER || e.getClickedBlock().getType() == Material.ENDER_CHEST) {
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

                        double newDamage = event.getDamage(EntityDamageEvent.DamageModifier.BASE) / (level * 1.3D + 1.0D) + 0.1D * level;
                        double damagePercent = newDamage / event.getDamage(EntityDamageEvent.DamageModifier.BASE);

                        try {
                            event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, event.getDamage(EntityDamageEvent.DamageModifier.ARMOR) * damagePercent);
                        } catch (Exception ignored) {
                        }

                        try {
                            event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, event.getDamage(EntityDamageEvent.DamageModifier.MAGIC) * damagePercent);
                        } catch (Exception ignored) {
                        }

                        try {
                            event.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE, event.getDamage(EntityDamageEvent.DamageModifier.RESISTANCE) * damagePercent);
                        } catch (Exception ignored) {
                        }

                        try {
                            event.setDamage(EntityDamageEvent.DamageModifier.BLOCKING, event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) * damagePercent);
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
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            if (event.getEntity().getKiller().getInventory().getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) != 0) {
                int lootingLevel = event.getEntity().getKiller().getInventory().getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
                double droppedXp = event.getDroppedExp();
                droppedXp *= 1.0 + lootingLevel / 5.0 + Math.pow(lootingLevel, 2.0) / 10.0 + (Math.random() * 0.5 - 0.2);
                droppedXp *= 0.8;
                event.setDroppedExp((int) droppedXp);
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (event.getPlayer() != null) {
            int fortuneLevel = event.getPlayer().getInventory().getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            double droppedXp = event.getExpToDrop();
            droppedXp *= 1.0 + fortuneLevel / 5.0 + Math.pow(fortuneLevel, 2.0) / 10.0 + (Math.random() * 0.5 - 0.2);
            droppedXp *= 0.8;
            event.setExpToDrop((int) droppedXp);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        String deathMessage = e.getDeathMessage();
        e.setDeathMessage(null);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : PlayerUtility.getOnlinePlayers()) {
                    if (!ToggleDMCommand.getToggled().contains(all.getUniqueId())) {
                        MessageManager.message(all, ChatColor.stripColor(deathMessage));
                    }
                }
            }
        }.runTaskAsynchronously(kMain.getInstance());
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        new BukkitRunnable() {
            public void run() {
                if (e.getEntity().getItemStack().getType() == Material.BOWL || e.getEntity().getItemStack().getType() == Material.MUSHROOM_SOUP || e.getEntity().getItemStack().getType() == Material.GLASS_BOTTLE || e.getEntity().getItemStack().getType() == Material.SAND || e.getEntity().getItemStack().getType() == Material.GRAVEL || e.getEntity().getItemStack().getType() == Material.DIRT || e.getEntity().getItemStack().getType() == Material.COBBLESTONE || e.getEntity().getItemStack().getType() == Material.ROTTEN_FLESH || e.getEntity().getItemStack().getType() == Material.STRING || e.getEntity().getItemStack().getType() == Material.BONE || e.getEntity().getItemStack().getType() == Material.ARROW || e.getEntity().getItemStack().getType() == Material.NETHERRACK) {
                    e.getEntity().remove();
                }
            }
        }.runTaskLater(kMain.getInstance(), 20 * 20L);
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent e) {
        Furnace furnace = (Furnace) e.getBlock().getState();
        Location location = furnace.getLocation();

        for (Entity entity : getNearbyEntities(location, 5)) {
            if (entity instanceof Player) {
                Player p = (Player) entity;

                if (!(p.getOpenInventory().getTopInventory().getType() == InventoryType.FURNACE)) {
                    return;
                }

                furnace.setCookTime((short) 100);
            }
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent e) {
        Furnace furnace = (Furnace) e.getBlock().getState();
        Location location = furnace.getLocation();

        for (Entity entity : getNearbyEntities(location, 5)) {
            if (entity instanceof Player) {
                Player p = (Player) entity;

                if (!(p.getOpenInventory().getTopInventory().getType() == InventoryType.FURNACE)) {
                    return;
                }

                furnace.setCookTime((short) 100);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        try {
            Block block = e.getWhoClicked().getTargetBlock(null, 10);

            if (block != null) {
                if ((block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE) && (e.getSlot() == 0 || e.getSlot() == 1) && e.getCursor().getType() != Material.AIR) {
                    Furnace furnace = (Furnace) block.getState();
                    furnace.setCookTime((short) 100);
                }
            }
        } catch (Exception ignored) {}
    }

    public List<Entity> getNearbyEntities(Location l, int size) {
        return l.getWorld().getEntities().stream().filter(e -> l.distance(e.getLocation()) <= size).collect(Collectors.toList());
    }
}
