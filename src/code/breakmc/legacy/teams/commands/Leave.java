package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class Leave extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public Leave() {
        super("leave", Arrays.asList("l"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou are not in a team!");
            return;
        }

        Team team = tm.getTeam(p.getUniqueId());

        if (team.isManager(p.getUniqueId())) {
            team.getManagers().remove(p.getUniqueId());
        }

        team.getMembers().remove(p.getUniqueId());

        team.sendMessage("&b" + p.getName() + " &3has left the team.");
        MessageManager.sendMessage(p, "&3You have left &b" + team.getName() + "&3.");

        if (tm.getTeamChatters().contains(p.getUniqueId())) {
            tm.getTeamChatters().remove(p.getUniqueId());
        }

        if (team.getMembers().size() <= 0 && team.getManagers().size() <=0) {
            tm.deleteTeam(team);
        }

        Legacy.getInstance().getTeamTagManager().reloadPlayer(p);

        for (UUID id : team.getMembers()) {
            if (Bukkit.getPlayer(id) != null) {
                Legacy.getInstance().getTeamTagManager().reloadPlayer(Bukkit.getPlayer(id));
            }
        }

        for (UUID id : team.getManagers()) {
            if (Bukkit.getPlayer(id) != null) {
                Legacy.getInstance().getTeamTagManager().reloadPlayer(Bukkit.getPlayer(id));
            }
        }
    }
}
