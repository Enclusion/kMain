package com.mccritz.kmain.profiles;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.listeners.TeleportationHandler;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Getter
@Setter
public class Profile {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private TeamManager tm = kMain.getInstance().getTeamManager();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private TeleportationHandler th = kMain.getInstance().getTeleportationHandler();

    private UUID uniqueId;
    private String name;
    private Double gold;
    private boolean safeLogged;
    private Location home;

    private long warpTime;

    public Profile(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void homeTeleport(final Player p) {
        if (kMain.getInstance().getSpawnManager().getSpawn().isInSpawnRadius(p.getLocation())) {
            MessageManager.message(p, "&7You cannot warp inside spawn.");
            return;
        }

        if (th.canTeleport(p)) {
            if (home == null) {
                MessageManager.message(p, "&7You do not have a home set.");
                return;
            }

            p.teleport(home);
            MessageManager.message(p, "&7You cannot attack for 10 seconds.");
        } else {
            if (th.getTeleporters().containsKey(p.getUniqueId())) {
                th.getTeleporters().get(p.getUniqueId()).cancel();
            }

            th.getTeleporters().put(p.getUniqueId(), new BukkitRunnable() {
                public void run() {
                    p.teleport(home);
                    MessageManager.message(p, "&7You have teleported to your &chome&7.");
                    th.getTeleporters().remove(p.getUniqueId());
                }
            });

            th.getTeleporters().get(p.getUniqueId()).runTaskLater(kMain.getInstance(), 10 * 20L);

            MessageManager.message(p, "&7Someone is nearby. Warping in 10 seconds. Do not move.");
        }
    }

    public void message(String message) {
        MessageManager.message(uniqueId, message);
    }
}
