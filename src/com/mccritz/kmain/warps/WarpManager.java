package com.mccritz.kmain.warps;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.utils.LocationSerialization;
import com.mccritz.kmain.utils.MessageManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import mkremins.fanciful.FancyMessage;

/**
 * Created by Calvin on 4/25/2015. Project: Legacy
 */
public class WarpManager {

    private Legacy main = Legacy.getInstance();
    private ListMultimap<UUID, Warp> warps = ArrayListMultimap.create();
    private MongoCollection<Document> warpCollection = main.getMongoDatabase().getCollection("warps");
    private HashMap<UUID, BukkitRunnable> overriding = new HashMap<>();

    public WarpManager() {
	loadWarps();

	new BukkitRunnable() {
	    @Override
	    public void run() {
		saveWarps();
	    }
	}.runTaskTimerAsynchronously(main, 0L, 300 * 20L);
    }

    public void loadWarps() {
	main.getLogger().log(Level.INFO, "Preparing to load " + warpCollection.count() + " player warps.");

	new BukkitRunnable() {
	    @Override
	    public void run() {
		for (Document document : warpCollection.find()) {
		    UUID id = UUID.fromString(document.getString("uuid"));
		    String name = document.getString("name");
		    Location location = LocationSerialization.deserializeLocation(document.getString("location"));

		    Warp warp = new Warp(id, name, location);

		    if (!warps.containsKey(id)) {
			warps.put(id, warp);
		    } else {
			warps.get(id).add(warp);
		    }
		}

		main.getLogger().log(Level.INFO, "Successfully loaded " + warps.size() + " players warps.");
	    }
	}.runTaskAsynchronously(main);
    }

    public void saveWarps() {
	main.getLogger().log(Level.INFO, "Preparing to save " + warps.size() + " players warps.");

	for (UUID ids : warps.keySet()) {
	    for (Warp ws : warps.get(ids)) {
		if (ws.getLocation() != null && ws.getLocation().getWorld() != null) {
		    Document document = new Document("uuid", ws.getOwner().toString()).append("name", ws.getName());
		    document.append("location", LocationSerialization.serializeLocation(ws.getLocation()));

		    new BukkitRunnable() {
			@Override
			public void run() {
			    for (Document documents : warpCollection
				    .find(Filters.eq("uuid", ws.getOwner().toString()))) {
				if (documents.getString("name").equalsIgnoreCase(ws.getName())) {
				    warpCollection.replaceOne(documents, document, new UpdateOptions().upsert(true));
				}
			    }
			}
		    }.runTaskAsynchronously(main);
		}
	    }
	}

	main.getLogger().log(Level.INFO, "Successfully saved " + warps.size() + " players warps.");
    }

    public void setWarp(UUID id, String name, Location loc) {
	final Player p = Bukkit.getPlayer(id);

	if (!name.matches("^[A-Za-z0-9_+-]*$")) {
	    MessageManager.sendMessage(id, "&7Warp names must be alphanumerical.");
	    return;
	}

	if (warps.get(id) != null && warps.get(id).size() >= warpSize(p)) {
	    MessageManager.sendMessage(id, "&7You have set the maximum amount of warps.");
	    return;
	}

	Warp warp;

	if (warps.containsKey(id)) {
	    for (Warp ws : warps.get(id)) {
		if (ws.getName().equalsIgnoreCase(name)) {
		    MessageManager.sendMessage(id, "&7You already have a warp named &c" + ws.getName() + "&7.");

		    FancyMessage message = new FancyMessage(
			    ChatColor.translateAlternateColorCodes('&', "&7Would you like to overwrite it? "));
		    message.then(ChatColor.GREEN + "/yes").command("/yes").tooltip(ChatColor.GREEN + "Yes");
		    message.then(ChatColor.GRAY + "/");
		    message.then(ChatColor.RED + "/no").command("/no").tooltip(ChatColor.RED + "No");
		    message.send(p);

		    final TempWarp tempwarp = new TempWarp(ws, loc);

		    p.setMetadata("warpToOverride", new FixedMetadataValue(main, tempwarp));

		    overriding.put(p.getUniqueId(), new BukkitRunnable() {
			@Override
			public void run() {
			    MessageManager.sendMessage(p, "&7You ran out of time. Cancelling overwrite of &c"
				    + tempwarp.getWarp().getName() + "&7.");
			}
		    });

		    overriding.get(p.getUniqueId()).runTaskLater(main, 200L);

		    return;
		}
	    }

	    warp = new Warp(id, name, loc);

	    warps.get(id).add(warp);
	} else {
	    warp = new Warp(id, name, loc);

	    warps.put(id, warp);
	}

	MessageManager.sendMessage(id, "&7You have set " + warp.getName() + " as a warp.");

	Document document = new Document("uuid", id.toString()).append("name", warp.getName());
	document.append("location", LocationSerialization.serializeLocation(warp.getLocation()));

	new BukkitRunnable() {
	    @Override
	    public void run() {
		warpCollection.insertOne(document);
	    }
	}.runTaskAsynchronously(main);
    }

