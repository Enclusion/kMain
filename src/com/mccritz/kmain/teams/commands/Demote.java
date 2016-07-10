package com.mccritz.kmain.teams.commands;

import java.util.Collections;

import org.bukkit.entity.Player;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;

public class Demote extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    private ProfileManager pm = Legacy.getInstance().getProfileManager();

    public Demote() {
	super("demote", true, Collections.singletonList("d"));
    }

    @Override
    public void execute(Player p, String[] args) {
	if (args.length != 1) {
	    MessageManager.sendMessage(p, "&cImproper usage! /team demote <player>");
	    return;
	}

	if (!tm.hasTeam(p.getUniqueId())) {
	    MessageManager.sendMessage(p, "&7You are not in a team.");
	    return;
	}

	Team team = tm.getTeam(p.getUniqueId());

	if (!team.isManager(p.getUniqueId())) {
	    MessageManager.sendMessage(p, "&7You must be a &3manager &7of this team to do that.");
	    return;
	}

	Profile prof = pm.getProfile(args[0]);

	if (prof == null) {
	    MessageManager.sendMessage(p, "&c" + args[0] + " &7could not be found.");
	    return;
	}

	if (!tm.hasTeam(prof.getUniqueId())) {
	    MessageManager.sendMessage(p, "&c" + prof.getName() + " &7is not in a team.");
	    return;
	}

	if (!tm.getTeam(prof.getUniqueId()).equals(team)) {
	    MessageManager.sendMessage(p, "&c" + prof.getName() + " &7is not in your team.");
	    return;
	}

	if (!team.getManagers().contains(prof.getUniqueId())) {
	    MessageManager.sendMessage(p, "&c" + prof.getName() + " &7is already a member.");
	    return;
	}

	if (prof.getName().equalsIgnoreCase(p.getName())) {
	    MessageManager.sendMessage(p, "&7You cannot demote yourself.");
	    return;
	}

	team.getMembers().add(prof.getUniqueId());
	team.getManagers().remove(prof.getUniqueId());

	team.sendMessage("&3" + prof.getName() + " &7was demoted by &3" + p.getName() + "&7.");
    }
}
