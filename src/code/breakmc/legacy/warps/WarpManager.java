package code.breakmc.legacy.warps;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.LocationSerialization;
import code.breakmc.legacy.utils.MessageManager;
import com.breakmc.pure.Pure;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Calvin on 4/25/2015.
 * Project: Legacy
 */
public class WarpManager {

    private Legacy main = Legacy.getInstance();
    private TeamManager tm = main.getTeamManager();
    private ListMultimap<UUID, Warp> warps = ArrayListMultimap.create();
    private DBCollection wCollection = main.getDb().getCollection("warps");
    private HashMap<UUID, BukkitRunnable> overriding = new HashMap<>();

    public WarpManager() {
        loadWarps();

        new BukkitRunnable() {
            @Override
            public void run() {
                saveWarps();
            }
        }.runTaskTimerAsynchronously(main, 0L, 300*20L);
    }

    public void loadWarps() {
        DBCursor dbc = wCollection.find();

        main.getLogger().log(Level.INFO, "Preparing to load " + dbc.count() + " player warps.");

        while (dbc.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) dbc.next();

            UUID id = UUID.fromString(dbo.getString("uuid"));
            String name = dbo.getString("name");
            Location loc = LocationSerialization.deserializeLocation(dbo.getString("location"));

            Warp warp = new Warp(id, name, loc);

            if (!warps.containsKey(id)) {
                warps.put(id, warp);
            } else {
                warps.get(id).add(warp);
            }
        }

