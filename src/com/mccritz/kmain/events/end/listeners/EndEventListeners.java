package com.mccritz.kmain.events.end.listeners;


import com.mccritz.kmain.events.EventManager;
import com.mccritz.kmain.events.end.EndEvent;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.listeners.CombatLogListener;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.utils.Cooldowns;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EndEventListeners implements Listener {

    private kMain main = kMain.getInstance();
    private EventManager eventManager = main.getEventManager();
    private SpawnManager spawnManager = main.getSpawnManager();

    @EventHandler(ignoreCancelled = true)
    public void onPortal(PlayerPortalEvent e) {
        EndEvent endEvent = eventManager.getEndEvent();

        if (endEvent != null) {
            if (Cooldowns.getCooldown(e.getPlayer().getUniqueId(), "deathban") > 0) {
                if (Cooldowns.tryCooldown(e.getPlayer().getUniqueId(), "deathbanmessage", 3500)) {
                    MessageManager.message(e.getPlayer(), "&c[&5End Event&c] You cannot enter the end event while deathbanned.");
                }

                e.setCancelled(true);
                return;
            }

            if (endEvent.getSpawnLocation() != null && endEvent.isStarted()) {
                e.setTo(endEvent.getSpawnLocation());
                spawnManager.getSpawnProtected().remove(e.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        EndEvent endEvent = eventManager.getEndEvent();

        if (endEvent.isStarted() && p.getWorld().getEnvironment() == World.Environment.THE_END) {
            if (Cooldowns.tryCooldown(p.getUniqueId(), "deathban", 120000)) {
                p.setHealth(0.0);
                MessageManager.broadcast("&c[&5End Event&c] " + p.getName() + " 2:00");
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location location = e.getTo();
        EndEvent endEvent = eventManager.getEndEvent();

        if (endEvent != null) {
            if (location.getWorld().getEnvironment() == World.Environment.THE_END) {
                if (location.getBlock().isLiquid() && (location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER)) {
                    if (CombatLogListener.isInCombat(player.getUniqueId())) {
                        if (Cooldowns.tryCooldown(e.getPlayer().getUniqueId(), "combattagmesssage", 3500)) {
                            MessageManager.message(e.getPlayer(), "&c[&5End Event&c] You cannot leave the end while combat tagged!");
                            return;
                        }
                    }

                    if (endEvent.isCanLeaveEnd()) {
                        if (spawnManager.getSpawn() != null) {
                            e.setTo(new Location(Bukkit.getWorld("world"), 0.0, spawnManager.getSpawn().getStoneHeight(), 0.0));
                            spawnManager.getSpawnProtected().add(player.getUniqueId());
                        }
                    } else {
                        if (Cooldowns.tryCooldown(e.getPlayer().getUniqueId(), "spawnmessage", 3500)) {
                            MessageManager.message(e.getPlayer(), "&c[&5End Event&c] You cannot leave the end until the timer is up!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        EndEvent endEvent = eventManager.getEndEvent();

        if (endEvent != null) {
            if (p.getLocation().getWorld().getEnvironment() == World.Environment.THE_END) {
                if (Cooldowns.tryCooldown(p.getUniqueId(), "deathban", 120000)) {
                    MessageManager.broadcast("&c[&5End Event&c] " + p.getName() + " 2:00");
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if (p.getWorld().getEnvironment() == World.Environment.THE_END) {
            if (!p.hasPermission("endevent.edit")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if (p.getWorld().getEnvironment() == World.Environment.THE_END) {
            if (!p.hasPermission("endevent.edit")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getName().equalsIgnoreCase(eventManager.getEndEvent().getTier1Inventory().getName()) || e.getInventory().getName().equalsIgnoreCase(eventManager.getEndEvent().getTier2Inventory().getName()) || e.getInventory().getName().equalsIgnoreCase(eventManager.getEndEvent().getTier3Inventory().getName())) {
            eventManager.getEndEvent().saveData();
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        if (eventManager.getEndEvent() == null) {
            return;
        }

        if (e.getMessage().toLowerCase().contains("/team hq")
                || e.getMessage().toLowerCase().contains("/t hq")
                || e.getMessage().toLowerCase().contains("/team sethq")
                || e.getMessage().toLowerCase().contains("/team shq")
                || e.getMessage().toLowerCase().contains("/team seth")
                || e.getMessage().toLowerCase().contains("/t sethq")
                || e.getMessage().toLowerCase().contains("/t shq")
                || e.getMessage().toLowerCase().contains("/t seth")
                || e.getMessage().toLowerCase().contains("/team rally")
                || e.getMessage().toLowerCase().contains("/t rally")
                || e.getMessage().toLowerCase().contains("/t setrally")
                || e.getMessage().toLowerCase().contains("/team setrally")
                || e.getMessage().toLowerCase().contains("/team setr")
                || e.getMessage().toLowerCase().contains("/team sr")
                || e.getMessage().toLowerCase().contains("/t setr")
                || e.getMessage().toLowerCase().contains("/t sr")
                || e.getMessage().toLowerCase().contains("/home")
                || e.getMessage().toLowerCase().contains("/sethome")
                || e.getMessage().toLowerCase().contains("/go")
                || e.getMessage().toLowerCase().contains("/warp")
                || e.getMessage().toLowerCase().contains("/buy")
                || e.getMessage().toLowerCase().contains("/sell")
                || e.getMessage().toLowerCase().contains("/price")
                || e.getMessage().toLowerCase().contains("/withdraw")
                || e.getMessage().toLowerCase().contains("/deposit")) {
            if (p.getWorld().getEnvironment() == World.Environment.THE_END) {
                e.setCancelled(true);
                MessageManager.message(p, "&cThese commands are not allowed during the end event.");
            }
        }
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (e.getEntity().getWorld().getEnvironment() == World.Environment.THE_END) {
            e.setCancelled(true);
        }
    }
}
