package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;

public class Rally extends TeamSubCommand {

    private TeamManager tm = kMain.getInstance().getTeamManager();

    public Rally() {
        super("rally");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.message(p, "&7You are not in a team.");
            return;
        }

        tm.getTeam(p.getUniqueId()).teleport(p, "rally");
    }
}
