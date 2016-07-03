package com.mccritz.kmain.teams;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.utils.PacketHandler;
import com.mccritz.kmain.utils.PlayerUtility;
import com.mccritz.kmain.utils.ScoreboardTeamPacketMod;
import com.mccritz.kmain.utils.TeamInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

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
        TeamInfo teamInfo;

//        if ((tm.getTeam(toRefresh.getUniqueId()) != null && tm.getTeam(refreshFor.getUniqueId()) != null) && (tm.getTeam(toRefresh.getUniqueId()) == tm.getTeam(refreshFor.getUniqueId()))) {
//            teamInfo = getOrCreate(ChatColor.translateAlternateColorCodes('&', "&a"), "");
//        } else {
//            teamInfo = getOrCreate(ChatColor.translateAlternateColorCodes('&', "&e"), "");
//        }
        teamInfo = getOrCreate(ChatColor.translateAlternateColorCodes('&', "&f"), "");

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

    public HashMap<String, HashMap<String, TeamInfo>> getTeamMap() {
        return teamMap;
    }

    public List<TeamInfo> getAllTeams() {
        return registeredTeams;
    }
}
