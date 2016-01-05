package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.teams.TeamSubCommand;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Promote extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    private ProfileManager pm = Legacy.getInstance().getProfileManager();

    public Promote() {
        super("promote", true, Arrays.asList("p"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 1) {
            MessageManager.sendMessage(p, "&cImproper usage! /team promote (player)");
            return;
        }

        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou are not in a team!");
            return;
        }

        Team team = tm.getTeam(p.getUniqueId());

        if (!team.isManager(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou must be a manager to perform this command.");
            return;
        }

        Profile prof = pm.getProfile(args[0]);

        if (prof == null) {
            MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found.");
            return;
        }

        if (!tm.hasTeam(prof.getUniqueId())) {
            MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is not in your team!");
            return;
        }

        if (!tm.getTeam(prof.getUniqueId()).equals(team)) {
            MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is not in your team!");
            return;
        }

        if (prof.getUniqueId().equals(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou cannot promote yourself!");
            return;
        }

        if (team.isManager(prof.getUniqueId())) {
            MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is already a manager!");
            return;
        }

        team.getMembers().remove(prof.getUniqueId());
        team.getManagers().add(prof.getUniqueId());
        team.sendMessage("&b" + prof.getName() + " &3has been promoted to manager by &b" + p.getName() + "&3!");
    }
}