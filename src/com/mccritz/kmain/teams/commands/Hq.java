package com.mccritz.kmain.teams.commands;

import org.bukkit.entity.Player;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;

public class Hq extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public Hq() {
	super("hq");
    }

    @Override
    public void execute(Player p, String[] args) {
	if (!tm.hasTeam(p.getUniqueId())) {
	    MessageManager.sendMessage(p, "&7You are not in a team.");
	    return;
	}

	tm.getTeam(p.getUniqueId()).teleport(p, "hq");
    }
}
