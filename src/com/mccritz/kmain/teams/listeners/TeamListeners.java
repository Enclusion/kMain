package com.mccritz.kmain.teams.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;

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
		    team.sendMessage(String.format("%s &7[%s&7] &f%s", "&f<&3" + p.getName() + "&f>", team.getName(),
			    e.getMessage()));
		} else {
		    team.sendMessage(String.format("%s &7[%s&7] &f%s", "&f<&7" + p.getName() + "&f>", team.getName(),
			    e.getMessage()));
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
	Player p = e.getPlayer();

	if (tm.hasTeam(p.getUniqueId())) {
	    Team team = tm.getTeam(p.getUniqueId());

	    team.sendMessage("&3Team Login&f: " + p.getName());
	}
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
	if (tm.hasTeam(e.getPlayer().getUniqueId())) {
	    Team team = tm.getTeam(e.getPlayer().getUniqueId());

	    team.sendMessage("&3Team Logout&d: " + e.getPlayer().getName());
	}
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
	Player p = e.getEntity();

	if (tm.hasTeam(p.getUniqueId())) {
	    Team team = tm.getTeam(p.getUniqueId());

	    team.sendMessage("&3Team Death&7: " + p.getName());
	}
    }
}
