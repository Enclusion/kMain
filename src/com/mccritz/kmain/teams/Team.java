package com.mccritz.kmain.teams;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.listeners.TeleportationHandler;
import com.mccritz.kmain.utils.MessageManager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Team {

    private TeleportationHandler th = Legacy.getInstance().getTeleportationHandler();
    private String name;
    private Set<UUID> managers;
    private Set<UUID> members;
    private Location hq;
    private Location rally;
    private boolean friendlyFireEnabled;
    private String password;

    public Team(String name) {
	this.name = name;
	this.managers = new HashSet<>();
	this.members = new HashSet<>();
	this.hq = null;
	this.rally = null;
	this.friendlyFireEnabled = false;
    }

    public boolean isManager(UUID id) {
	return managers.contains(id);
    }

    public void sendMessage(String message) {
	for (UUID id : getManagers()) {
	    MessageManager.sendMessage(id, message);
	}
	for (UUID id : getMembers()) {
	    MessageManager.sendMessage(id, message);
	}
    }

    public List<Player> getOnlinePlayers() {
	List<Player> players = new ArrayList<>();

	for (UUID id : managers) {
	    if (Bukkit.getPlayer(id) != null) {
		players.add(Bukkit.getPlayer(id));
	    }
	}

	for (UUID id : members) {
	    if (Bukkit.getPlayer(id) != null) {
		players.add(Bukkit.getPlayer(id));
	    }
	}

	return players;
    }

    public void getExtraInformation(UUID id) {
	MessageManager.sendMessage(id, "&7*** &3" + getName() + " &7***");
	MessageManager.sendMessage(id,
		"&7Password: " + (!getPassword().equalsIgnoreCase("") ? "&3" + getPassword() : "&3Unset"));
	MessageManager.sendMessage(id, "&7Team HQ: " + (getHq() != null ? "&3Set" : "&3Unset"));
	MessageManager.sendMessage(id, "&7Team Rally: " + (getRally() != null ? "&3Set" : "&3Unset"));
	MessageManager.sendMessage(id, "&7Friendly Fire: " + (!isFriendlyFireEnabled() ? "&3Off" : "&3On"));
	MessageManager.sendMessage(id, "&3Members &7(" + (getMembers().size() + getManagers().size()) + "/30):");

	for (UUID ids : getManagers()) {
	    if (Bukkit.getPlayer(ids) != null) {
		MessageManager.sendMessage(id, "&3" + Bukkit.getOfflinePlayer(ids).getName() + " &7- "
			+ formatHealth(Bukkit.getPlayer(ids)) + "%");
	    } else {
		MessageManager.sendMessage(id, "&3" + Bukkit.getOfflinePlayer(ids).getName() + " &7- Offline");
	    }
	}

	for (UUID ids : getMembers()) {
	    if (Bukkit.getPlayer(ids) != null) {
		MessageManager.sendMessage(id, "&7" + Bukkit.getOfflinePlayer(ids).getName() + " - "
			+ formatHealth(Bukkit.getPlayer(ids)) + "%");
	    } else {
		MessageManager.sendMessage(id, "&7" + Bukkit.getOfflinePlayer(ids).getName() + " - Offline");
	    }
	}
    }

    public void getInformation(UUID id) {
	MessageManager.sendMessage(id, "&7*** &3" + getName() + " &7***");
	MessageManager.sendMessage(id, "&3Members &7(" + (getMembers().size() + getManagers().size()) + "/30):");

	for (UUID ids : getManagers()) {
	    MessageManager.sendMessage(id, "&3" + Bukkit.getOfflinePlayer(ids).getName());
	}

	for (UUID ids : getMembers()) {
	    MessageManager.sendMessage(id, "&7" + Bukkit.getOfflinePlayer(ids).getName());
	}
    }

    int formatHealth(Player player) {
	Damageable damageable = player;

	int percent = Math.round((float) damageable.getHealth() / (float) damageable.getMaxHealth() * 100.0F);
	return percent;
    }

    public void teleport(final Player p, final String locName) {
	if (locName.equalsIgnoreCase("hq")) {
	    if (Legacy.getInstance().getSpawnManager().getSpawn().isInSpawnRadius(p.getLocation())) {
		MessageManager.sendMessage(p, "&cYou cannot warp this close to spawn.");
		return;
	    }

	    if (hq == null) {
		MessageManager.sendMessage(p, "&7Your team does not have a headquaters set.");
		return;
	    }

	    if (th.canTeleport(p)) {
		hq.getChunk().load(true);
		p.teleport(hq);
		MessageManager.sendMessage(p, "&7You cannot attack for 10 seconds.");
		return;
	    }

	    if (th.getTeleporters().containsKey(p.getUniqueId())) {
		th.getTeleporters().get(p.getUniqueId()).cancel();
	    }

	    th.getTeleporters().put(p.getUniqueId(), new BukkitRunnable() {
		@Override
		public void run() {
		    hq.getChunk().load(true);
		    p.teleport(hq);
		    th.getTeleporters().remove(p.getUniqueId());
		    MessageManager.sendMessage(p, "&7You can not attack for 10 seconds.");
		}
	    });

	    th.getTeleporters().get(p.getUniqueId()).runTaskLaterAsynchronously(Legacy.getInstance(), 10 * 20);

	    MessageManager.sendMessage(p, "&7Someone is nearby. Warping in 10 seconds. Do not move.");
	}

	if (locName.equalsIgnoreCase("rally")) {
	    if (Legacy.getInstance().getSpawnManager().hasSpawnProt(p.getUniqueId())) {
		MessageManager.sendMessage(p, "&7Your cannot warp this close to spawn.");
		return;
	    }

	    if (rally == null) {
		MessageManager.sendMessage(p, "&7Your team does not have a headquaters set.");
		return;
	    }

	    if (th.canTeleport(p)) {
		rally.getChunk().load(true);
		p.teleport(rally);
		MessageManager.sendMessage(p, "&7You can not attack for 10 seconds.");
		return;
	    }

	    if (th.getTeleporters().containsKey(p.getUniqueId())) {
		th.getTeleporters().get(p.getUniqueId()).cancel();
	    }

	    th.getTeleporters().put(p.getUniqueId(), new BukkitRunnable() {
		@Override
		public void run() {
		    rally.getChunk().load(true);
		    p.teleport(rally);
		    th.getTeleporters().remove(p.getUniqueId());
		    MessageManager.sendMessage(p, "&7You can not attack for 10 seconds.");
		}
	    });

	    th.getTeleporters().get(p.getUniqueId()).runTaskLaterAsynchronously(Legacy.getInstance(), 10 * 20);

	    MessageManager.sendMessage(p, "&7Someone is nearby. Warping in 10 seconds. Do not move.");
	}
    }
}
