package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetHq extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    
    public SetHq() {
        super("sethq", true, Arrays.asList("shq", "seth"), false);
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&7You are not in a team.");
            return;
        }

        if (!tm.getTeam(p.getUniqueId()).isManager(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&7You must be a &3manager &7of this team to do that.");
            return;
        }

        tm.getTeam(p.getUniqueId()).sendMessage("&3" + p.getName() + " &7has updated the team HQ&7.");
        tm.getTeam(p.getUniqueId()).setHq(p.getLocation());
    }
}
