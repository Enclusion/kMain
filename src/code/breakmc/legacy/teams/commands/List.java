package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.teams.TeamSubCommand;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class List extends TeamSubCommand {

    private Legacy main = Legacy.getInstance();
    private TeamManager tm = main.getTeamManager();
    private ProfileManager pm = main.getProfileManager();

    public List() {
        super("list");
    }

    @Override
    public void execute(final Player p, String[] args) {
        new BukkitRunnable() {
            public void run() {
                HashMap<Team, Integer> map = new HashMap<>();

                for (Team team : tm.getTeams()) {
                    map.put(team, (team.getMembers().size() + team.getManagers().size()));
                }

                MessageManager.sendMessage(p, "&7&m***&r &3Teams &7(&b" + tm.getTeams().size() + "&7) &7&m***");

                Object[] a = map.entrySet().toArray();
                Arrays.sort(a, (o1, o2) -> ((Map.Entry<Team, Integer>) o2).getValue().compareTo(((Map.Entry<Team, Integer>) o1).getValue()));

                int topten = 0;
                for (Object e : a) {
                    if (topten <= 4) {
                        MessageManager.sendMessage(p, "&b" + ((Map.Entry<Team, Integer>) e).getKey().getName() + " &7(&a" + ((Map.Entry<Team, Integer>) e).getValue() + "&7/30)");
                    }
                    topten++;
                }
            }
        }.runTaskAsynchronously(main);
    }
}