    public void removeWarp(UUID id, String name) {
	Warp warp = null;
	if (warps.containsKey(id)) {
	    for (Warp ws : warps.get(id)) {
		if (ws.getName().equalsIgnoreCase(name)) {
		    warp = ws;
		}
	    }

	    if (warp == null) {
		MessageManager.sendMessage(id, "&7Warp &c" + name + "&7 could not be found.");
		return;
	    }

	    warps.get(id).remove(warp);

	    final Warp finalWarp = warp;
	    new BukkitRunnable() {
		@Override
		public void run() {
		    for (Document documents : warpCollection.find(Filters.eq("uuid", id.toString()))) {
			if (documents.getString("name").equalsIgnoreCase(finalWarp.getName())) {
			    warpCollection.deleteOne(documents);
			}
		    }
		}
	    }.runTaskAsynchronously(main);

	    MessageManager.sendMessage(id, "&7Warp &c" + warp.getName() + "&7 has been deleted.");
	} else {
	    MessageManager.sendMessage(id, "&7You have not set any warps yet.");
	}
    }

    public void adminRemoveWarp(UUID id, UUID target, String name) {
	Warp warp = null;
	if (warps.containsKey(target)) {
	    for (Warp ws : warps.get(target)) {
		if (ws.getName().equalsIgnoreCase(name)) {
		    warp = ws;
		}
	    }

	    if (warp == null) {
		MessageManager.sendMessage(id, "&7Warp &c" + name + "&7 could not be found.");
		return;
	    }

	    warps.get(target).remove(warp);

	    final Warp finalWarp = warp;
	    new BukkitRunnable() {
		@Override
		public void run() {
		    for (Document documents : warpCollection.find(Filters.eq("uuid", id.toString()))) {
			if (documents.getString("name").equalsIgnoreCase(finalWarp.getName())) {
			    warpCollection.deleteOne(documents);
			}
		    }
		}
	    }.runTaskAsynchronously(main);

	    MessageManager.sendMessage(id, "&7You deleted &c" + Bukkit.getOfflinePlayer(target).getName() + " &7warp &c"
		    + warp.getName() + "&7.");
	} else {
	    MessageManager.sendMessage(id,
		    "&c" + Bukkit.getOfflinePlayer(target).getName() + " &7does not have any warps.");
	}
    }

    public void warpTeleport(final Player p, String name) {
	Warp warp = null;
	if (warps.containsKey(p.getUniqueId())) {
	    for (Warp ws : warps.get(p.getUniqueId())) {
		if (ws.getName().equalsIgnoreCase(name)) {
		    warp = ws;
		}
	    }

	    if (warp == null) {
		MessageManager.sendMessage(p, "&7Warp &c" + name + "&7 could not be found.");
		return;
	    }

	    warp.teleport(p);
	} else {
	    MessageManager.sendMessage(p, "&7You have not set any warps yet.");
	}
    }

    public void warpAdminTeleport(Player p, UUID target, String name) {
	Warp warp = null;
	if (warps.containsKey(target)) {
	    for (Warp ws : warps.get(target)) {
		if (ws.getName().equalsIgnoreCase(name)) {
		    warp = ws;
		}
	    }

	    if (warp == null) {
		MessageManager.sendMessage(p, "&7Warp &c" + name + "&7 could not be found.");
		return;
	    }

	    p.teleport(warp.getLocation());
	    MessageManager.sendMessage(p, "&7You cannot attack for 10 seconds.");
	    Legacy.getInstance().getLogger().log(Level.INFO,
		    "[Admin Teleport]: " + p.getName() + " to " + Bukkit.getOfflinePlayer(name).getName() + "'s warp");
	} else {
	    MessageManager.sendMessage(p,
		    "&c" + Bukkit.getOfflinePlayer(target).getName() + "&7does not have any warps.");
	}
    }

