package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.teams.TeamSubCommand;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Info extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public Info() {
        super("info", Arrays.asList("i"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length > 1) {
            MessageManager.sendMessage(p, "&cImproper usage! /team info (player)");
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
            if (Bukkit.getPlayer(args[0]) == null) {
                MessageManager.sendMessage(p, "&cCould not find player \"" + args[0] + "\"!");
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (!tm.hasTeam(target.getUniqueId())) {
                MessageManager.sendMessage(p, "&c\"" + Bukkit.getPlayer(args[0]).getName() + "\" is not in a team!");
                return;
            }

            tm.getTeam(target.getUniqueId()).getInformation(p.getUniqueId());
        }
    }
}
