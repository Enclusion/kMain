package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class List extends TeamSubCommand {

    private kMain main = kMain.getInstance();
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

                MessageManager.message(p, "&7*** &3Teams &7(&3" + tm.getTeams().size() + "&7) ***");

                Object[] a = map.entrySet().toArray();
                Arrays.sort(a, (o1, o2) -> ((Map.Entry<Team, Integer>) o2).getValue().compareTo(((Map.Entry<Team, Integer>) o1).getValue()));

                int topten = 0;
                for (Object e : a) {
                    if (topten <= 4) {
                        MessageManager.message(p, "&3" + ((Map.Entry<Team, Integer>) e).getKey().getName() + " &7(&a" + ((Map.Entry<Team, Integer>) e).getValue() + "&7/30)");
                    }
                    topten++;
                }
            }
        }.runTaskAsynchronously(main);
    }
}
