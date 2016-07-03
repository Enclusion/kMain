package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetPassword extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    
    public SetPassword() {
        super("pass", true, Arrays.asList("spw", "spwd", "setpw", "setpwd", "pass", "password", "setpassword"), false);
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&7You are not in a team.");
            return;
        }

        Team team = tm.getTeam(p.getUniqueId());

        if (args.length == 0) {
            if (team.getPassword().equalsIgnoreCase("")) {
                MessageManager.sendMessage(p, "&7Your team does not have a password.");
                return;
            }

            MessageManager.sendMessage(p, "&7The teams password is &3" + team.getPassword() + "&7.");
        } else if (args.length == 1) {
            if (!team.isManager(p.getUniqueId())) {
                MessageManager.sendMessage(p, "&7You must be a &3manager &7of this team to do that.");
                return;
            }

            if (args[0].equalsIgnoreCase("none") || args[0].equalsIgnoreCase("null") || args[0].equalsIgnoreCase("nil") || args[0].equalsIgnoreCase("off")) {
                team.setPassword("");
                team.sendMessage("&7You have removed the password for your team.");
                return;
            }

            team.setPassword(args[0]);

            team.sendMessage("&3" + p.getName() + " &7has set the password to &3" + args[0] + "&7.");
        } else {
            MessageManager.sendMessage(p, "&cImproper usage! /team password <password/none>");
        }
    }
}
