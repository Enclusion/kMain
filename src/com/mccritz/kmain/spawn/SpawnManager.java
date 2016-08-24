package com.mccritz.kmain.spawn;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.MessageManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

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
                    Spawn spawn = new Spawn();
                    spawn.setRadius(document.getInteger("spawnradius"));
                    spawn.setHeight(document.getInteger("spawnheight"));
                    spawn.setStoneRadius(document.getInteger("stoneradius"));
                    spawn.setStoneHeight(document.getInteger("stoneheight"));

                    setSpawn(spawn);

                    MessageManager.debug("&7Successfully loaded spawn. Radius: &c" + spawn.getRadius() + " &7Height: &c" + spawn.getHeight() + " &7Stone Radius: &c" + spawn.getStoneRadius() + " &7Stone Height: &c" + spawn.getStoneHeight());
                } else {
                    MessageManager.debug("&cSpawn failed to load: Not set");
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

            mainCollection.replaceOne(Filters.eq("searchby", "spawn"), document, new UpdateOptions().upsert(true));
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
