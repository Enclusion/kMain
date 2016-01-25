package code.breakmc.legacy.listeners;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.commands.Command_logout;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.spawn.SpawnManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.PlayerUtility;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Calvin on 5/9/2015.
 */
public class CombatLogListener implements Listener {

    private SpawnManager sm = Legacy.getInstance().getSpawnManager();
    public static HashMap<UUID, Villager> loggers = new HashMap<>();
    private Multimap<UUID, ItemStack[]> inventories = ArrayListMultimap.create();
    private HashMap<Villager, BukkitRunnable> villagerLog = new HashMap<>();
    private ArrayList<String> blockedCommands = new ArrayList<>();

    public CombatLogListener() {
        blockedCommands.add("/buy soup");
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Profile profile = Legacy.getInstance().getProfileManager().getProfile(p.getUniqueId());

        if (p.getGameMode() == GameMode.SURVIVAL) {
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

    //30 Second Cooldown

    private ConcurrentHashMap<UUID, Long> tagged = new ConcurrentHashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || (PlayerUtility.getDamage(e) == 0)) {
            return;
        }

        Entity damager = e.getDamager();

        if (damager instanceof Projectile) {
            if (((Projectile) damager).getShooter() != null) {
                damager = ((Projectile) damager).getShooter();
            }
        }

        if (e.getEntity() instanceof Player) {
            Player tagged = (Player) e.getEntity();

            if (sm.hasSpawnProt(tagged.getUniqueId())) {
                return;
            }

            if (damager instanceof Player) {
                Player tagger = (Player) damager;

                if (tagger != tagged) {
                    if (sm.hasSpawnProt(tagger.getUniqueId())) {
                        return;
                    }

                    addTagged(tagger, 30);
                    addTagged(tagged, 30);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        removeTagged(e.getEntity().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (p.isDead() || PlayerUtility.getHealth(p) <= 0) {
            removeTagged(p.getUniqueId());
            return;
        }

        if (isInCombat(p.getUniqueId())) {
            removeTagged(p.getUniqueId());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        removeTagged(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (isInCombat(p.getUniqueId())) {
            String command = e.getMessage();
            for (String disabledCommand : blockedCommands) {
                if (command.indexOf(" ") == disabledCommand.length()) {
                    if (command.substring(0, command.indexOf(" ")).equalsIgnoreCase(disabledCommand)) {
                        MessageManager.sendMessage(p, "&cYou cannot buy soup in combat!");
                        e.setCancelled(true);
                        return;
                    }
                } else if (disabledCommand.indexOf(" ") > 0) {
                    if (command.toLowerCase().startsWith(disabledCommand.toLowerCase())) {
                        MessageManager.sendMessage(p, "&cYou cannot buy soup in combat!");
                        e.setCancelled(true);
                        return;
                    }
                } else if (!command.contains(" ") && command.equalsIgnoreCase(disabledCommand)) {
                    MessageManager.sendMessage(p, "&cYou cannot buy soup in combat!");
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    public long getRemainingTime(UUID id) {
        Long tag = tagged.get(id);

        if (tag == null) {
            return -1;
        }

        return tag - System.currentTimeMillis();
    }

    public boolean addTagged(Player p, int seconds) {
        if (p.isOnline()) {
            tagged.put(p.getUniqueId(), PvPTimeout(seconds));
            return true;
        }

        return false;
    }

    public long removeTagged(UUID id) {
        if (isInCombat(id)) {
            MessageManager.sendMessage(id, "&7You are no longer in combat");
            return tagged.remove(id);
        }

        return -1;
    }

    public long PvPTimeout(int seconds) {
        return System.currentTimeMillis() + (seconds * 1000);
    }

    public boolean isInCombat(UUID id) {
        if (getRemainingTime(id) < 0) {
            if (tagged.contains(id)) {
                tagged.remove(id);
                MessageManager.sendMessage(id, "&7You are no longer in combat");
            }
            return false;
        } else {
            return true;
        }
    }

    public static String formatTime(long difference) {
        Calendar call = Calendar.getInstance();
        call.setTimeInMillis(difference);
        return new SimpleDateFormat("ss.S").format(difference);
    }

    public ImmutableSet<UUID> listTagged() {
        return ImmutableSet.copyOf(tagged.keySet());
    }
}