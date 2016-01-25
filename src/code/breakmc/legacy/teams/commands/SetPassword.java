package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.MessageManager;
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
            MessageManager.sendMessage(p, "&cYou are not in a team!");
            return;
        }

        Team team = tm.getTeam(p.getUniqueId());

        if (args.length == 0) {
            if (team.getPassword().equalsIgnoreCase("")) {
                MessageManager.sendMessage(p, "&3Your team does not have a password set.");
                return;
            }

            MessageManager.sendMessage(p, "&3Team password&7: &b" + team.getPassword());
        } else if (args.length == 1) {
            if (!team.isManager(p.getUniqueId())) {
                MessageManager.sendMessage(p, "&cYou must be a manager to perform this command!");
                return;
            }

            if (args[0].equalsIgnoreCase("none") || args[0].equalsIgnoreCase("null") || args[0].equalsIgnoreCase("nil")) {
                team.setPassword("");
                team.sendMessage("&b" + p.getName() + " &3has turned off password protection.");
                return;
            }

            team.setPassword(args[0]);

            team.sendMessage("&b" + p.getName() + " &3has set the password to&b " + team.getPassword());
        } else {
            MessageManager.sendMessage(p, "&cImproper usage! /team password (password/none)");
        }
    }
}
