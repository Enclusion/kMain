package com.mccritz.kmain.listeners;

import com.google.common.collect.ImmutableSet;
import com.mccritz.kmain.commands.LogoutCommand;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatLogListener implements Listener {

    private SpawnManager sm = kMain.getInstance().getSpawnManager();
    private ArrayList<String> blockedCommands = new ArrayList<>();
    private static ConcurrentHashMap<UUID, Long> tagged = new ConcurrentHashMap<>();

    public CombatLogListener() {
        blockedCommands.add("/buy soup");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (LogoutCommand.getDontMove().containsKey(e.getPlayer().getUniqueId())) {
                    if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                        LogoutCommand.getDontMove().get(e.getPlayer().getUniqueId()).cancel();
                        LogoutCommand.getDontMove().remove(e.getPlayer().getUniqueId());
                        LogoutCommand.getCounter().get(e.getPlayer().getUniqueId()).cancel();
                        LogoutCommand.getCounter().remove(e.getPlayer().getUniqueId());
                        LogoutCommand.getCount().remove(e.getPlayer().getUniqueId());
                        MessageManager.message(e.getPlayer(), "&cYou moved. Logout cancelled.");
                    }
                }
            }
        }.runTaskAsynchronously(kMain.getInstance());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.hasBlock() && e.getClickedBlock().getType() == Material.ENDER_CHEST && isInCombat(p.getUniqueId())) {
                e.setCancelled(true);
                MessageManager.message(p, "&7You cannot open your enderchest during combat.");
            }
        }
    }

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

                    addTagged(tagger, kMain.getInstance().getConfig().getInt("combat.tag-length"));
                    addTagged(tagged, kMain.getInstance().getConfig().getInt("combat.tag-length"));
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
            p.setHealth(0.0);
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
                        MessageManager.message(p, "&7You cannot buy soup in combat.");
                        e.setCancelled(true);
                        return;
                    }
                } else if (disabledCommand.indexOf(" ") > 0) {
                    if (command.toLowerCase().startsWith(disabledCommand.toLowerCase())) {
                        MessageManager.message(p, "&7You cannot buy soup in combat.");
                        e.setCancelled(true);
                        return;
                    }
                } else if (!command.contains(" ") && command.equalsIgnoreCase(disabledCommand)) {
                    MessageManager.message(p, "&7You cannot buy soup in combat.");
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    public static long getRemainingTime(UUID id) {
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
            return tagged.remove(id);
        }

        return -1;
    }

    public long PvPTimeout(int seconds) {
        return System.currentTimeMillis() + (seconds * 1000);
    }

    public static boolean isInCombat(UUID id) {
        if (getRemainingTime(id) < 0) {
            if (tagged.containsKey(id)) {
                tagged.remove(id);
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