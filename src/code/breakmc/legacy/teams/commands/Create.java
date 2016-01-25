package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.entity.Player;

public class Create extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    
    public Create() {
        super("create");
    }

    @Override
    public void execute(final Player p, String[] args) {
        if (args.length == 0 || args.length > 2) {
            MessageManager.sendMessage(p, "&cImproper usage! /team create (name) (password)");
            return;
        }

        if (args.length == 1) {
            tm.createTeam(p.getUniqueId(), args[0], "");
            return;
        }

        if (args.length == 2) {
            tm.createTeam(p.getUniqueId(), args[0], args[1]);
        }
    }
}
