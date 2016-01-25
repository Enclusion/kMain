package code.breakmc.legacy.profiles;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.LocationSerialization;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

public class ProfileManager {

    private Legacy main = Legacy.getInstance();
    private HashSet<Profile> loadedProfiles = new HashSet<>();
    private DBCollection pCollection = main.getDb().getCollection("profiles");

    public ProfileManager() {
        loadProfiles();

        new BukkitRunnable() {
            @Override
            public void run() {
                saveProfiles();
            }
        }.runTaskTimerAsynchronously(main, 0L, 300*20L);
    }

    public void loadProfiles() {
        DBCursor dbc = pCollection.find();

        main.getLogger().log(Level.INFO, "Loading " + dbc.count() + " profiles.");

        while (dbc.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) dbc.next();

            Profile prof;
            UUID id = UUID.fromString(dbo.getString("uniqueId"));
            String name = dbo.getString("name");
            String youtubename = dbo.getString("youtubeBame");
            Double balance = dbo.getDouble("balance");
            Location home = null;

            if (dbo.getString("home") != null) {
                home = LocationSerialization.deserializeLocation(dbo.getString("home"));
            }

            boolean safeLogged = true;

            if (dbo.getString("safeLogged") != null) {
                safeLogged = dbo.getBoolean("safeLogged");
            }

            HashMap<String, Long> usedKits = new HashMap<>();
            BasicDBObject oj = (BasicDBObject) dbo.get("kitsUsed");
            if (oj != null) {
                for (String str : oj.keySet()) {
                    usedKits.put(str, (long) oj.get(str));
                }
            }

            int emeraldsSold = dbo.getInt("emeraldsSold");
            int diamondsSold = dbo.getInt("diamondsSold");
            int goldSold = dbo.getInt("goldSold");
            int ironSold = dbo.getInt("ironSold");

            prof = new Profile(id, name, youtubename, balance, home, safeLogged, usedKits, emeraldsSold, diamondsSold, goldSold, ironSold);

            loadedProfiles.add(prof);
        }

        main.getLogger().log(Level.INFO, "Successfully loaded " + dbc.count() + " profiles.");
    }

    public void saveProfiles() {
        main.getLogger().log(Level.INFO, "Saving " + getLoadedProfiles().size() + " profiles.");

        for (Profile prof : getLoadedProfiles()) {
            DBCursor dbc = pCollection.find(new BasicDBObject("uniqueId", prof.getUniqueId().toString()));
            BasicDBObject dbo = new BasicDBObject("uniqueId", prof.getUniqueId().toString());
            dbo.put("name", prof.getName());
            dbo.put("youtubeName", prof.getYoutubename());
            dbo.put("balance", prof.getBalance());
            if (prof.getHome() != null) {
                dbo.put("home", LocationSerialization.serializeLocation(prof.getHome()));
            }
            dbo.put("safeLogged", prof.isSafeLogged());
            dbo.put("kitsUsed", prof.getUsedKits());
            dbo.put("emeraldsSold", prof.getEmeraldsSold());
            dbo.put("diamondsSold", prof.getDiamondsSold());
            dbo.put("goldSold", prof.getGoldSold());
            dbo.put("ironSold", prof.getIronSold());

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
            Profile prof = new Profile(p.getUniqueId(), p.getName(), "", main.getConfig().getDouble("economy.default-balance"), null, false, new HashMap<>(), 0, 0, 0, 0);

            BasicDBObject dbo = new BasicDBObject("uniqueId", prof.getUniqueId().toString());
            dbo.put("name", prof.getName());
            dbo.put("youtubeName", prof.getYoutubename());
            dbo.put("balance", prof.getBalance());
            dbo.put("safeLogged", prof.isSafeLogged());
            dbo.put("kitsUsed", prof.getUsedKits());
            dbo.put("emeraldsSold", prof.getEmeraldsSold());
            dbo.put("diamondsSold", prof.getDiamondsSold());
            dbo.put("goldSold", prof.getGoldSold());
            dbo.put("ironSold", prof.getIronSold());

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
