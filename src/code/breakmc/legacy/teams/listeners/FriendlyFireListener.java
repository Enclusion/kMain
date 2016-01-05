package code.breakmc.legacy.teams.listeners;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FriendlyFireListener implements Listener {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
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
}
