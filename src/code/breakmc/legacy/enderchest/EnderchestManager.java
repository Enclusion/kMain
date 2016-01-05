package code.breakmc.legacy.enderchest;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.PlayerUtility;
import code.breakmc.legacy.utils.serialization.InventorySerialization;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

/**
 * Created by Calvin on 4/26/2015.
 * Project: Legacy
 */
public class EnderchestManager {

    private Legacy main = Legacy.getInstance();
    private DBCollection eCollection = main.getDb().getCollection("enderchest");

    public void saveEnderchest() {
        for (Player all : PlayerUtility.getOnlinePlayers()) {
            if (all.getOpenInventory() != null) {
                if (all.getOpenInventory().getTopInventory() != null) {
                    Inventory inv = all.getOpenInventory().getTopInventory();

                    if (inv.getTitle().contains("Enderchest")) {
                        setEnderchest(all, inv);
                        all.closeInventory();
                    }
                }
            }
        }
    }

    public void setEnderchest(Player p, Inventory ec) {
        BasicDBObject dbo = new BasicDBObject("uuid", p.getUniqueId().toString());
        dbo.append("inventory", InventorySerialization.serializeInventoryAsString(ec));

        DBCursor dbc1 = eCollection.find(new BasicDBObject("uuid", p.getUniqueId().toString()));

        if (dbc1.hasNext()) {
            eCollection.update(dbc1.getQuery(), dbo);
        } else {
            eCollection.insert(dbo);
        }
    }

    public void setEnderchest(UUID id, Inventory ec) {
        BasicDBObject dbo = new BasicDBObject("uuid", id.toString());
        dbo.append("inventory", InventorySerialization.serializeInventoryAsString(ec));

        DBCursor dbc1 = eCollection.find(new BasicDBObject("uuid", id.toString()));

        if (dbc1.hasNext()) {
            eCollection.update(dbc1.getQuery(), dbo);
        } else {
            eCollection.insert(dbo);
        }
    }

    public Inventory getEnderchest(Player p) {
        DBCursor dbc1 = eCollection.find(new BasicDBObject("uuid", p.getUniqueId().toString()));

        if (dbc1.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) dbc1.next();
            String invstring = dbo.getString("inventory");

            Inventory inv = Bukkit.createInventory(p, 27, ChatColor.DARK_PURPLE + "Enderchest");
            InventorySerialization.setInventory(inv, invstring);

            return inv;
        }
        return null;
    }

    public Inventory getEnderchest(UUID id) {
        DBCursor dbc1 = eCollection.find(new BasicDBObject("uuid", id.toString()));

        if (dbc1.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) dbc1.next();
            String invstring = dbo.getString("inventory");

            Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + "Enderchest");
            InventorySerialization.setInventory(inv, invstring);

            return inv;
        }
        return null;
    }

    public Boolean hasEnderchest(Player p) {
        DBCursor dbc1 = eCollection.find(new BasicDBObject("uuid", p.getUniqueId().toString()));

        return dbc1.hasNext();
    }

    public Boolean hasEnderchest(UUID id) {
        DBCursor dbc1 = eCollection.find(new BasicDBObject("uuid", id.toString()));

        return dbc1.hasNext();
    }
}
