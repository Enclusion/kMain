package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.teams.TeamSubCommand;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.entity.Player;

public class Rally extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public Rally() {
        super("rally");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou are not in a team!");
            return;
        }

        tm.getTeam(p.getUniqueId()).teleport(p, "rally");
    }
}
