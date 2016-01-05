package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.teams.TeamSubCommand;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Chat extends TeamSubCommand {

    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public Chat() {
        super("chat", Arrays.asList("c", "ch"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (tm.hasTeam(p.getUniqueId())) {
            if (tm.getTeamChatters().contains(p.getUniqueId())) {
                MessageManager.sendMessage(p, "&3Now speaking in public chat.");
                tm.getTeamChatters().remove(p.getUniqueId());
            } else {
                MessageManager.sendMessage(p, "&3Now speaking in team chat.");
                tm.getTeamChatters().add(p.getUniqueId());
            }
        } else {
            MessageManager.sendMessage(p, "&cYou are not in a team!");
        }
    }
}
