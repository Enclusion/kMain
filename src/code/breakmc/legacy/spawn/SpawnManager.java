package code.breakmc.legacy.spawn;

import code.breakmc.legacy.Legacy;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Calvin on 4/27/2015.
 * Project: Legacy
 */
public class SpawnManager {

    private Legacy main = Legacy.getInstance();
    private Spawn spawn;
    private HashSet<UUID> spawnProtected = new HashSet<>();
    private DBCollection mCollection = main.getDb().getCollection("main");

    public SpawnManager() {
        loadSpawn();
        spawnProtected = new HashSet<>();
    }

    public void loadSpawn() {
        DBCursor dbc = mCollection.find(new BasicDBObject("searchby", "spawn"));

        if (dbc.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) dbc.next();

            int radius = dbo.getInt("spawnradius");
            int height = dbo.getInt("spawnheight");
            int stoneradius = dbo.getInt("stoneradius");
            int stoneheight = dbo.getInt("stoneheight");

            Spawn spawn = new Spawn(radius, height, stoneradius, stoneheight);

            setSpawn(spawn);
            main.getLogger().log(Level.INFO, "Successfully loaded spawn. Radius: " + spawn.getRadius() + " Height: " + spawn.getHeight() + " Stone Radius: " + spawn.getStoneRadius() + " Stone Height: " + spawn.getStoneHeight());
        } else {
            main.getLogger().log(Level.INFO, "Spawn failed to load: Not set");
        }
    }

    public void saveSpawn() {
        if (spawn != null) {
            DBCursor dbc = mCollection.find(new BasicDBObject("searchby", "spawn"));

            BasicDBObject dbo = new BasicDBObject("searchby", "spawn");
            dbo.append("spawnradius", spawn.getRadius());
            dbo.append("spawnheight", spawn.getHeight());
            dbo.append("stoneradius", spawn.getStoneRadius());
            dbo.append("stoneheight", spawn.getStoneHeight());

            if (dbc.hasNext()) {
                mCollection.update(dbc.getQuery(), dbo);
            } else {
                mCollection.insert(dbo);
            }
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
