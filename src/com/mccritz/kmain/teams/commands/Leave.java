package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Leave extends TeamSubCommand {

    private TeamManager tm = kMain.getInstance().getTeamManager();

    public Leave() {
        super("leave", Collections.singletonList("l"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.message(p, "&7You are not in a team.");
            return;
        }

        Team team = tm.getTeam(p.getUniqueId());

        if (team.isManager(p.getUniqueId())) {
            team.getManagers().remove(p.getUniqueId());
        }

        team.getMembers().remove(p.getUniqueId());

        team.message("&3" + p.getName() + " &7has left the team.");
        MessageManager.message(p, "&7You have left the team.");

        if (tm.getTeamChatters().contains(p.getUniqueId())) {
            tm.getTeamChatters().remove(p.getUniqueId());
        }

        if (team.getMembers().size() <= 0 && team.getManagers().size() <=0) {
            tm.deleteTeam(team);
        }
    }
}
