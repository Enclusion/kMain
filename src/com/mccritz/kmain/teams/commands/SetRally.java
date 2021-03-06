package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetRally extends TeamSubCommand {

    private TeamManager tm = kMain.getInstance().getTeamManager();

    public SetRally() {
        super("setrally", true, Arrays.asList("sr", "setr"), false);
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.message(p, "&7You are not in a team.");
            return;
        }

        if (!tm.getTeam(p.getUniqueId()).isManager(p.getUniqueId())) {
            MessageManager.message(p, "&7You must be a &3manager &7of this team to do that.");
            return;
        }

        tm.getTeam(p.getUniqueId()).message("&3" + p.getName() + " &7has updated the team rally.");
        tm.getTeam(p.getUniqueId()).setRally(p.getLocation());
    }
}
