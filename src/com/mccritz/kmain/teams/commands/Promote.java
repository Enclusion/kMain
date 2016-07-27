package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Promote extends TeamSubCommand {

    private TeamManager tm = kMain.getInstance().getTeamManager();
    private ProfileManager pm = kMain.getInstance().getProfileManager();

    public Promote() {
        super("promote", true, Collections.singletonList("p"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 1) {
            MessageManager.message(p, "&cImproper usage! /team promote <player>");
            return;
        }

        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.message(p, "&7You are not in a team.");
            return;
        }

        Team team = tm.getTeam(p.getUniqueId());

        if (!team.isManager(p.getUniqueId())) {
            MessageManager.message(p, "&7You must be a &3manager &7of this team to do that.");
            return;
        }

        Profile prof = pm.getProfile(args[0]);

        if (prof == null) {
            MessageManager.message(p, "&c" + args[0] + " &7could not be found.");
            return;
        }

        if (!tm.hasTeam(prof.getUniqueId())) {
            MessageManager.message(p, "&c" + prof.getName() + " &7is not in your team.");
            return;
        }

        if (!tm.getTeam(prof.getUniqueId()).equals(team)) {
            MessageManager.message(p, "&c" + prof.getName() + " &7is not in your team.");
            return;
        }

        if (prof.getUniqueId().equals(p.getUniqueId())) {
            MessageManager.message(p, "&7You cannot promote yourself.");
            return;
        }

        if (team.isManager(prof.getUniqueId())) {
            MessageManager.message(p, "&3" + prof.getName() + " &7is already a manager.");
            return;
        }

        team.getMembers().remove(prof.getUniqueId());
        team.getManagers().add(prof.getUniqueId());
        team.message("&3" + prof.getName() + " &7has been promoted by &3" + p.getName() + "&7.");
        MessageManager.message(prof.getUniqueId(), "&7You are now a manager of this team.");
    }
}