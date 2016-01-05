package code.breakmc.legacy.teams.listeners;

import code.BreakMC.origin.util.event.UpdateNameEvent;
import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Calvin on 5/4/2015.
 */
public class TeamListeners implements Listener {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (tm.hasTeam(p.getUniqueId())) {
            Team team = tm.getTeam(p.getUniqueId());
            if (tm.getTeamChatters().contains(p.getUniqueId())) {
                e.setCancelled(true);
                if (team.isManager(p.getUniqueId())) {
                    team.sendMessage(String.format("&3(%s) %s&f: %s", team.getName(), "&3" + p.getName(), e.getMessage()));
                } else {
                    team.sendMessage(String.format("&3(%s) %s&f: %s", team.getName(), "&7" + p.getName(), e.getMessage()));
                }
            }
        } else {
            if (tm.getTeamChatters().contains(p.getUniqueId())) {
                tm.getTeamChatters().remove(p.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onFriendlyFire(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player hitter = (Player) e.getDamager();
            Player hit = (Player) e.getEntity();

            if (tm.hasTeam(hitter.getUniqueId()) && tm.hasTeam(hit.getUniqueId())) {
                Team t1 = tm.getTeam(hit.getUniqueId());
                Team t2 = tm.getTeam(hitter.getUniqueId());

                if (t1.equals(t2)) {
                    if (!t1.isFriendlyFireEnabled()) {
                        e.setCancelled(true);
                    }
                }
            }
        }

        if (e.getDamager() instanceof Projectile && e.getEntity() instanceof Player) {
            Projectile hitterp = (Projectile) e.getDamager();

            if (hitterp.getShooter() instanceof Player) {
                Player hit = (Player) e.getEntity();
                Player hitter = (Player) hitterp.getShooter();

                if (tm.hasTeam(hitter.getUniqueId()) && tm.hasTeam(hit.getUniqueId())) {
                    Team t1 = tm.getTeam(hit.getUniqueId());
                    Team t2 = tm.getTeam(hitter.getUniqueId());

                    if (t1.equals(t2)) {
                        if (!t1.isFriendlyFireEnabled()) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

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

//        if (e.getEntity().getKiller() != null) {
//            Player killer =  e.getEntity().getKiller();
//            Team kt = null;
//            Team pt = null;
//
//            if (tm.hasTeam(killer.getUniqueId())) {
//                kt = tm.getTeam(killer.getUniqueId());
//            }
//
//            if (tm.hasTeam(p.getUniqueId())) {
//                pt = tm.getTeam(p.getUniqueId());
//            }
//
//            final Hologram holo = HologramsAPI.createHologram(Legacy.getInstance(), p.getEyeLocation());
//            holo.appendTextLine(ChatColor.translateAlternateColorCodes('&', p.getPlayerListName() + " " + (pt != null ? "&7(&3" + pt.getName() + "&7)" : "") + " &7was slain by " + killer.getPlayerListName() + " " + (kt != null ? "&7(&3" + kt.getName() + "&7)" : "")));
//
//            new BukkitRunnable() {
//                public void run() {
//                    holo.delete();
//                }
//            }.runTaskLater(Legacy.getInstance(), 10 * 20);
//        }
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