    public void listWarps(UUID id) {
	if (!warps.containsKey(id) || warps.containsKey(id) && warps.get(id).size() == 0) {
	    MessageManager.sendMessage(id, "&7You have not set any warps yet.");
	} else {
	    if (Bukkit.getPlayer(id) != null) {
		MessageManager.sendMessage(id,
			"&7***Warp List*** (" + getWarps().get(id).size() + "/" + warpSize(Bukkit.getPlayer(id)) + ")");
		FancyMessage message = new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
		for (int i = 0; i < warps.get(id).size(); i++) {
		    if (i < warps.get(id).size() - 1) {
			message.then(warps.get(id).get(i).getName()).color(ChatColor.GRAY)
				.tooltip(ChatColor.translateAlternateColorCodes('&',
					"&7Warp to &c" + warps.get(id).get(i).getName() + "&7."))
				.command("/go " + warps.get(id).get(i).getName()).then(", ").color(ChatColor.GRAY);
		    } else {
			message.then(warps.get(id).get(i).getName()).color(ChatColor.GRAY)
				.tooltip(ChatColor.translateAlternateColorCodes('&',
					"&7warp to &c" + warps.get(id).get(i).getName() + "&7."))
				.command("/go " + warps.get(id).get(i).getName());
		    }
		}

		message.send(Bukkit.getPlayer(id));
	    }
	}
    }

    public void adminListWarps(UUID id, UUID target) {
	OfflinePlayer op = Bukkit.getOfflinePlayer(target);

	if (!warps.containsKey(target) || warps.containsKey(target) && warps.get(target).size() == 0) {
	    MessageManager.sendMessage(id, "&c" + op.getName() + " &7does not have any warps.");
	} else {
	    if (Bukkit.getPlayer(target) != null) {
		MessageManager.sendMessage(id, "&7***Warp List*** (" + getWarps().get(target).size() + "/"
			+ warpSize(Bukkit.getPlayer(target)) + ")");
		FancyMessage message = new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&r"));

		for (int i = 0; i < warps.get(target).size(); i++) {
		    if (i < warps.get(target).size() - 1) {
			message.then(warps.get(target).get(i).getName()).color(ChatColor.GRAY)
				.tooltip(ChatColor.translateAlternateColorCodes('&',
					"&7Warp to &c" + op.getName() + "'s &c" + warps.get(target).get(i).getName()
						+ "&7."))
				.command("/goas " + op.getName() + " " + warps.get(target).get(i).getName()).then(", ")
				.color(ChatColor.GRAY);
		    } else {
			message.then(warps.get(target).get(i).getName()).color(ChatColor.GRAY)
				.tooltip(ChatColor.translateAlternateColorCodes('&',
					"&7Warp to &c" + op.getName() + "'s &c" + warps.get(target).get(i).getName()
						+ "&7."))
				.command("/goas " + op.getName() + " " + warps.get(target).get(i).getName());
		    }
		}

		message.send(Bukkit.getPlayer(id));
	    } else {
		FancyMessage message = new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&r"));

		for (int i = 0; i < warps.get(target).size(); i++) {
		    if (i < warps.get(target).size() - 1) {
			message.then(warps.get(target).get(i).getName()).color(ChatColor.GRAY)
				.tooltip(ChatColor.translateAlternateColorCodes('&',
					"&7Warp to &c" + op.getName() + "'s &c" + warps.get(target).get(i).getName()
						+ "&7."))
				.command("/goas " + op.getName() + " " + warps.get(target).get(i).getName()).then(", ")
				.color(ChatColor.GRAY);
		    } else {
			message.then(warps.get(target).get(i).getName()).color(ChatColor.GRAY)
				.tooltip(ChatColor.translateAlternateColorCodes('&',
					"&7Warp to &c" + op.getName() + "'s &c" + warps.get(target).get(i).getName()
						+ "&7."))
				.command("/goas " + op.getName() + " " + warps.get(target).get(i).getName());
		    }
		}

		message.send(Bukkit.getPlayer(id));
	    }
	}
    }

    public int warpSize(Player player) {
	int warpCount = 0;

	if (player.isOp() || player.hasPermission("warps.admin")) {
	    warpCount = 100;
	} else {
	    for (int i = 0; i < 100; i++) {
		if (player.hasPermission("warps." + i)) {
		    warpCount = i;
		}
	    }
	}
	return warpCount;
    }

    public class TempWarp {

	Warp warp;
	Location loc;

	public TempWarp(Warp warp, Location loc) {
	    this.warp = warp;
	    this.loc = loc;
	}

	public Warp getWarp() {
	    return warp;
	}

	public Location getLoc() {
	    return loc;
	}
    }

    public HashMap<UUID, BukkitRunnable> getOverriding() {
	return overriding;
    }

    public ListMultimap<UUID, Warp> getWarps() {
	return warps;
    }
}