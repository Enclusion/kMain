package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.teams.TeamSubCommand;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Debug extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public Debug() {
        super("debug", Arrays.asList("db"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!p.hasPermission("legacy.debug")) {
            MessageManager.sendMessage(p, "&cThis command is for developers only c:, silly goose.");
            return;
        }


    }
}
