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

public class Kick extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    
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

        if (Bukkit.getPlayer(args[0]) != null) {
            Player target = Bukkit.getPlayer(args[0]);

            if (!tm.hasTeam(target.getUniqueId())) {
                MessageManager.sendMessage(p, "&c\"" + target.getName() + "\" is not in your team!");
                return;
            }

            if (!tm.getTeam(target.getUniqueId()).equals(team)) {
                MessageManager.sendMessage(p, "&c\"" + target.getName() + "\" is not in your team!");
                return;
            }

            if (team.getMembers().contains(target.getUniqueId())) {
                team.getMembers().remove(target.getUniqueId());
            } else {
                team.getManagers().remove(target.getUniqueId());
            }

            MessageManager.sendMessage(target, "&3You have been kicked from &b" + team.getName() + "&3!");
            team.sendMessage("&b" + p.getName() + " &3has kicked&b " + target.getName() + " &3from the team!");

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
        } else {
            if (Bukkit.getOfflinePlayer(args[0]) != null) {
                OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);

                if (!tm.hasTeam(op.getUniqueId())) {
                    MessageManager.sendMessage(p, "&c\"" + op.getName() + "\" is not in your team!");
                    return;
                }

                if (!tm.getTeam(op.getUniqueId()).equals(team)) {
                    MessageManager.sendMessage(p, "&c\"" + op.getName() + "\" is not in your team!");
                    return;
                }

                if (team.getMembers().contains(op.getUniqueId())) {
                    team.getMembers().remove(op.getUniqueId());
                } else {
                    team.getManagers().remove(op.getUniqueId());
                }
                MessageManager.sendMessage(op.getUniqueId(), "&3You have been kicked from &b" + team.getName() + "&3!");
                team.sendMessage("&b" + p.getName() + " &3has kicked &b" + op.getName() + " &3from the team!");
            } else {
                MessageManager.sendMessage(p, "&c" + args[0] + " could not be found!");
            }
        }
    }
}
