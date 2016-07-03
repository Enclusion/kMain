package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetFriendlyFire extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    
    public SetFriendlyFire() {
        super("ff", true, Arrays.asList("friendlyfire", "sff", "setfriendlyfire", "setff"), false);
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&7You are not in a team.");
            return;
        }

        Team team = tm.getTeam(p.getUniqueId());

        if (!team.isManager(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&7You must be a &3manager &7of this team to do that.");
            return;
        }

        if (args.length == 0) {
            if (team.isFriendlyFireEnabled()) {
                team.setFriendlyFireEnabled(false);
                team.sendMessage("&3" + p.getName() + " &7has disabled friendly fire.");
            } else {
                team.setFriendlyFireEnabled(true);
                team.sendMessage("&3" + p.getName() + " &7has enabled friendly fire.");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true")) {
                if (team.isFriendlyFireEnabled()) {
                    MessageManager.sendMessage(p, "&7Friendly fire is already enabled.");
                    return;
                }

                team.setFriendlyFireEnabled(true);
                team.sendMessage("&3" + p.getName() + " &7has enabled friendly fire.");
            } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("false")) {
                if (!team.isFriendlyFireEnabled()) {
                    MessageManager.sendMessage(p, "&7Friendly fire is already disabled.");
                    return;
                }

                team.setFriendlyFireEnabled(false);
                team.sendMessage("&3" + p.getName() + " &7has disabled friendly fire.");
            } else {
                MessageManager.sendMessage(p, "&cImproper usage! /team ff <on/off>");
            }
        } else {
            MessageManager.sendMessage(p, "&cImproper usage! /team ff <on/off>");
        }
    }
}
