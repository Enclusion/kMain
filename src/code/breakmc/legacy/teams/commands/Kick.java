package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.teams.TeamSubCommand;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class Kick extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    private ProfileManager pm = Legacy.getInstance().getProfileManager();

    public Kick() {
        super("kick", true, Arrays.asList("k"), false);
    }

    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length == 0 || args.length > 1) {
            MessageManager.sendMessage(p, "&cImproper usage! /team kick (player)");
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
            MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is not in your team!");
            return;
        }

        if (!tm.getTeam(prof.getUniqueId()).equals(team)) {
            MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is not in your team!");
            return;
        }

        if (team.getMembers().contains(prof.getUniqueId())) {
            team.getMembers().remove(prof.getUniqueId());
        } else {
            team.getManagers().remove(prof.getUniqueId());
        }

        MessageManager.sendMessage(prof.getUniqueId(), "&3You have been kicked from &b" + team.getName() + "&3!");
        team.sendMessage("&b" + p.getName() + " &3has kicked&b " + prof.getName() + " &3from the team!");

        Legacy.getInstance().getTeamTagManager().reloadPlayer(p);

        for (UUID id : team.getMembers()) {
            if (Bukkit.getPlayer(id) != null) {
                Legacy.getInstance().getTeamTagManager().reloadPlayer(Bukkit.getPlayer(id));
            }
        }

        for (UUID id : team.getManagers()) {
            if (Bukkit.getPlayer(id) != null) {
                Legacy.getInstance().getTeamTagManager().reloadPlayer(Bukkit.getPlayer(id));
            }
        }
    }
}
