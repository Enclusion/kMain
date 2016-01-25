package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Roster extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public Roster() {
        super("roster", Arrays.asList("r"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length < 1) {
            MessageManager.sendMessage(p, "&cImproper usage! /team roster (team)");
            return;
        }

        if (args.length == 0) {
            if (!tm.hasTeam(p.getUniqueId())) {
                MessageManager.sendMessage(p, "&cYou are not in a team!");
                return;
            }

            tm.getTeam(p.getUniqueId()).getExtraInformation(p.getUniqueId());
        }

        if (args.length == 1) {
            if (tm.getTeam(args[0]) == null) {
                MessageManager.sendMessage(p, "&cThat team doesn't exist!");
                return;
            }

            if (tm.hasTeam(p.getUniqueId())) {
                if (tm.getTeam(p.getUniqueId()).equals(tm.getTeam(args[0]))) {
                    tm.getTeam(args[0]).getExtraInformation(p.getUniqueId());
                } else {
                    tm.getTeam(args[0]).getInformation(p.getUniqueId());
                }
            } else {
                tm.getTeam(args[0]).getInformation(p.getUniqueId());
            }
        }
    }
}
