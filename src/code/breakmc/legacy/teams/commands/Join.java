package code.breakmc.legacy.teams.commands;


import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class Join extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    
    public Join() {
        super("join", Arrays.asList("j"));
    }

    @Override
    public void execute(final Player p, String[] args) {
        if (args.length == 0 || args.length > 2) {
            MessageManager.sendMessage(p, "&cImproper usage! /team join (name) (password)");
            return;
        }

        if (tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou are already on a team!");
            return;
        }

        if (tm.getTeam(args[0]) == null) {
            MessageManager.sendMessage(p, "&cCould not find team \"" + args[0] + "\"!");
            return;
        }

        Team team = tm.getTeam(args[0]);

        if (args.length == 1) {
            if (team.getPassword().isEmpty()) {
                MessageManager.sendMessage(p, "&3You have successfully joined &b" + team.getName() + "&3!");
                team.sendMessage("&b" + p.getName() + " &3has joined the team!");
                team.getMembers().add(p.getUniqueId());
            } else {
                MessageManager.sendMessage(p, "&cThis team requires a password.");
                return;
            }
        }

        if (args.length == 2) {
            if (team.getPassword().isEmpty()) {
                MessageManager.sendMessage(p, "&3You have successfully joined &b" + team.getName() + "&3!");
                team.sendMessage("&b" + p.getName() + " &3has joined the team!");
                team.getMembers().add(p.getUniqueId());
                return;
            }

            if (!args[1].equals(team.getPassword())) {
                MessageManager.sendMessage(p, "&cIncorrect password.");
                return;
            }

            MessageManager.sendMessage(p, "&3You have successfully joined &b" + team.getName() + "&3!");
            team.sendMessage("&b" + p.getName() + " &3has joined the team!");
            team.getMembers().add(p.getUniqueId());
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
