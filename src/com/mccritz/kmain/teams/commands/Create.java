package com.mccritz.kmain.teams.commands;

import org.bukkit.entity.Player;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;

public class Create extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public Create() {
	super("create");
    }

    @Override
    public void execute(final Player p, String[] args) {
	if (args.length == 0 || args.length > 2) {
	    MessageManager.sendMessage(p, "&cImproper usage! /team create <name> <password>");
	    return;
	}

	if (args.length == 1) {
	    tm.createTeam(p.getUniqueId(), args[0], "");
	    return;
	}

	if (args.length == 2) {
	    tm.createTeam(p.getUniqueId(), args[0], args[1]);
	}
    }
}
