package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class TeamAsCommand extends BaseCommand {

    private TeamManager tm = kMain.getInstance().getTeamManager();
    private ProfileManager pm = kMain.getInstance().getProfileManager();

    public TeamAsCommand() {
        super("teamas", "kmain.teamas", CommandUsageBy.PlAYER);
        setUsage("&cImproper usage! /teamas <player> <hq/rally>");
        setMinArgs(1);
        setMaxArgs(2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        Profile prof = pm.getProfile(args[0]);

        if (prof == null) {
            MessageManager.message(p, "&c" + args[0] + " &7could not be found.");
            return;
        }

        if (!tm.hasTeam(prof.getUniqueId())) {
            MessageManager.message(p, "&c" + prof.getName() + " &7is not in a team.");
            return;
        }

        Team team = tm.getTeam(prof.getUniqueId());

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("hq")) {
                team.getHq().getChunk().load();
                p.teleport(team.getHq());
                MessageManager.message(p, "&7You cannot attack for 10 seconds.");
                kMain.getInstance().getLogger().log(Level.INFO, "[Admin Teleport]: " + p.getName() + " to team " + team.getName() + "'s HQ.");
            } else if (args[1].equalsIgnoreCase("rally")) {
                team.getRally().getChunk().load();
                p.teleport(team.getRally());
                MessageManager.message(p, "&7You cannot attack for 10 seconds.");
                kMain.getInstance().getLogger().log(Level.INFO, "[Admin Teleport]: " + p.getName() + " to team " + team.getName() + "'s Rally.");
            } else {
                MessageManager.message(p, "&cImproper usage! /teamas <player> <hq/rally>");
            }
        } else {
            MessageManager.message(p, "&cImproper usage! /teamas <player> <hq/rally>");
        }
    }
}
