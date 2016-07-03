package com.mccritz.kmain.teams;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.utils.LocationSerialization;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class TeamManager {

    private Legacy main = Legacy.getInstance();
    private HashSet<Team> teams = new HashSet<>();
    private List<UUID> teamChatters = new ArrayList<>();
    private MongoCollection<Document> teamCollection = main.getMongoDatabase().getCollection("teams");

    public TeamManager() {
        loadTeams();

        new BukkitRunnable() {
            @Override
            public void run() {
                saveTeams();
            }
        }.runTaskTimerAsynchronously(main, 0L, 300 * 20L);
    }

    public void loadTeams() {
        main.getLogger().log(Level.INFO, "Preparing to load " + teamCollection.count() + " teams.");

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Document document : teamCollection.find()) {
                    String name = document.getString("name");
                    Location hq = null;
                    Location rally = null;

                    HashSet<UUID> managerList = new HashSet<>();
                    List<String> managerStrings = (List<String>) document.get("managers");
                    for (String s : managerStrings) {
                        managerList.add(UUID.fromString(s));
                    }

                    HashSet<UUID> memberList = new HashSet<>();
                    List<String> memberStrings = (List<String>) document.get("members");
                    for (String s : memberStrings) {
                        managerList.add(UUID.fromString(s));
                    }

                    if (document.getString("hq") != null) {
                        if (LocationSerialization.deserializeLocation(document.getString("hq")) != null) {
                            hq = LocationSerialization.deserializeLocation(document.getString("hq"));
                        }
                    }

                    if (document.getString("rally") != null) {
                        if (LocationSerialization.deserializeLocation(document.getString("rally")) != null) {
                            rally = LocationSerialization.deserializeLocation(document.getString("rally"));
                        }
                    }

                    boolean friendlyfire = document.getBoolean("friendlyfire");
                    String password = document.getString("password");

                    Team team = new Team(name);
                    team.setManagers(managerList);
                    team.setMembers(memberList);
                    team.setHq(hq);
                    team.setRally(rally);
                    team.setFriendlyFireEnabled(friendlyfire);
                    team.setPassword(password);
                    teams.add(team);
                }

                main.getLogger().log(Level.INFO, "Successfully loaded " + teams.size() + " teams.");
            }
        }.runTaskAsynchronously(main);
    }

    public void saveTeams() {
        Bukkit.getLogger().log(Level.INFO, "Preparing to save " + getTeams().size() + " teams.");

        for (Team team : getTeams()) {
            Document document = new Document("name", team.getName());

            List<String> managerStrings = new ArrayList<>();
            for (UUID id : team.getManagers()) {
                managerStrings.add(id.toString());
            }

            document.append("managers", managerStrings);

            List<String> memberStrings = new ArrayList<>();
            for (UUID id : team.getMembers()) {
                memberStrings.add(id.toString());
            }

            document.append("members", memberStrings);

            if (team.getHq() != null) {
                document.append("hq", LocationSerialization.serializeLocation(team.getHq()));
            }

            if (team.getRally() != null) {
                document.append("rally", LocationSerialization.serializeLocation(team.getRally()));
            }

            document.append("friendlyfire", team.isFriendlyFireEnabled());
            document.append("password", team.getPassword());

            new BukkitRunnable() {
                @Override
                public void run() {
                    Document previousDocument = teamCollection.find(Filters.eq("name", team.getName())).first();
                    if (previousDocument != null) {
                        teamCollection.findOneAndReplace(previousDocument, document, new FindOneAndReplaceOptions().upsert(true));
                    } else {
                        teamCollection.insertOne(document);
                    }
                }
            }.runTaskAsynchronously(main);
        }

        main.getLogger().log(Level.INFO, "Successfully saved " + teamCollection.count() + " teams.");
    }

    public void createTeam(UUID id, String name, String password) {
        if (hasTeam(id)) {
            MessageManager.sendMessage(id, "&7YOu are already in a team.");
            return;
        }

        for (Team teams : getTeams()) {
            if (name.equalsIgnoreCase(teams.getName())) {
                MessageManager.sendMessage(id, "&7That team name is already in use.");
                return;
            }
        }

        if (!name.matches("^[A-Za-z0-9_+-]*$")) {
            MessageManager.sendMessage(id, "&7Your team name must be alphanumerical.");
            return;
        }

        if (name.length() < 3) {
            MessageManager.sendMessage(id, "&7Your team name must have at least 2 characters.");
            return;
        } else if (name.length() > 14) {
            MessageManager.sendMessage(id, "&7Team names have a limit of 14 characters.");
            return;
        }

        Team team = new Team(name);
        team.setPassword(password.equalsIgnoreCase("") ? "" : password);
        team.getManagers().add(id);

        MessageManager.sendMessage(id, "&7You have created the team &3" + name + "&7.");

        teams.add(team);

        PlayerUtility.updateScoreboard(Bukkit.getPlayer(id));

        Document document = new Document("name", team.getName());

        List<String> managerStrings = new ArrayList<>();
        for (UUID uuid : team.getManagers()) {
            managerStrings.add(uuid.toString());
        }

        document.append("managers", managerStrings);

        List<String> memberStrings = new ArrayList<>();
        for (UUID uuid : team.getMembers()) {
            memberStrings.add(uuid.toString());
        }

        document.append("members", memberStrings);

        if (team.getHq() != null) {
            document.append("hq", LocationSerialization.serializeLocation(team.getHq()));
        }

        if (team.getRally() != null) {
            document.append("rally", LocationSerialization.serializeLocation(team.getRally()));
        }

        document.append("friendlyfire", team.isFriendlyFireEnabled());
        document.append("password", team.getPassword());

        new BukkitRunnable() {
            @Override
            public void run() {
                teamCollection.insertOne(document);
            }
        }.runTaskAsynchronously(main);
    }

    public void deleteTeam(Team team) {
        teams.remove(team);

        for (Player all : team.getOnlinePlayers()) {
            PlayerUtility.updateScoreboard(all);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Document document = teamCollection.find(Filters.eq("name", team.getName())).first();
                if (document != null) {
                    teamCollection.deleteOne(document);
                }
            }
        }.runTaskAsynchronously(main);
    }

    public boolean hasTeam(UUID id) {
        if (getTeam(id) != null) {
            return true;
        }

        for (Team teams : getTeams()) {
            if (teams.getManagers().contains(id) || teams.getMembers().contains(id)) {
                return true;
            }
        }

        return false;
    }

    public Team getTeam(UUID id) {
        for (Team teams : getTeams()) {
            if (teams.getManagers().contains(id) || teams.getMembers().contains(id)) {
                return teams;
            }
        }

        return null;
    }

    public Team getTeam(String name) {
        for (Team teams : getTeams()) {
            if (teams.getName().equalsIgnoreCase(name)) {
                return teams;
            }
        }
        return null;
    }

    public List<UUID> getTeamChatters() {
        return teamChatters;
    }

    public HashSet<Team> getTeams() {
        return teams;
    }
}