        main.getLogger().log(Level.INFO, "Successfully loaded " + warps.size() + " players warps.");
    }

    public void saveWarps() {
        main.getLogger().log(Level.INFO, "Preparing to save " + warps.size() + " players warps.");

        for (UUID ids : warps.keySet()) {
            for (Warp ws : warps.get(ids)) {
                if (ws.getLocation() != null && ws.getLocation().getWorld() != null) {
                    DBCursor dbc = wCollection.find(new BasicDBObject("uuid", ids.toString()).append("name", ws.getName()));

                    BasicDBObject dbo = new BasicDBObject();
                    dbo.put("uuid", ids.toString());
                    dbo.put("name", ws.getName());
                    dbo.put("location", LocationSerialization.serializeLocation(ws.getLocation()));

                    if (dbc.hasNext()) {
                        wCollection.update(dbc.getQuery(), dbo);
                    } else {
                        wCollection.insert(dbo);
                    }
                }
            }
        }

        main.getLogger().log(Level.INFO, "Successfully saved " + warps.size() + " players warps.");
    }

    public void setWarp(UUID id, String name, Location loc) {
        final Player p = Bukkit.getPlayer(id);

        if (!name.matches("^[A-Za-z0-9_+-]*$")) {
            MessageManager.sendMessage(id, "&cWarp names must be alphanumerical.");
            return;
        }

        if (warps.get(id) != null && warps.get(id).size() >= warpSize(p)) {
            p.sendMessage("�cYou have set the max amount of warps!");
            return;
        }

        Warp warp;

        if (warps.containsKey(id)) {
            for (Warp ws : warps.get(id)) {
                if (ws.getName().equalsIgnoreCase(name)) {
                    MessageManager.sendMessage(id, "&7You already have a warp \"&b" + ws.getName() + "&7\"");

                    FancyMessage message = new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7Would you like to overwrite it? "));
                    message.then(ChatColor.GREEN + "/yes").command("/yes").tooltip(ChatColor.GREEN + "Yes");
                    message.then(ChatColor.RED + "/no").command("/no").tooltip(ChatColor.RED + " No");
                    message.send(p);

                    final TempWarp tempwarp = new TempWarp(ws, loc);

                    p.setMetadata("warpToOverride", new FixedMetadataValue(main, tempwarp));

                    overriding.put(p.getUniqueId(), new BukkitRunnable() {
                        public void run() {
                            MessageManager.sendMessage(p, "&cDid not receive an answer in time! Cancelling overwrite of warp \"" + tempwarp.getWarp().getName() + "\"");
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

        MessageManager.sendMessage(id, "&7Warp \"&b" + warp.getName() + "&7\" has been set!");

        DBCursor dbc = wCollection.find(new BasicDBObject("uuid", id.toString()).append("name", warp.getName()));

        BasicDBObject dbo = new BasicDBObject();
        dbo.put("uuid", id.toString());
        dbo.put("name", warp.getName());
        dbo.put("location", LocationSerialization.serializeLocation(warp.getLocation()));

        if (dbc.hasNext()) {
            wCollection.update(dbc.getQuery(), dbo);
        } else {
            wCollection.insert(dbo);
        }
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
                MessageManager.sendMessage(id, "&cWarp \"" + name + "&7\" could not be found!");
                return;
            }

            warps.get(id).remove(warp);

            DBCursor dbc = wCollection.find(new BasicDBObject("uuid", id.toString()).append("name", warp.getName()));

            if (dbc.hasNext()) {
                wCollection.remove(dbc.getQuery());
            }

            MessageManager.sendMessage(id, "&7Warp \"&b" + warp.getName() + "&7\" has been deleted!");
        } else {
            MessageManager.sendMessage(id, "&cYou have not set any warps.");
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
                MessageManager.sendMessage(id, "&cWarp \"" + name + "&7\" could not be found!");
                return;
            }

            warps.get(target).remove(warp);

            DBCursor dbc = wCollection.find(new BasicDBObject("uuid", target.toString()).append("name", warp.getName()));

            if (dbc.hasNext()) {
                wCollection.remove(dbc.getQuery());
            }

            MessageManager.sendMessage(id, "&a" + Bukkit.getOfflinePlayer(target).getName() + "'s &7warp \"&b" + warp.getName() + "&7\" has been deleted!");
        } else {
            MessageManager.sendMessage(id, "&c" + Bukkit.getOfflinePlayer(target).getName() + " does not have any warps.");
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
                MessageManager.sendMessage(p, "&cWarp \"" + name + "\" could not be found!");
                return;
            }

            if (canTeleport(p)) {
                p.teleport(warp.getLocation());
                MessageManager.sendMessage(p, "&7You have warped to \"&b" + warp.getName() + "&7\"");
                return;
            }

            warp.teleport(p);
        } else {
            MessageManager.sendMessage(p, "&cYou have not set any warps.");
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
                MessageManager.sendMessage(p, "&cWarp \"" + name + "\" could not be found!");
                return;
            }

            p.teleport(warp.getLocation());
            MessageManager.sendMessage(p, "&7Warped to " + Bukkit.getOfflinePlayer(target).getName() + "'s " + warp.getName());
        } else {
            MessageManager.sendMessage(p, "&7" + Bukkit.getOfflinePlayer(target).getName() + " doesn't have any warps.");
        }
    }

    public void listWarps(UUID id) {
        if (!warps.containsKey(id) || warps.containsKey(id) && warps.get(id).size() == 0) {
            MessageManager.sendMessage(id, "&cYou have not set any warps.");
        } else {
            if (Bukkit.getPlayer(id) != null) {
                MessageManager.sendMessage(id, "&3Your Warps &7(" + warps.get(id).size() + "/" + warpSize(Bukkit.getPlayer(id)) + ")");
                FancyMessage message = new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
                for (int i = 0; i < warps.get(id).size(); i++) {
                    if (i < warps.get(id).size() - 1) {
                        message.then(warps.get(id).get(i).getName()).color(ChatColor.AQUA).tooltip(ChatColor.translateAlternateColorCodes('&', "&7Click here to warp to \"&b" + warps.get(id).get(i).getName() + "&7\"!")).command("/go " + warps.get(id).get(i).getName()).then(", ").color(ChatColor.GRAY);
                    } else {
                        message.then(warps.get(id).get(i).getName()).color(ChatColor.AQUA).tooltip(ChatColor.translateAlternateColorCodes('&', "&7Click here to warp to \"&b" + warps.get(id).get(i).getName() + "&7\"!")).command("/go " + warps.get(id).get(i).getName());
                    }
                }

                message.send(Bukkit.getPlayer(id));
            }
        }
    }

    public void adminListWarps(UUID id, UUID target) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(target);

        if (!warps.containsKey(target) || warps.containsKey(target) && warps.get(target).size() == 0) {
            MessageManager.sendMessage(id, "&b" + op.getName() + " &7hasn't set any warps.");
        } else {
            if (Bukkit.getPlayer(target) != null) {
                MessageManager.sendMessage(id, "&3" + Bukkit.getPlayer(target).getName() + "'s Warps &7(" + warps.get(id).size() + "/" + warpSize(Bukkit.getPlayer(target)) + ")");
                FancyMessage message = new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&r"));

                for (int i = 0; i < warps.get(target).size(); i++) {
                    if (i < warps.get(target).size() - 1) {
                        message.then(warps.get(target).get(i).getName()).color(ChatColor.AQUA).tooltip(ChatColor.translateAlternateColorCodes('&', "&7Click here to warp to " + op.getName() + "'s \"&b" + warps.get(target).get(i).getName() + "&7\"!")).command("/goas " + op.getName() + " " + warps.get(target).get(i).getName()).then(", ").color(ChatColor.GRAY);
                    } else {
                        message.then(warps.get(target).get(i).getName()).color(ChatColor.AQUA).tooltip(ChatColor.translateAlternateColorCodes('&', "&7Click here to warp to " + op.getName() + "'s \"&b" + warps.get(target).get(i).getName() + "&7\"!")).command("/goas " + op.getName() + " " + warps.get(target).get(i).getName());
                    }
                }

                message.send(Bukkit.getPlayer(id));
            } else {
                FancyMessage message = new FancyMessage(op.getName() + "'s Warps").color(ChatColor.DARK_AQUA).then(" (" + warps.get(target).size() + "/unknown)\n").color(ChatColor.GRAY);

                for (int i = 0; i < warps.get(target).size(); i++) {
                    if (i < warps.get(target).size() - 1) {
                        message.then(warps.get(target).get(i).getName()).color(ChatColor.AQUA).tooltip(ChatColor.translateAlternateColorCodes('&', "&7Click here to warp to " + op.getName() + "'s \"&b" + warps.get(target).get(i).getName() + "&7\"!")).command("/goas " + op.getName() + " " + warps.get(target).get(i).getName()).then(", ").color(ChatColor.GRAY);
                    } else {
                        message.then(warps.get(target).get(i).getName()).color(ChatColor.AQUA).tooltip(ChatColor.translateAlternateColorCodes('&', "&7Click here to warp to " + op.getName() + "'s \"&b" + warps.get(target).get(i).getName() + "&7\"!")).command("/goas " + op.getName() + " " + warps.get(target).get(i).getName());
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

    public boolean canTeleport(Player p) {
        for (Entity ent : p.getNearbyEntities(40, 20, 40)) {
            if (ent instanceof Player) {
                Player near = (Player) ent;

                if (near.equals(p)) continue;

                if (Pure.getInstance().getPunishmentManager().isVanished(near)) continue;

                if (tm.hasTeam(near.getUniqueId()) && tm.hasTeam(p.getUniqueId())) {
                    if (!tm.getTeam(p.getUniqueId()).equals(tm.getTeam(near.getUniqueId()))) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
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