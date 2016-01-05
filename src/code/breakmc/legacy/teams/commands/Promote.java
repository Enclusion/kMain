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

public class Promote extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

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

            if (target.getUniqueId().equals(p.getUniqueId())) {
                MessageManager.sendMessage(p, "&cYou cannot promote yourself!");
                return;
            }

            if (team.isManager(target.getUniqueId())) {
                MessageManager.sendMessage(p, "&c\"" + target.getName() + "\" is already a manager!");
                return;
            }

            team.getMembers().remove(target.getUniqueId());
            team.getManagers().add(target.getUniqueId());
            team.sendMessage("&b" + target.getName() + " &3has been promoted to manager by &b" + p.getName() + "&3!");
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

                if (op.getUniqueId().equals(p.getUniqueId())) {
                    MessageManager.sendMessage(p, "&cYou cannot promote yourself!");
                    return;
                }

                if (team.isManager(op.getUniqueId())) {
                    MessageManager.sendMessage(p, "&c\"" + op.getName() + "\" is already a manager!");
                    return;
                }

                team.getMembers().remove(op.getUniqueId());
                team.getManagers().add(op.getUniqueId());
                team.sendMessage("&b" + op.getName() + " &3has been promoted to manager by &b" + p.getName() + "&3!");
            } else {
                MessageManager.sendMessage(p, "&c" + args[0] + " could not be found!");
            }
        }
    }
}