package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.teams.TeamSubCommand;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetHq extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    
    public SetHq() {
        super("sethq", true, Arrays.asList("shq", "seth"), false);
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!tm.hasTeam(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou are not in a team!");
            return;
        }

        if (!tm.getTeam(p.getUniqueId()).isManager(p.getUniqueId())) {
            MessageManager.sendMessage(p, "&cYou must be a manager to perform this command!");
            return;
        }

        tm.getTeam(p.getUniqueId()).sendMessage("&b" + p.getName() + " &3has updated the team's HQ!");
        tm.getTeam(p.getUniqueId()).setHq(p.getLocation());
    }
}
