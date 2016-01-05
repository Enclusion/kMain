package code.breakmc.legacy.warps;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Calvin on 4/25/2015.
 * Project: Legacy
 */
public class Warp implements Listener {

    UUID owner;
    String name;
    Location location;
    HashMap<UUID, BukkitRunnable> dontMove = new HashMap<>();

    public Warp(UUID owner, String name, Location location) {
        this.owner = owner;
        this.name = name;
        this.location = location;

        Bukkit.getServer().getPluginManager().registerEvents(this, Legacy.getInstance());
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void teleport(final Player p) {
        if (Legacy.getInstance().getSpawnManager().hasSpawnProt(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou cannot warp within spawn");
            return;
        }

        if (dontMove.containsKey(p.getUniqueId())) {
            dontMove.get(p.getUniqueId()).cancel();
        }

        dontMove.put(p.getUniqueId(), new BukkitRunnable() {
            public void run() {
                p.teleport(getLocation());
                dontMove.remove(p.getUniqueId());
                MessageManager.sendMessage(p, "&7You have warped to \"&b" + getName() + "\"");
            }
        });

        dontMove.get(p.getUniqueId()).runTaskLater(Legacy.getInstance(), 10 * 20);

        MessageManager.sendMessage(p, "&7Someone is nearby! Warping in 10 seconds, do not move!");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (dontMove.containsKey(e.getPlayer().getUniqueId())) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                dontMove.get(e.getPlayer().getUniqueId()).cancel();
                dontMove.remove(e.getPlayer().getUniqueId());
                MessageManager.sendMessage(e.getPlayer(), "&cYou moved! Teleportation cancelled.");
            }
        }
    }
}