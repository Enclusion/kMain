package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Leave extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public Leave() {
        super("leave", Collections.singletonList("l"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&7You are not in a team.");
            return;
        }

        Team team = tm.getTeam(p.getUniqueId());

        if (team.isManager(p.getUniqueId())) {
            team.getManagers().remove(p.getUniqueId());
        }

        team.getMembers().remove(p.getUniqueId());

        team.sendMessage("&3" + p.getName() + " &7has left the team.");
        MessageManager.sendMessage(p, "&7You have left the team.");

        PlayerUtility.updateScoreboard(p);

        if (tm.getTeamChatters().contains(p.getUniqueId())) {
            tm.getTeamChatters().remove(p.getUniqueId());
        }

        if (team.getMembers().size() <= 0 && team.getManagers().size() <=0) {
            tm.deleteTeam(team);
        }
    }
}
