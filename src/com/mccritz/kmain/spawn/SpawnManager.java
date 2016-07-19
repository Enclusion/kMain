package com.mccritz.kmain.spawn;

import com.mccritz.kmain.kMain;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

public class SpawnManager {

    private kMain main = kMain.getInstance();
    private Spawn spawn;
    private HashSet<UUID> spawnProtected = new HashSet<>();
    private MongoCollection<Document> mainCollection = main.getMongoDatabase().getCollection("main");

    public SpawnManager() {
        loadSpawn();
        spawnProtected = new HashSet<>();
    }

    public void loadSpawn() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Document document = mainCollection.find(Filters.eq("searchby", "spawn")).first();

                if (document != null) {
                    Spawn spawn = new Spawn(document.getInteger("spawnradius"), document.getInteger("spawnheight"), document.getInteger("stoneradius"), document.getInteger("stoneheight"));

                    setSpawn(spawn);

                    main.getLogger().log(Level.INFO, "Successfully loaded spawn. Radius: " + spawn.getRadius() + " Height: " + spawn.getHeight() + " Stone Radius: " + spawn.getStoneRadius() + " Stone Height: " + spawn.getStoneHeight());
                } else {
                    main.getLogger().log(Level.INFO, "Spawn failed to load: Not set");
                }
            }
        }.runTaskAsynchronously(main);
    }

    public void saveSpawn() {
        if (spawn != null) {
            Document document = new Document("searchby", "spawn");
            document.append("spawnradius", spawn.getRadius());
            document.append("spawnheight", spawn.getHeight());
            document.append("stoneradius", spawn.getStoneRadius());
            document.append("stoneheight", spawn.getStoneHeight());

            new BukkitRunnable() {
                @Override
                public void run() {
                    mainCollection.updateOne(Filters.eq("searchby", "spawn"), document, new UpdateOptions().upsert(true));
                }
            }.runTaskAsynchronously(main);
        }
    }

    public void setSpawn(Spawn spawn) {
        this.spawn = spawn;
    }

    public Spawn getSpawn() {
        return spawn;
    }

    public boolean hasSpawnProt(UUID id) {
        return spawnProtected.contains(id);
    }

    public HashSet<UUID> getSpawnProtected() {
        return spawnProtected;
    }
}
