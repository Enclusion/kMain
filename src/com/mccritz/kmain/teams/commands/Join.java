package com.mccritz.kmain.teams.commands;

import java.util.Collections;

import org.bukkit.entity.Player;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;

public class Join extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public Join() {
	super("join", Collections.singletonList("j"));
    }

    @Override
    public void execute(final Player p, String[] args) {
	if (args.length == 0 || args.length > 2) {
	    MessageManager.sendMessage(p, "&cImproper usage! /team join <name> <password>");
	    return;
	}

	if (tm.hasTeam(p.getUniqueId())) {
	    MessageManager.sendMessage(p, "&7You are already in a team.");
	    return;
	}

	if (tm.getTeam(args[0]) == null) {
	    MessageManager.sendMessage(p, "&7Could not find the team &3" + args[0] + "&7.");
	    return;
	}

	Team team = tm.getTeam(args[0]);

	if (args.length == 1) {
	    if (team.getPassword().isEmpty()) {
		MessageManager.sendMessage(p, "&7You have joined the team &3" + team.getName() + "&7.");
		team.sendMessage("&3" + p.getName() + " &7has joined the team.");
		team.getMembers().add(p.getUniqueId());
	    } else {
		MessageManager.sendMessage(p, "&7Incorrect password.");
		return;
	    }
	}

	if (args.length == 2) {
	    if (team.getPassword().isEmpty()) {
		MessageManager.sendMessage(p, "&7You have joined the team &3" + team.getName() + "&7.");
		team.sendMessage("&3" + p.getName() + " &7has joined the team.");
		team.getMembers().add(p.getUniqueId());
		return;
	    }

	    if (!args[1].equals(team.getPassword())) {
		MessageManager.sendMessage(p, "&7Incorrect password.");
		return;
	    }

	    MessageManager.sendMessage(p, "&7You have joined the team &3" + team.getName() + "&7.");
	    team.sendMessage("&3" + p.getName() + " &7has joined the team.");
	    team.getMembers().add(p.getUniqueId());
	}
    }
}
