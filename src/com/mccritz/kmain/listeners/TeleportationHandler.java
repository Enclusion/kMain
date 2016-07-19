package com.mccritz.kmain.listeners;

import com.mccritz.kbasic.util.ProfileUtils;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class TeleportationHandler implements Listener {

    private HashMap<UUID, BukkitRunnable> teleporters = new HashMap<>();

    public TeleportationHandler() {
        Bukkit.getPluginManager().registerEvents(this, kMain.getInstance());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        double x = Math.floor(e.getFrom().getX());
        double y = Math.floor(e.getFrom().getY());
        double z = Math.floor(e.getFrom().getZ());

        if (Math.floor(e.getTo().getX()) != x || Math.floor(e.getTo().getY()) != y || Math.floor(e.getTo().getZ()) != z) {
            if (teleporters.containsKey(e.getPlayer().getUniqueId())) {
                teleporters.get(e.getPlayer().getUniqueId()).cancel();
                teleporters.remove(e.getPlayer().getUniqueId());
                MessageManager.sendMessage(e.getPlayer(), "&7You moved! Teleportation cancelled.");
            }
        }
    }

    @EventHandler
    public void onPlayerDamageTeleport(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (teleporters.containsKey(player.getUniqueId())) {
                teleporters.get(player.getUniqueId()).cancel();
                teleporters.remove(player.getUniqueId());
                MessageManager.sendMessage(player, "&7You moved! Teleportation cancelled.");
            }
        }
    }

    public boolean canTeleport(Player p) {
        TeamManager tm = kMain.getInstance().getTeamManager();
        boolean canTeleport = true;

        for (Entity ent : p.getNearbyEntities(40, 20, 40)) {
            if (ent instanceof Player) {
                Player near = (Player) ent;

                if (near.equals(p)) continue;

                if (ProfileUtils.getInstance().getProfile(near.getUniqueId()).isVanished()) continue;

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

    public HashMap<UUID, BukkitRunnable> getTeleporters() {
        return teleporters;
    }
}
