package code.breakmc.legacy.profiles;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.moon.ScoreboardManager;
import com.breakmc.pure.Pure;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class Profile implements Listener {

    private ScoreboardManager sbm = Legacy.getInstance().getScoreboardManager();
    private TeamManager tm = Legacy.getInstance().getTeamManager();
    private HashMap<UUID, BukkitRunnable> dontMove = new HashMap<>();
    private UUID uniqueId;
    private String name;
    private String youtubename;
    private Double balance;
    private boolean safeLogged;
    private Location home;
    private HashMap<String, Long> usedKits = new HashMap<>();
    private int emeraldsSold, diamondsSold, goldSold, ironSold;

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

        Bukkit.getPluginManager().registerEvents(this, Legacy.getInstance());
    }

    @EventHandler
    public void onaMove(PlayerMoveEvent e) {
        if (dontMove.containsKey(e.getPlayer().getUniqueId())) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                dontMove.get(e.getPlayer().getUniqueId()).cancel();
                dontMove.remove(e.getPlayer().getUniqueId());
                MessageManager.sendMessage(e.getPlayer(), "&cYou moved! Teleportation cancelled.");
            }
        }
    }

    public void homeTeleport(final Player p) {
        if (Legacy.getInstance().getSpawnManager().getSpawn().isInSpawnRadius(p.getLocation())) {
            MessageManager.sendMessage(p, "&cYou cannot warp within spawn.");
            return;
        }

        if (canTeleport(p)) {
            if (home == null) {
                MessageManager.sendMessage(p, "&cYou do not have a home set.");
                return;
            }

            p.teleport(home);
            MessageManager.sendMessage(p, "&7You have teleported to your &bhome&7.");
        } else {
            if (dontMove.containsKey(p.getUniqueId())) {
                dontMove.get(p.getUniqueId()).cancel();
            }

            dontMove.put(p.getUniqueId(), new BukkitRunnable() {
                public void run() {
                    p.teleport(home);
                    MessageManager.sendMessage(p, "&7You have teleported to your &bhome&7.");
                    dontMove.remove(p.getUniqueId());
                }
            });

            dontMove.get(p.getUniqueId()).runTaskLater(Legacy.getInstance(), 10 * 20L);

            MessageManager.sendMessage(p, "&7Someone is nearby! Warping in 10 seconds, do not move");
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

                if (tm.getTeam(near.getUniqueId()) != null) {
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

    public void setBalance(Double balance) {
        this.balance = balance;
        if (this.balance >= 250000.00)
            this.balance = 250000.00;

    }

    public void sendMessage(String message) {
        MessageManager.sendMessage(uniqueId, message);
    }
}
