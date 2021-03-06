package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;

public class Create extends TeamSubCommand {

    private TeamManager tm = kMain.getInstance().getTeamManager();
    
    public Create() {
        super("create");
    }

    @Override
    public void execute(final Player p, String[] args) {
        if (args.length == 0 || args.length > 2) {
            MessageManager.message(p, "&cImproper usage! /team create <name> <password>");
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
