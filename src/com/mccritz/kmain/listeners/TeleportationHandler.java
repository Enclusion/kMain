package com.mccritz.kmain.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.mccritz.kbasic.util.ProfileUtils;
import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;

public class TeleportationHandler implements Listener {

    private HashMap<UUID, BukkitRunnable> teleporters = new HashMap<>();

    public TeleportationHandler() {
	Bukkit.getPluginManager().registerEvents(this, Legacy.getInstance());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
	if (teleporters.containsKey(e.getPlayer().getUniqueId())) {
	    if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY()
		    || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
		teleporters.get(e.getPlayer().getUniqueId()).cancel();
		teleporters.remove(e.getPlayer().getUniqueId());
		MessageManager.sendMessage(e.getPlayer(), "&7You moved! Teleportation cancelled.");
		return;
	    }
	}

	double x = Math.floor(e.getFrom().getX());
	double y = Math.floor(e.getFrom().getY());
	double z = Math.floor(e.getFrom().getZ());

	if (Math.floor(e.getTo().getX()) != x || Math.floor(e.getTo().getY()) != y
		|| Math.floor(e.getTo().getZ()) != z) {
	    Profile profile = Legacy.getInstance().getProfileManager().getProfile(e.getPlayer().getUniqueId());

	    if (profile.getTeleportRunnable() != null && !profile.getTeleportRunnable().isCancelled()) {
		profile.getTeleportRunnable().cancel();
		profile.setTeleportRunnable(null);
		MessageManager.sendMessage(e.getPlayer(), "&7You moved! Teleportation cancelled.");
	    }
	}
    }

    @EventHandler
    public void onPlayerDamageTeleport(EntityDamageEvent e) {
	if (e.getEntity() instanceof Player) {
	    Player player = (Player) e.getEntity();
	    Profile profile = Legacy.getInstance().getProfileManager().getProfile(player.getUniqueId());

	    if (profile.getTeleportRunnable() != null && !profile.getTeleportRunnable().isCancelled()) {
		profile.getTeleportRunnable().cancel();
		profile.setTeleportRunnable(null);
		MessageManager.sendMessage(player, "&7You moved! Teleportation cancelled.");
	    }
	}
    }

    public boolean canTeleport(Player p) {
	TeamManager tm = Legacy.getInstance().getTeamManager();
	boolean canTeleport = true;

	for (Entity ent : p.getNearbyEntities(40, 20, 40)) {
	    if (ent instanceof Player) {
		Player near = (Player) ent;

		if (near.equals(p)) {
		    continue;
		}

		if (ProfileUtils.getInstance().getProfile(near.getUniqueId()).isVanished())
		    continue;

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
