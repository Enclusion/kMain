package com.mccritz.kmain.warps;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.listeners.TeleportationHandler;
import com.mccritz.kmain.utils.MessageManager;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Calvin on 4/25/2015. Project: Legacy
 */
@Getter
@Setter
public class Warp {

    private TeleportationHandler th = Legacy.getInstance().getTeleportationHandler();
    private UUID owner;
    private String name;
    private Location location;

    public Warp(UUID owner, String name, Location location) {
	this.owner = owner;
	this.name = name;
	this.location = location;
    }

    public void teleport(final Player p) {
	if (Legacy.getInstance().getSpawnManager().hasSpawnProt(p.getUniqueId())) {
	    MessageManager.sendMessage(p, "&7You cannot warp this close to the spawn.");
	    return;
	}

	if (location == null) {
	    MessageManager.sendMessage(p, "&7That warp location does not exist.");
	    return;
	}

	if (th.canTeleport(p)) {
	    location.getChunk().load(true);
	    p.teleport(location);
	    MessageManager.sendMessage(p, "&7You cannot attack for 10 seconds.");
	    return;
	}

	if (th.getTeleporters().containsKey(p.getUniqueId())) {
	    th.getTeleporters().get(p.getUniqueId()).cancel();
	}

	th.getTeleporters().put(p.getUniqueId(), new BukkitRunnable() {
	    @Override
	    public void run() {
		location.getChunk().load(true);
		p.teleport(location);
		th.getTeleporters().remove(p.getUniqueId());
		MessageManager.sendMessage(p, "&7You cannot attack for 10 seconds.");
	    }
	});

	th.getTeleporters().get(p.getUniqueId()).runTaskLaterAsynchronously(Legacy.getInstance(), 10 * 20);
	MessageManager.sendMessage(p, "&7Someone is nearby. Warping in 10 seconds. Do not move.");
    }
}