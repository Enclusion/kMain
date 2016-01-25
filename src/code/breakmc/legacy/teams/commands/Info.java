package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Info extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    private ProfileManager pm = Legacy.getInstance().getProfileManager();

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
            Profile prof = pm.getProfile(args[0]);

            if (prof == null) {
                MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found.");
                return;
            }

            if (!tm.hasTeam(prof.getUniqueId())) {
                MessageManager.sendMessage(p, "&c\"" + prof.getName() + "\" is not in a team!");
                return;
            }

            tm.getTeam(prof.getUniqueId()).getInformation(p.getUniqueId());
        }
    }
}
