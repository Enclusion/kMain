package code.breakmc.legacy.teams.listeners;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

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
}
