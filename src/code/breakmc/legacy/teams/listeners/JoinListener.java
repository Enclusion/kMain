package code.breakmc.legacy.teams.listeners;

import code.BreakMC.origin.util.event.UpdateNameEvent;
import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Calvin on 5/4/2015.
 */
public class JoinListener implements Listener {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Legacy.getInstance().getTeamTagManager().initPlayer(e.getPlayer());
        Legacy.getInstance().getTeamTagManager().sendTeamsToPlayer(e.getPlayer());
        Legacy.getInstance().getTeamTagManager().reloadPlayer(e.getPlayer());

        if (tm.hasTeam(e.getPlayer().getUniqueId())) {
            Team team = tm.getTeam(e.getPlayer().getUniqueId());

            if (team.isManager(e.getPlayer().getUniqueId())) {
                team.sendMessage("&3Team Login&7: &b" + e.getPlayer().getName());
            } else {
                team.sendMessage("&3Team Login&7: " + e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Legacy.getInstance().getTeamTagManager().getTeamMap().remove(e.getPlayer().getName());

        if (tm.hasTeam(e.getPlayer().getUniqueId())) {
            Team team = tm.getTeam(e.getPlayer().getUniqueId());

            if (team.isManager(e.getPlayer().getUniqueId())) {
                team.sendMessage("&3Team Logout&7: &b" + e.getPlayer().getName());
            } else {
                team.sendMessage("&3Team Logout&7: " + e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        if (tm.hasTeam(p.getUniqueId())) {
            Team team = tm.getTeam(p.getUniqueId());

            if (team.isManager(p.getUniqueId())) {
                team.sendMessage("&3Team Death&7: &b" + p.getName());
            } else {
                team.sendMessage("&3Team Death&7: " + p.getName());
            }
        }

        if (e.getEntity().getKiller() != null) {
            Player killer =  e.getEntity().getKiller();
            Team kt = null;
            Team pt = null;

            if (tm.hasTeam(killer.getUniqueId())) {
                kt = tm.getTeam(killer.getUniqueId());
            }

            if (tm.hasTeam(p.getUniqueId())) {
                pt = tm.getTeam(p.getUniqueId());
            }

//            final Hologram holo = HologramsAPI.createHologram(Legacy.getInstance(), p.getEyeLocation());
//            holo.appendTextLine(ChatColor.translateAlternateColorCodes('&', p.getPlayerListName() + " " + (pt != null ? "&7(&3" + pt.getName() + "&7)" : "") + " &7was slain by " + killer.getPlayerListName() + " " + (kt != null ? "&7(&3" + kt.getName() + "&7)" : "")));
//
//            new BukkitRunnable() {
//                public void run() {
//                    holo.delete();
//                }
//            }.runTaskLater(Legacy.getInstance(), 10 * 20);
        }
    }

    @EventHandler
    public void onPlayerUpdateName(UpdateNameEvent event) {
        Player player = event.getPlayer();

        Legacy.getInstance().getTeamTagManager().reloadPlayer(player);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player hit = (Player) e.getEntity();

            if (tm.hasTeam(hit.getUniqueId())) {
                Legacy.getInstance().getTeamTagManager().reloadPlayer(hit);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player hit = (Player) e.getEntity();

            if (tm.hasTeam(hit.getUniqueId())) {
                Legacy.getInstance().getTeamTagManager().reloadPlayer(hit);
            }
        }
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent e) {
        new BukkitRunnable() {
            public void run() {
                Legacy.getInstance().getTeamTagManager().reloadPlayer(e.getPlayer());
            }
        }.runTaskLater(Legacy.getInstance(), 5L);
    }

    @EventHandler
    public void onHealthRegain(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (tm.hasTeam(p.getUniqueId())) {
                Legacy.getInstance().getTeamTagManager().reloadPlayer(p);
            }
        }
    }
}
