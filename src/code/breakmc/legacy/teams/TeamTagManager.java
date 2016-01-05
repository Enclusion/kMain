package code.breakmc.legacy.teams;

import code.BreakMC.origin.manager.ProfileGroupsManager;
import code.BreakMC.origin.util.object.ProfileGroup;
import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.PacketHandler;
import code.breakmc.legacy.utils.PlayerUtility;
import code.breakmc.legacy.utils.ScoreboardTeamPacketMod;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Calvin on 5/4/2015.
 */
public class TeamTagManager {

    private Legacy main = Legacy.getInstance();
    private TeamManager tm = main.getTeamManager();
    private List<TeamInfo> registeredTeams = new ArrayList<>();
    private int teamCreateIndex = 1;
    private HashMap<String, HashMap<String, TeamInfo>> teamMap = new HashMap<>();

    public void reloadPlayer(Player toRefresh) {
        for (Player refreshFor : PlayerUtility.getOnlinePlayers()) {
            reloadPlayer(toRefresh, refreshFor);
        }
    }

    public void reloadPlayer(Player toRefresh, Player refreshFor) {
        ProfileGroup group = ProfileGroupsManager.getInstance().getPlayersGroup(toRefresh);
        TeamInfo teamInfo;

        if ((tm.getTeam(toRefresh.getUniqueId()) != null && tm.getTeam(refreshFor.getUniqueId()) != null) && (tm.getTeam(toRefresh.getUniqueId()) == tm.getTeam(refreshFor.getUniqueId()))) {
            double health = toRefresh.getHealth() / 2;

            if (health == 10) {
                teamInfo = getOrCreate(ChatColor.translateAlternateColorCodes('&', "&b\u2764 &3"), "");
            } else if (health < 10 && health >= 7) {
                teamInfo = getOrCreate(ChatColor.translateAlternateColorCodes('&', "&a\u2764 &3"), "");
            } else if (health <= 7 && health >= 4) {
                teamInfo = getOrCreate(ChatColor.translateAlternateColorCodes('&', "&e\u2764 &3"), "");
            } else {
                teamInfo = getOrCreate(ChatColor.translateAlternateColorCodes('&', "&c\u2764 &3"), "");
            }
        } else {
            if (group.getMainColor() != null) {
                teamInfo = getOrCreate(ChatColor.translateAlternateColorCodes('&', group.getMainColor()), "");
            } else {
                teamInfo = getOrCreate(ChatColor.translateAlternateColorCodes('&', ""), "");
            }
        }

        HashMap<String, TeamInfo> teamInfoMap = new HashMap<>();

        if (teamMap.containsKey(refreshFor.getName())) {
            teamInfoMap = teamMap.get(refreshFor.getName());

            if (teamInfoMap.containsKey(toRefresh.getName())) {
                TeamInfo tem = teamInfoMap.get(toRefresh.getName());

                if (tem != teamInfo) {
                    sendPacketsRemoveFromTeam(tem, toRefresh.getName(), refreshFor);
                    teamInfoMap.remove(toRefresh.getName());
                }
            }
        }

        sendPacketsAddToTeam(teamInfo, new String[]{toRefresh.getName()}, refreshFor);
        teamInfoMap.put(toRefresh.getName(), teamInfo);
        teamMap.put(refreshFor.getName(), teamInfoMap);
    }

    public void clearFromTeam(Player toRefresh) {
        for (Player refreshFor : PlayerUtility.getOnlinePlayers()) {
            HashMap<String, TeamInfo> teamInfoMap;

            if (teamMap.containsKey(refreshFor.getName())) {
                teamInfoMap = teamMap.get(refreshFor.getName());

                if (teamInfoMap.containsKey(toRefresh.getName())) {
                    TeamInfo tem = teamInfoMap.get(toRefresh.getName());
                    sendPacketsRemoveFromTeam(tem, toRefresh.getName(), refreshFor);
                    teamInfoMap.remove(toRefresh.getName());
                }
            }
        }
    }

    public void sendPacketsRemoveTeam(TeamInfo team) {
        try {
            for (Player p : PlayerUtility.getOnlinePlayers()) {
                if (p != null) {
                    PacketHandler mod = new PacketHandler(team.getName(), team.getPrefix(), team.getSuffix(), new ArrayList<String>(), 1);
                    mod.sendToPlayer(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initPlayer(Player player) {
        for (TeamInfo teamInfo : registeredTeams) {
            sendPacketsAddTeam(teamInfo, player);
        }
    }

    public TeamInfo getOrCreate(String prefix, String suffix) {
        for (TeamInfo teamInfo : registeredTeams) {
            if ((teamInfo.getPrefix().equals(prefix)) && (teamInfo.getSuffix().equals(suffix))) {
                return teamInfo;
            }
        }

        TeamInfo newTeam = new TeamInfo(String.valueOf(teamCreateIndex), prefix, suffix);
        teamCreateIndex += 1;
        registeredTeams.add(newTeam);

        for (Player player : PlayerUtility.getOnlinePlayers()) {
            sendPacketsAddTeam(newTeam, player);
        }

        return newTeam;
    }

    public void sendTeamsToPlayer(Player player) {
        for (Player toRefresh : PlayerUtility.getOnlinePlayers()) {
            reloadPlayer(toRefresh, player);
        }
    }

    public void sendPacketsAddTeam(TeamInfo team, Player p) {
        try {
            new ScoreboardTeamPacketMod(team.getName(), team.getPrefix(), team.getSuffix(), new ArrayList(), 0).sendToPlayer(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacketsRemoveFromTeam(TeamInfo team, String player) {
        boolean cont = false;

        for (TeamInfo t : teamMap.get(player).values()) {
            if (t == team) {
                cont = true;
            }
        }

        if (!cont) {
            return;
        }

        try {
            for (Player p : PlayerUtility.getOnlinePlayers()) {
                if (p != null) {
                    PacketHandler mod = new PacketHandler(team.getName(), Collections.singletonList(player), 4);

                    mod.sendToPlayer(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacketsAddToTeam(TeamInfo team, String[] player, Player p) {
        try {
            new ScoreboardTeamPacketMod(team.getName(), Arrays.asList(player), 3).sendToPlayer(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacketsRemoveFromTeam(TeamInfo team, String player, Player tp) {
        try {
            new ScoreboardTeamPacketMod(team.getName(), Collections.singletonList(player), 4).sendToPlayer(tp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    public String getMainColor(Player p) {
//        if (p.isOp()) {
//            return "&4";
//        } else {
//            if (p.hasPermission("tag.member")) {
//                return "&7";
//            } else if (p.hasPermission("tag.premium")) {
//                return "&9";
//            } else if (p.hasPermission("tag.vip")) {
//                return "&2";
//            } else if (p.hasPermission("tag.pro")) {
//                return "&6";
//            } else if (p.hasPermission("tag.legend")) {
//                return "&a";
//            } else if (p.hasPermission("tag.master")) {
//                return "&e";
//            } else if (p.hasPermission("tag.infamous")) {
//                return "&d&l";
//            } else if (p.hasPermission("tag.advanced")) {
//                return "&b&l";
//            } else if (p.hasPermission("tag.extreme")) {
//                return "&6&l";
//            } else {
//                return "&7";
//            }
//        }
//    }

    public HashMap<String, HashMap<String, TeamInfo>> getTeamMap() {
        return teamMap;
    }

    public List<TeamInfo> getAllTeams() {
        return registeredTeams;
    }
}
