package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Info extends TeamSubCommand {

    private TeamManager tm = kMain.getInstance().getTeamManager();
    private ProfileManager pm = kMain.getInstance().getProfileManager();

    public Info() {
        super("info", Collections.singletonList("i"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length > 1) {
            MessageManager.message(p, "&cImproper usage! /team info <player>");
            return;
        }

        if (args.length == 0) {
            if (!tm.hasTeam(p.getUniqueId())) {
                MessageManager.message(p, "&7You are not in a team.");
                return;
            }

            tm.getTeam(p.getUniqueId()).getExtraInformation(p.getUniqueId());
        }

        if (args.length == 1) {
            Profile prof = pm.getProfile(args[0]);

            if (prof == null) {
                MessageManager.message(p, "&c" + args[0] + " &7could not be found.");
                return;
            }

            if (!tm.hasTeam(prof.getUniqueId())) {
                MessageManager.message(p, "&c" + prof.getName() + " &7is not in a team.");
                return;
            }

            tm.getTeam(prof.getUniqueId()).getInformation(p.getUniqueId());
        }
    }
}
