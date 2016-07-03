package com.mccritz.kmain.profiles;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.listeners.TeleportationHandler;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.teleportation.Teleport;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class Profile {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    private TeleportationHandler th = Legacy.getInstance().getTeleportationHandler();
    private UUID uniqueId;
    private String name;
    private String youtubename;
    private Double balance;
    private boolean safeLogged;
    private Location home;
    private HashMap<String, Long> usedKits = new HashMap<>();
    private int emeraldsSold, diamondsSold, goldSold, ironSold;

    private long lastTeleportTime = 0;
    private long teleportRequestTime;
    private boolean teleportRequestHere;
    private UUID teleportRequester;
    private Location teleportRequestLocation;
    private Teleport teleportRunnable = null;

    public Profile(UUID uniqueId, String name, String youtubename, Double balance, Location home, boolean safeLogged, HashMap<String, Long> usedKits, int emeraldsSold, int diamondsSold, int goldSold, int ironSold) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.youtubename = youtubename;
        this.balance = balance;
        this.safeLogged = safeLogged;
        this.usedKits = usedKits;
        this.home = home;
        this.emeraldsSold = emeraldsSold;
        this.diamondsSold = diamondsSold;
        this.goldSold = goldSold;
        this.ironSold = ironSold;
    }

    public void homeTeleport(final Player p) {
        if (Legacy.getInstance().getSpawnManager().getSpawn().isInSpawnRadius(p.getLocation())) {
            MessageManager.sendMessage(p, "&7You cannot warp inside spawn.");
            return;
        }

        if (th.canTeleport(p)) {
            if (home == null) {
                MessageManager.sendMessage(p, "&7You do not have a home set.");
                return;
            }

            p.teleport(home);
            MessageManager.sendMessage(p, "&7You cannot attack for 10 seconds.");
        } else {
            if (th.getTeleporters().containsKey(p.getUniqueId())) {
                th.getTeleporters().get(p.getUniqueId()).cancel();
            }

            th.getTeleporters().put(p.getUniqueId(), new BukkitRunnable() {
                public void run() {
                    p.teleport(home);
                    MessageManager.sendMessage(p, "&7You have teleported to your &chome&7.");
                    th.getTeleporters().remove(p.getUniqueId());
                }
            });

            th.getTeleporters().get(p.getUniqueId()).runTaskLaterAsynchronously(Legacy.getInstance(), 10 * 20L);

            MessageManager.sendMessage(p, "&7Someone is nearby. Warping in 10 seconds. Do not move.");
        }
    }

    public void setTeleportRunnable(Teleport runnable) {
        if (this.teleportRunnable != null) {
            this.teleportRunnable.cancel();
            this.teleportRunnable = null;
        }

        this.teleportRunnable = runnable;
    }

    public void requestTeleport(Player requester, boolean here) {
        if (requester != null) {
            teleportRequestTime = System.currentTimeMillis() + (120 * 1000L);
            teleportRequester = requester.getUniqueId();
            teleportRequestHere = here;
            teleportRequestLocation = here ? requester.getLocation() : Bukkit.getPlayer(uniqueId).getLocation();
        }
    }

    public void createTeleportRunnable(Teleport teleportInst) {
        if (Bukkit.getPlayer(teleportInst.getID()) != null) {
            if (Bukkit.getPlayer(teleportInst.getID()).getGameMode() == GameMode.CREATIVE && Bukkit.getPlayer(teleportInst.getID()).hasPermission("kmain.tpa.bypass")) {
                setTeleportRunnable(teleportInst);
                teleportInst.setTime(0);
                teleportInst.send();
                Legacy.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Legacy.getInstance(), teleportInst, 0);
                return;
            }
        }

        setTeleportRunnable(teleportInst);
        teleportInst.send();
        Legacy.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Legacy.getInstance(), teleportInst, teleportInst.getTime() == 0 ? 0 : teleportInst.getTime() * 20L);
    }

    public void setBalance(Double balance) {
        this.balance = balance;

        if (this.balance >= Legacy.getInstance().getConfig().getDouble("economy.max-balance"))
            this.balance = Legacy.getInstance().getConfig().getDouble("economy.max-balance");
    }

    public void sendMessage(String message) {
        MessageManager.sendMessage(uniqueId, message);
    }
}
