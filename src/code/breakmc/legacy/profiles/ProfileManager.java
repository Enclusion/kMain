package code.breakmc.legacy.profiles;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.LocationSerialization;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Calvin on 5/9/2015.
 */
public class ProfileManager {

    private Legacy main = Legacy.getInstance();
    private HashSet<Profile> loadedProfiles = new HashSet<>();
    private DBCollection pCollection = main.getDb().getCollection("profiles");

    public ProfileManager() {
        loadProfiles();
    }

    public void loadProfiles() {
        DBCursor dbc = pCollection.find();

        main.getLogger().log(Level.INFO, "Loading " + dbc.count() + " profiles.");

        while (dbc.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) dbc.next();

            Profile prof;
            UUID id = UUID.fromString(dbo.getString("id"));
            String name = dbo.getString("name");
            String youtubename = dbo.getString("youtubename");
            Double balance = dbo.getDouble("balance");
            Location home = null;

            if (dbo.getString("home") != null) {
                home = LocationSerialization.deserializeLocation(dbo.getString("home"));
            }

            boolean safeLogged = true;

            if (dbo.getString("safe-logged") != null) {
                safeLogged = dbo.getBoolean("safe-logged");
            }

            prof = new Profile(id, name, youtubename, balance, home, safeLogged);

            loadedProfiles.add(prof);
        }

        main.getLogger().log(Level.INFO, "Successfully loaded " + dbc.count() + " profiles.");
    }

    public void saveProfiles() {
        main.getLogger().log(Level.INFO, "Saving " + getLoadedProfiles().size() + " profiles.");

        for (Profile prof : getLoadedProfiles()) {
            DBCursor dbc = pCollection.find(new BasicDBObject("id", prof.getUniqueId().toString()));
            BasicDBObject dbo = new BasicDBObject("id", prof.getUniqueId().toString());
            dbo.put("name", prof.getName());
            dbo.put("youtubename", prof.getYoutubename());
            dbo.put("balance", prof.getBalance());
            if (prof.getHome() != null) {
                dbo.put("home", LocationSerialization.serializeLocation(prof.getHome()));
            }
            dbo.put("safe-logged", prof.isSafeLogged());

            if (dbc.hasNext()) {
                pCollection.update(dbc.getQuery(), dbo);
            } else {
                pCollection.insert(dbo);
            }
        }

        main.getLogger().log(Level.INFO, "Successfully saved " + getLoadedProfiles().size() + " profiles.");
    }

    public void createProfile(Player p) {
        if (!hasProfile(p.getUniqueId())) {
            Profile prof = new Profile(p.getUniqueId(), p.getName(), "", 0.0, null, false);

            BasicDBObject dbo = new BasicDBObject("id", prof.getUniqueId().toString());
            dbo.put("name", prof.getName());
            dbo.put("youtubename", prof.getYoutubename());
            dbo.put("balance", prof.getBalance());
            dbo.put("safe-loggged", prof.isSafeLogged());

            pCollection.insert(dbo);

            getLoadedProfiles().add(prof);
        }
    }

    public Boolean hasProfile(UUID id) {
        for (Profile prof : getLoadedProfiles()) {
            if (prof.getUniqueId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public Profile getProfile(UUID id) {
        for (Profile prof : getLoadedProfiles()) {
            if (prof.getUniqueId().equals(id)) {
                return prof;
            }
        }
        return null;
    }

    public Profile getProfile(String name) {
        for (Profile prof : getLoadedProfiles()) {
            if (prof.getName().equalsIgnoreCase(name)) {
                return prof;
            }
        }

        return null;
    }

    public HashSet<Profile> getLoadedProfiles() {
        return loadedProfiles;
    }
}
