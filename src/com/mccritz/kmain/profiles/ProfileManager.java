package com.mccritz.kmain.profiles;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.LocationSerialization;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

public class ProfileManager {

    private kMain main = kMain.getInstance();
    private HashSet<Profile> loadedProfiles = new HashSet<>();
    private MongoCollection<Document> profileCollection = main.getMongoDatabase().getCollection("profiles");

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
        main.getLogger().log(Level.INFO, "Loading " + profileCollection.count() + " profiles.");

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Document document : profileCollection.find()) {
                    UUID id = UUID.fromString(document.getString("uniqueId"));
                    String name = document.getString("name");
                    Double gold = document.getDouble("gold");
                    Location home = null;

                    if (document.getString("home") != null) {
                        home = LocationSerialization.deserializeLocation(document.getString("home"));
                    }

                    boolean safeLogged = true;

                    if (document.getBoolean("safeLogged") != null) {
                        safeLogged = document.getBoolean("safeLogged");
                    }

                    Profile profile = new Profile(id);
                    profile.setName(name);
                    profile.setGold(gold);
                    profile.setHome(home);
                    profile.setSafeLogged(safeLogged);

                    loadedProfiles.add(profile);
                }

                main.getLogger().log(Level.INFO, "Successfully loaded " + profileCollection.count() + " profiles.");
            }
        }.runTaskAsynchronously(main);
    }

    public void saveProfiles() {
        main.getLogger().log(Level.INFO, "Saving " + getLoadedProfiles().size() + " profiles.");

        for (Profile profile : getLoadedProfiles()) {
            Document document = new Document("uniqueId", profile.getUniqueId().toString());
            document.append("name", profile.getName());
            document.append("gold", profile.getGold());
            if (profile.getHome() != null)
                document.append("home", LocationSerialization.serializeLocation(profile.getHome()));
            document.append("safeLogged", profile.isSafeLogged());

            new BukkitRunnable() {
                @Override
                public void run() {
                    profileCollection.updateOne(Filters.eq("uniqueId", profile.getUniqueId().toString()), document, new UpdateOptions().upsert(true));
                }
            }.runTaskAsynchronously(main);
        }

        main.getLogger().log(Level.INFO, "Successfully saved " + getLoadedProfiles().size() + " profiles.");
    }

    public void createProfile(Player p) {
        if (!hasProfile(p.getUniqueId())) {
            Profile profile = new Profile(p.getUniqueId());
            profile.setName(p.getName());
            profile.setGold(0.0);
            profile.setSafeLogged(false);

            Document document = new Document("uniqueId", profile.getUniqueId().toString());
            document.append("name", profile.getName());
            document.append("gold", profile.getGold());
            if (profile.getHome() != null)
                document.append("home", LocationSerialization.serializeLocation(profile.getHome()));
            document.append("safeLogged", profile.isSafeLogged());

            new BukkitRunnable() {
                @Override
                public void run() {
                    profileCollection.insertOne(document);
                }
            }.runTaskAsynchronously(main);

            getLoadedProfiles().add(profile);
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
