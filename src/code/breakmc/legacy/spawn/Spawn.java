package code.breakmc.legacy.spawn;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.MessageManager;
import com.breakmc.pure.Pure;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class Spawn {

    private HashMap<UUID, BukkitRunnable> dontMove = new HashMap<>();
    private int radius;
    private int height;
    private int stoneRadius;
    private int stoneHeight;

    public Spawn(int radius, int height, int stoneRadius, int stoneHeight) {
        this.radius = radius;
        this.height = height;
        this.stoneRadius = stoneRadius;
        this.stoneHeight = stoneHeight;

        Bukkit.getServer().getWorld("world").setSpawnLocation(0, height, 0);
    }

    public boolean isInSpawnRadius(Location location) {
        return location.getWorld().getEnvironment() == World.Environment.NORMAL && (location.getBlockX() < radius + 1) && (location.getBlockX() > -radius - 1) && (location.getBlockZ() < radius + 1) && (location.getBlockZ() >= -radius - 1);
    }

    public boolean isInStoneRadius(Location location) {
        return location.getWorld().getEnvironment() == World.Environment.NORMAL && (location.getBlockX() < stoneRadius + 1) && (location.getBlockX() > -stoneRadius - 1) && (location.getBlockZ() < stoneRadius + 1) && (location.getBlockZ() >= -stoneRadius - 1);
    }

    public boolean isInNetherRadius(Location location) {
        return location.getWorld().getEnvironment() != World.Environment.THE_END && location.getWorld().getEnvironment() != World.Environment.NORMAL && (location.getBlockX() < 30 + 1) && (location.getBlockX() > -30 - 1) && (location.getBlockZ() < 30 + 1) && (location.getBlockZ() >= -30 - 1);
    }

    public HashMap<UUID, BukkitRunnable> getDontMove() {
        return dontMove;
    }

    public void spawnTeleport(final Player p) {
        if (canTeleport(p)) {
            p.teleport(new Location(Bukkit.getWorld("world"), 0.5, getHeight(), 0.5));

            MessageManager.sendMessage(p, "&7You have regained spawn protection!");

            Legacy.getInstance().getSpawnManager().getSpawnProtected().add(p.getUniqueId());
        } else {
            if (dontMove.containsKey(p.getUniqueId())) {
                dontMove.get(p.getUniqueId()).cancel();
            }

            dontMove.put(p.getUniqueId(), new BukkitRunnable() {
                public void run() {
                    p.teleport(new Location(Bukkit.getWorld("world"), 0.5, getHeight(), 0.5));

                    MessageManager.sendMessage(p, "&7You have regained spawn protection!");
                    Legacy.getInstance().getSpawnManager().getSpawnProtected().add(p.getUniqueId());
                    dontMove.remove(p.getUniqueId());
                }
            });

            dontMove.get(p.getUniqueId()).runTaskLater(Legacy.getInstance(), 10 * 20L);
            MessageManager.sendMessage(p, "&cSomeone is nearby! Warping in 10 seconds! Do not move!");
        }
    }

    public boolean canTeleport(Player p) {
        TeamManager tm = Legacy.getInstance().getTeamManager();
        boolean canTeleport = true;
        for (Entity ent : p.getNearbyEntities(40, 20, 40)) {
            if (ent instanceof Player) {
                Player near = (Player) ent;

                if (near.equals(p)) continue;

                if (Pure.getInstance().getPunishmentManager().isVanished(near)) continue;

                if (tm.getTeam(near.getUniqueId()) != null && tm.getTeam(p.getUniqueId()) != null) {
                    if (!tm.getTeam(p.getUniqueId()).equals(tm.getTeam(near.getUniqueId()))) {
                        canTeleport = false;
                    }
                } else {
                    canTeleport = false;
                }
            }
        }

        return canTeleport;
    }
}
