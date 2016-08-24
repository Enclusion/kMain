package com.mccritz.kmain.spawn;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.listeners.TeleportationHandler;
import com.mccritz.kmain.utils.MessageManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class Spawn {

    private TeleportationHandler th = kMain.getInstance().getTeleportationHandler();
    private int radius;
    private int height;
    private int stoneRadius;
    private int stoneHeight;

    public Spawn() {
        Bukkit.getServer().getWorld("world").setSpawnLocation(0, height, 0);
    }

    public boolean isInSpawnRadius(Location location) {
        return location.getWorld().getEnvironment() == World.Environment.NORMAL && (location.getBlockX() < radius + 1) && (location.getBlockX() > -radius - 1) && (location.getBlockZ() < radius + 1) && (location.getBlockZ() >= -radius - 1);
    }

    public boolean isInStoneRadius(Location location) {
        return location.getWorld().getEnvironment() == World.Environment.NORMAL && (location.getBlockX() < stoneRadius + 1) && (location.getBlockX() > -stoneRadius - 1) && (location.getBlockZ() < stoneRadius + 1) && (location.getBlockZ() >= -stoneRadius - 1);
    }

    public boolean isIn512Radius(Location location) {
        return location.getWorld().getEnvironment() == World.Environment.NORMAL && (location.getBlockX() < 512 + 1) && (location.getBlockX() > -512 - 1) && (location.getBlockZ() < 512 + 1) && (location.getBlockZ() >= -512 - 1);
    }

    public boolean isInNetherRadius(Location location) {
        return location.getWorld().getEnvironment() != World.Environment.THE_END && location.getWorld().getEnvironment() != World.Environment.NORMAL && (location.getBlockX() < 30 + 1) && (location.getBlockX() > -30 - 1) && (location.getBlockZ() < 30 + 1) && (location.getBlockZ() >= -30 - 1);
    }

    public void spawnTeleport(final Player p) {
        if (th.canTeleport(p)) {
            p.teleport(new Location(Bukkit.getWorld("world"), 0.5, getHeight(), 0.5));

            MessageManager.message(p, "&7You cannot attack for 10 seconds.");

            kMain.getInstance().getSpawnManager().getSpawnProtected().add(p.getUniqueId());
        } else {
            if (th.getTeleporters().containsKey(p.getUniqueId())) {
                th.getTeleporters().get(p.getUniqueId()).cancel();
            }

            th.getTeleporters().put(p.getUniqueId(), new BukkitRunnable() {
                public void run() {
                    p.teleport(new Location(Bukkit.getWorld("world"), 0.5, getHeight(), 0.5));

                    MessageManager.message(p, "&7You cannot attack for 10 seconds.");
                    kMain.getInstance().getSpawnManager().getSpawnProtected().add(p.getUniqueId());
                    th.getTeleporters().remove(p.getUniqueId());
                }
            });

            th.getTeleporters().get(p.getUniqueId()).runTaskLater(kMain.getInstance(), 10 * 20L);
            MessageManager.message(p, "&7Someone is nearby. Warping in 10 seconds. Do not move.");
        }
    }
}
