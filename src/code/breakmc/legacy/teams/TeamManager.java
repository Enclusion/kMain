package code.breakmc.legacy.teams;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.LocationSerialization;
import code.breakmc.legacy.utils.MessageManager;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class TeamManager {

    private Legacy main = Legacy.getInstance();
    private HashSet<Team> teams = new HashSet<>();
    private List<UUID> teamChatters = new ArrayList<>();
    private DBCollection tCollection = main.getDb().getCollection("teams");

    public TeamManager() {
        loadTeams();

        new BukkitRunnable() {
            @Override
            public void run() {
                saveTeams();
            }
        }.runTaskTimerAsynchronously(main, 0L, 300*20L);
    }

    public void loadTeams() {
        DBCursor dbc = tCollection.find();

        main.getLogger().log(Level.INFO, "Preparing to load " + dbc.count() + " teams.");

        while (dbc.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) dbc.next();

            String name = dbo.getString("name");
            List<UUID> managers = new ArrayList<>();
            List<UUID> members = new ArrayList<>();
            Location hq;
            Location rally;

            BasicDBList ml1 = (BasicDBList) dbo.get("managers");
            if (ml1 != null) {
                managers.addAll(ml1.stream().map(obj -> UUID.fromString((String) obj)).collect(Collectors.toList()));
            }

            BasicDBList ml2 = (BasicDBList) dbo.get("members");
            if (ml2 != null) {
                members.addAll(ml2.stream().map(obj -> UUID.fromString((String) obj)).collect(Collectors.toList()));
            }

            if (dbo.getString("hq") != null) {
                if (LocationSerialization.deserializeLocation(dbo.getString("hq")) != null) {
                    hq = LocationSerialization.deserializeLocation(dbo.getString("hq"));
                } else {
                    hq = null;
                }
            } else {
                hq = null;
            }

            if (dbo.getString("rally") != null) {
                if (LocationSerialization.deserializeLocation(dbo.getString("rally")) != null) {
                    rally = LocationSerialization.deserializeLocation(dbo.getString("rally"));
                } else {
                    rally = null;
                }
            } else {
                rally = null;
            }

            boolean friendlyfire = dbo.getBoolean("friendlyfire");
            String password = dbo.getString("password");
            int valorpoints = dbo.getInt("valorpoints");

            Team team = new Team(name, managers, members, hq, rally, friendlyfire, password, valorpoints);
            teams.add(team);
        }

        main.getLogger().log(Level.INFO, "Successfully loaded " + teams.size() + " teams.");
    }

    public void saveTeams() {
        Bukkit.getLogger().log(Level.INFO, "Preparing to save " + getTeams().size() + " teams.");
        for (Team team : getTeams()) {
            DBCursor dbc = tCollection.find(new BasicDBObject("name", team.getName()));
            BasicDBObject dbo = new BasicDBObject("name", team.getName());

            BasicDBList ml1 = team.getManagers().stream().map(UUID::toString).collect(Collectors.toCollection(BasicDBList::new));
            dbo.put("managers", ml1);

            BasicDBList ml2 = team.getMembers().stream().map(UUID::toString).collect(Collectors.toCollection(BasicDBList::new));
            dbo.put("members", ml2);

            if (team.getHq() != null) {
                dbo.put("hq", LocationSerialization.serializeLocation(team.getHq()));
            }

            if (team.getRally() != null) {
                dbo.put("rally", LocationSerialization.serializeLocation(team.getRally()));
            }

            dbo.put("friendlyfire", team.isFriendlyFireEnabled());
            dbo.put("password", team.getPassword());
            dbo.put("valorpoints", team.getValorPoints());

            if (dbc.hasNext()) {
                tCollection.update(dbc.getQuery(), dbo);
            } else {
                tCollection.insert(dbo);
            }
        }

        main.getLogger().log(Level.INFO, "Successfully saved " + tCollection.find().count() + " teams.");
    }

    public void createTeam(UUID id, String name, String password) {
        if (hasTeam(id)) {
            MessageManager.sendMessage(id, "&cYou are already in a team!");
            return;
        }

        for (Team teams : getTeams()) {
            if (name.equalsIgnoreCase(teams.getName())) {
                MessageManager.sendMessage(id, "&cThat team name is already in use!");
                return;
            }
        }

        if (!name.matches("^[A-Za-z0-9_+-]*$")) {
            MessageManager.sendMessage(id, "&cYour team name must be alphanumerical.");
            return;
        }

        if (name.length() < 3) {
            MessageManager.sendMessage(id, "&cYour team name must be at least 3 characters.");
            return;
        } else if (name.length() > 16) {
            MessageManager.sendMessage(id, "&cYour team name must be under 16 characters.");
            return;
        }

        Team team = new Team(name, new ArrayList<>(), new ArrayList<>(), null, null, false, (password.equalsIgnoreCase("") ? "" : password), 0);
        team.getManagers().add(id);

        MessageManager.sendMessage(id, "&3You have created team &b" + name);

        teams.add(team);

        BasicDBObject dbo = new BasicDBObject("name", team.getName());

        BasicDBList ml1 = team.getManagers().stream().map(UUID::toString).collect(Collectors.toCollection(BasicDBList::new));
        dbo.put("managers", ml1);

        BasicDBList ml2 = team.getMembers().stream().map(UUID::toString).collect(Collectors.toCollection(BasicDBList::new));
        dbo.put("members", ml2);

        dbo.put("friendlyfire", team.isFriendlyFireEnabled());
        dbo.put("password", team.getPassword());
        dbo.put("valorpoints", team.getValorPoints());

        tCollection.insert(dbo);
    }

    public void deleteTeam(Team team) {
        teams.remove(team);

        DBCursor dbc = tCollection.find(new BasicDBObject("name", team.getName()));

        if (dbc.hasNext()) {
            tCollection.remove(dbc.next());
        }
    }

    public boolean hasTeam(UUID id) {
        if (getTeam(id) != null) {
            return true;
        }
        for (Team teams : getTeams()) {
            if (teams.getManagers().contains(id)) {
                return true;
            }
            if (teams.getMembers().contains(id)) {
                return true;
            }
        }
        return false;
    }

    public Team getTeam(UUID id) {
        for (Team teams : getTeams()) {
            if (teams.getManagers().contains(id)) {
                return teams;
            }
            if (teams.getMembers().contains(id)) {
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
