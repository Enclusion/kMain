package com.mccritz.kmain.profiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.utils.LocationSerialization;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;

public class ProfileManager {

    private Legacy main = Legacy.getInstance();
    private HashSet<Profile> loadedProfiles = new HashSet<>();
    private MongoCollection<Document> profileCollection = main.getMongoDatabase().getCollection("profiles");

    public ProfileManager() {
	loadProfiles();

	new BukkitRunnable() {
	    @Override
	    public void run() {
		saveProfiles();
	    }
	}.runTaskTimerAsynchronously(main, 0L, 300 * 20L);
    }

    public void loadProfiles() {
	main.getLogger().log(Level.INFO, "Loading " + profileCollection.count() + " profiles.");

	new BukkitRunnable() {
	    @Override
	    public void run() {
		for (Document document : profileCollection.find()) {
		    UUID id = UUID.fromString(document.getString("uniqueId"));
		    String name = document.getString("name");
		    Double balance = document.getDouble("balance");
		    Location home = null;

		    if (document.getString("home") != null) {
			home = LocationSerialization.deserializeLocation(document.getString("home"));
		    }

		    boolean safeLogged = true;

		    if (document.getBoolean("safeLogged") != null) {
			safeLogged = document.getBoolean("safeLogged");
		    }

		    HashMap<String, Long> usedKits = new HashMap<>();
		    Document kitsUsed = (Document) document.get("kitsUsed");
		    Bukkit.broadcastMessage("kitsUsed:" + kitsUsed.toJson());
		    for (String field : kitsUsed.keySet()) {
			Bukkit.broadcastMessage("field: " + field + ":" + kitsUsed.getLong(field));
			usedKits.put(field, kitsUsed.getLong(field));
		    }

		    int emeraldsSold = document.getInteger("emeraldsSold");
		    int diamondsSold = document.getInteger("diamondsSold");
		    int goldSold = document.getInteger("goldSold");
		    int ironSold = document.getInteger("ironSold");

		    Profile profile = new Profile(id, name, "", balance, home, safeLogged, usedKits, emeraldsSold,
			    diamondsSold, goldSold, ironSold);

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
	    document.append("balance", profile.getBalance());
	    if (profile.getHome() != null) {
		document.append("home", LocationSerialization.serializeLocation(profile.getHome()));
	    }
	    document.append("safeLogged", profile.isSafeLogged());

	    Document usedKits = new Document();
	    for (String field : profile.getUsedKits().keySet()) {
		usedKits.append(field, profile.getUsedKits().get(field));
	    }

	    document.append("kitsUsed", usedKits);
	    document.append("emeraldsSold", profile.getEmeraldsSold());
	    document.append("diamondsSold", profile.getDiamondsSold());
	    document.append("goldSold", profile.getGoldSold());
	    document.append("ironSold", profile.getIronSold());

	    new BukkitRunnable() {
		@Override
		public void run() {
		    Document previousDocument = profileCollection
			    .find(Filters.eq("uniqueId", profile.getUniqueId().toString())).first();
		    if (previousDocument != null) {
			profileCollection.findOneAndReplace(previousDocument, document,
				new FindOneAndReplaceOptions().upsert(true));
		    } else {
			profileCollection.insertOne(document);
		    }
		}
	    }.runTaskAsynchronously(main);
	}

	main.getLogger().log(Level.INFO, "Successfully saved " + getLoadedProfiles().size() + " profiles.");
    }

    public void createProfile(Player p) {
	if (!hasProfile(p.getUniqueId())) {
	    Profile profile = new Profile(p.getUniqueId(), p.getName(), "",
		    main.getConfig().getDouble("economy.default-balance"), null, false, new HashMap<>(), 0, 0, 0, 0);

	    Document document = new Document("uniqueId", profile.getUniqueId().toString());
	    document.append("name", profile.getName());
	    document.append("balance", profile.getBalance());
	    if (profile.getHome() != null) {
		document.append("home", LocationSerialization.serializeLocation(profile.getHome()));
	    }
	    document.append("safeLogged", profile.isSafeLogged());
	    document.append("kitsUsed", profile.getUsedKits());
	    document.append("emeraldsSold", profile.getEmeraldsSold());
	    document.append("diamondsSold", profile.getDiamondsSold());
	    document.append("goldSold", profile.getGoldSold());
	    document.append("ironSold", profile.getIronSold());

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
	    if (prof.getUniqueId().equals(id))
		return true;
	}
	return false;
    }

    public Profile getProfile(UUID id) {
	for (Profile prof : getLoadedProfiles()) {
	    if (prof.getUniqueId().equals(id))
		return prof;
	}
	return null;
    }

    public Profile getProfile(String name) {
	for (Profile prof : getLoadedProfiles()) {
	    if (prof.getName().equalsIgnoreCase(name))
		return prof;
	}

	return null;
    }

    public HashSet<Profile> getLoadedProfiles() {
	return loadedProfiles;
    }
}
