package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.teams.TeamSubCommand;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class Demote extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    
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

        OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);

        if (op == null) {
            MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found!");
            return;
        }

        if (!tm.hasTeam(op.getUniqueId())) {
            MessageManager.sendMessage(p, "&c\"" + op.getName() + "\" is not in a team!");
            return;
        }

        if (!tm.getTeam(op.getUniqueId()).equals(team)) {
            MessageManager.sendMessage(p, "&c\"" + op.getName() + "\" is not in your team!");
            return;
        }

        if (!team.getManagers().contains(op.getUniqueId())) {
            MessageManager.sendMessage(p, "&c\"" + op.getName() + "\" is already a member!");
            return;
        }

        if (op.getName().equalsIgnoreCase(p.getName())) {
            MessageManager.sendMessage(p, "&cYou cannot demote yourself!");
            return;
        }

        team.getMembers().add(op.getUniqueId());
        team.getManagers().remove(op.getUniqueId());


        team.sendMessage("&b" + op.getName() + " &3was demoted by&b " + p.getName() + "&3!");
    }
}
