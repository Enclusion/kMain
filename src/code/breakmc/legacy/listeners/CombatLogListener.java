package code.breakmc.legacy.listeners;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.commands.Command_logout;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.utils.MessageManager;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Calvin on 5/9/2015.
 */
public class CombatLogListener implements Listener {

    public static HashMap<UUID, Villager> loggers = new HashMap<>();
    private Multimap<UUID, ItemStack[]> inventories = ArrayListMultimap.create();
    private HashMap<Villager, BukkitRunnable> villagerLog = new HashMap<>();

    @EventHandler
    public void onLogout(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Profile profile = Legacy.getInstance().getProfileManager().getProfile(p.getUniqueId());

        if (!profile.isSafeLogged() && !p.hasMetadata("logout") && !Legacy.getInstance().getSpawnManager().hasSpawnProt(p.getUniqueId())) {
            final Villager villager = (Villager) p.getLocation().getWorld().spawnCreature(p.getLocation(), EntityType.VILLAGER);
            villager.setAdult();
            villager.setMaxHealth(p.getMaxHealth());
            villager.setHealth(p.getHealth());
            villager.setCustomName(ChatColor.AQUA + p.getName());
            villager.setCustomNameVisible(true);
            villager.setFireTicks(p.getFireTicks());
            p.getActivePotionEffects().forEach(villager::addPotionEffect);
            villager.setCanPickupItems(false);
            villager.setMetadata("logger", new FixedMetadataValue(Legacy.getInstance(), p.getUniqueId().toString()));

            loggers.put(p.getUniqueId(), villager);
            inventories.get(p.getUniqueId()).add(p.getInventory().getContents());
            inventories.get(p.getUniqueId()).add(p.getInventory().getArmorContents());

            if (villagerLog.containsKey(villager)) {
                villagerLog.get(villager).cancel();
            }

            villagerLog.put(villager, new BukkitRunnable() {
                public void run() {
                    loggers.remove(villager.getMetadata("logger").get(0).value().toString());
                    villager.removeMetadata("logger", Legacy.getInstance());
                    villagerLog.remove(villager);
                    villager.remove();
                }
            });

            villagerLog.get(villager).runTaskLater(Legacy.getInstance(), 10 * 20L);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();

        Profile profile = Legacy.getInstance().getProfileManager().getProfile(p.getUniqueId());
        profile.setSafeLogged(false);

        if (loggers.containsKey(p.getUniqueId())) {
            if (loggers.get(p.getUniqueId()) != null && loggers.get(p.getUniqueId()).getHealth() != 0.0) {
                p.teleport(loggers.get(p.getUniqueId()).getLocation());
                p.setHealth(loggers.get(p.getUniqueId()).getHealth());
                p.setFireTicks(loggers.get(p.getUniqueId()).getFireTicks());
                villagerLog.remove(loggers.get(p.getUniqueId()));
                loggers.get(p.getUniqueId()).remove();
                loggers.remove(p.getUniqueId());
                inventories.removeAll(p.getUniqueId());
            } else {
                new BukkitRunnable() {
                    public void run() {
                        p.getInventory().clear();
                        p.getInventory().setHelmet(null);
                        p.getInventory().setChestplate(null);
                        p.getInventory().setLeggings(null);
                        p.getInventory().setBoots(null);
                        p.setHealth(0.0);
                    }
                }.runTaskLaterAsynchronously(Legacy.getInstance(), 5L);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Villager) {
            Villager villager = (Villager) e.getEntity();

            if (villager.hasMetadata("logger")) {
                UUID id = UUID.fromString(villager.getMetadata("logger").get(0).value().toString());

                if (loggers.containsKey(id) && loggers.get(id) == villager) {
                    for (ItemStack[] itemStacks : inventories.get(id)) {
                        for (ItemStack items : itemStacks) {
                            if (items != null && items.getType() != Material.AIR) {
                                villager.getLocation().getWorld().dropItemNaturally(villager.getLocation(), items);
                            }
                        }
                    }
                }

                villagerLog.remove(loggers.get(id));
            }
        }
    }

    @EventHandler
    public void onaMove(PlayerMoveEvent e) {
        if (Command_logout.getDontMove().containsKey(e.getPlayer().getUniqueId())) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                Command_logout.getDontMove().get(e.getPlayer().getUniqueId()).cancel();
                Command_logout.getDontMove().remove(e.getPlayer().getUniqueId());
                Command_logout.getCounter().get(e.getPlayer().getUniqueId()).cancel();
                Command_logout.getCounter().remove(e.getPlayer().getUniqueId());
                Command_logout.getCount().remove(e.getPlayer().getUniqueId());
                MessageManager.sendMessage(e.getPlayer(), "&cYou moved! Logout cancelled!");
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Villager) {
            Villager villager = (Villager) e.getRightClicked();

            if (villager.hasMetadata("logger")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onUnload(ChunkUnloadEvent e) {
        for (Entity ent : e.getChunk().getEntities()) {
            if (ent instanceof Villager) {
                Villager villager = (Villager) ent;

                if (villager.hasMetadata("logger")) {
                    villager.remove();
                }
            }
        }
    }
}