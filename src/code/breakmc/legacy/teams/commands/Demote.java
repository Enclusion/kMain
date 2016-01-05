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

public class Demote extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    private ProfileManager pm = Legacy.getInstance().getProfileManager();
    
    public Demote() {
        super("demote", true, Arrays.asList("d"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 1) {
            MessageManager.sendMessage(p, "&cImproper usage! /team demote (player)");
            return;
        }

        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou are not on a team!");
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
            MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is not in a team!");
            return;
        }

        if (!tm.getTeam(prof.getUniqueId()).equals(team)) {
            MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is not in your team!");
            return;
        }

        if (!team.getManagers().contains(prof.getUniqueId())) {
            MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is already a member!");
            return;
        }

        if (prof.getName().equalsIgnoreCase(p.getName())) {
            MessageManager.sendMessage(p, "&cYou cannot demote yourself!");
            return;
        }

        team.getMembers().add(prof.getUniqueId());
        team.getManagers().remove(prof.getUniqueId());


        team.sendMessage("&b" + prof.getName() + " &3was demoted by&b " + p.getName() + "&3!");
    }
}
