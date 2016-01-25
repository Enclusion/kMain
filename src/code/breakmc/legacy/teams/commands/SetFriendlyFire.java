package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.MessageManager;
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
            MessageManager.sendMessage(p, "&cYou are not on a team!");
            return;
        }

        Team team = tm.getTeam(p.getUniqueId());

        if (!team.isManager(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou must be a manager to perform this command!");
            return;
        }

        if (args.length == 0) {
            if (team.isFriendlyFireEnabled()) {
                team.setFriendlyFireEnabled(false);
                team.sendMessage("&b" + p.getName() + " &3has disabled friendly fire!");
            } else {
                team.setFriendlyFireEnabled(true);
                team.sendMessage("&b" + p.getName() + " &3has enabled friendly fire!");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true")) {
                if (team.isFriendlyFireEnabled()) {
                    MessageManager.sendMessage(p, "&cFriendly fire is already enabled!");
                    return;
                }

                team.setFriendlyFireEnabled(true);
                team.sendMessage("&b" + p.getName() + " &3has enabled friendly fire!");
            } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("false")) {
                if (!team.isFriendlyFireEnabled()) {
                    MessageManager.sendMessage(p, "&cFriendly fire is already disabled!");
                    return;
                }

                team.setFriendlyFireEnabled(false);
                team.sendMessage("&b" + p.getName() + " &3has disabled friendly fire!");
            } else {
                MessageManager.sendMessage(p, "&cImproper usage! /team ff <on/off>");
            }
        } else {
            MessageManager.sendMessage(p, "&cImproper usage! /team ff <on/off>");
        }
    }
}
