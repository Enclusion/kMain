package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Kick extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    private ProfileManager pm = Legacy.getInstance().getProfileManager();

    public Kick() {
        super("kick", true, Collections.singletonList("k"), false);
    }

    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length == 0 || args.length > 1) {
            MessageManager.sendMessage(p, "&cImproper usage! /team kick <player>");
            return;
        }

        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou are not in a team.");
            return;
        }

        Team team = tm.getTeam(p.getUniqueId());

        if (!team.isManager(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&7You must be a &3manager &7of this team to do that.");
            return;
        }

        Profile prof = pm.getProfile(args[0]);

        if (prof == null) {
            MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found.");
            return;
        }

        if (!tm.hasTeam(prof.getUniqueId())) {
            MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is not in your team!");
            return;
        }

        if (!tm.getTeam(prof.getUniqueId()).equals(team)) {
            MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is not in your team!");
            return;
        }

        if (team.getMembers().contains(prof.getUniqueId())) {
            team.getMembers().remove(prof.getUniqueId());
        } else {
            team.getManagers().remove(prof.getUniqueId());
        }

        PlayerUtility.updateScoreboard(p);

        MessageManager.sendMessage(prof.getUniqueId(), "&7You have left the team.");
        team.sendMessage("&3" + prof.getName() + " &7has been kicked by &3" + p.getName() + "&7.");
    }
}
