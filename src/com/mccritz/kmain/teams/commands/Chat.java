package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Chat extends TeamSubCommand {

    private TeamManager tm = kMain.getInstance().getTeamManager();

    public Chat() {
        super("chat", Arrays.asList("c", "ch"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (tm.hasTeam(p.getUniqueId())) {
            if (tm.getTeamChatters().contains(p.getUniqueId())) {
                MessageManager.message(p, "&3You are now in global chat.");
                tm.getTeamChatters().remove(p.getUniqueId());
            } else {
                MessageManager.message(p, "&3You are now in team chat.");
                tm.getTeamChatters().add(p.getUniqueId());
            }
        } else {
            MessageManager.message(p, "&7You are not in a team.");
        }
    }
}
