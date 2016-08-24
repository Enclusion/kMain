package com.mccritz.kmain.events.end;

import com.mccritz.kmain.events.Event;
import com.mccritz.kmain.events.EventType;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.Cooldowns;
import com.mccritz.kmain.utils.LocationSerialization;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import com.mccritz.kmain.utils.serialization.InventorySerialization;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Random;

@Getter
@Setter
public class EndEvent extends Event {

    private String MESSAGE_PREFIX = "&c[&5End Event&c] ";
    private boolean started = false;
    private boolean canLeaveEnd = false;
    private Location spawnLocation;
    private BukkitTask gameRunnable;
    private int gameTime = 660;
    private double endExitTime = 300.0;
    private double chestRefillTime = 120.0;
    private double endOverTime = 60.0;
    private HashMap<Location, Integer> chests = new HashMap<>();
    private Inventory tier1Inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&5Tier 1"));
    private Inventory tier2Inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&5Tier 2"));
    private Inventory tier3Inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&5Tier 3"));

    public EndEvent() {
        super(EventType.END_EVENT);

        loadData();
    }

    public void startEvent() {
        started = true;

        if (spawnLocation != null)
            spawnLocation.getWorld().setSpawnLocation(spawnLocation.getBlockX(), spawnLocation.getBlockY(), spawnLocation.getBlockZ());

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "toggleworld world_the_end");

        MessageManager.broadcast(MESSAGE_PREFIX + "the end event has begun!");

        gameRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                gameTime--;

                if (gameTime <= 660 && gameTime > 360) {
                    endExitTime--;
                }

                if (gameTime == 360) {
                    canLeaveEnd = true;
                    MessageManager.broadcast(MESSAGE_PREFIX + "You can now exit the end.");
                }

                if (gameTime <= 360 && gameTime > 240) {
                    chestRefillTime--;
                }

                if (gameTime == 240) {
                    fillChests();
                    MessageManager.broadcast(MESSAGE_PREFIX + "Chests have been refilled!");
                }

                if (gameTime <= 60 && gameTime > 0) {
                    endOverTime--;
                }

                if (gameTime == 60) {
                    MessageManager.broadcast(MESSAGE_PREFIX + "There is 1 minute until you will get teleported to spawn.");
                }

                if (gameTime == 0) {
                    MessageManager.broadcast(MESSAGE_PREFIX + "The event is over.");
                    cancelEvent();
                }
            }
        }.runTaskTimer(getMain(), 0L, 20L);
    }

    public void cancelEvent() {
        started = false;
        canLeaveEnd = false;
        gameRunnable.cancel();
        gameRunnable = null;
        gameTime = 660;
        endExitTime = 300;
        chestRefillTime = 120;
        endOverTime = 60;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "toggleworld world_the_end");

        clearChests();

        for (Player player : PlayerUtility.getOnlinePlayers()) {
            if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
                if (kMain.getInstance().getSpawnManager().getSpawn() != null) {
                    player.teleport(new Location(Bukkit.getWorld("world"), 0.0, kMain.getInstance().getSpawnManager().getSpawn().getStoneHeight(), 0.0));
                    kMain.getInstance().getSpawnManager().getSpawnProtected().add(player.getUniqueId());
                }
            }
        }
    }

    public void fillChests() {
        World w = Bukkit.getWorld("world_the_end");

        for (Chunk chunk : w.getLoadedChunks()) {
            for (BlockState e : chunk.getTileEntities()) {
                if (e instanceof Chest || e instanceof DoubleChest) {
                    Chest chest = (Chest) e;
                    Random rand = new Random();
                    int chance = rand.nextInt(100) + 1;

                    getChests().clear();
                    getChests().put(e.getLocation(), (chance <= 25 ? 1 : chance <= 40 ? 2 : chance <= 65 ? 3 : 3));

                    if (getChests().get(e.getLocation()) == 1) {
                        chest.getInventory().setContents(tier1Inventory.getContents());
                    } else if (getChests().get(e.getLocation()) == 2) {
                        chest.getInventory().setContents(tier2Inventory.getContents());
                    } else if (getChests().get(e.getLocation()) == 3) {
                        chest.getInventory().setContents(tier3Inventory.getContents());
                    }
                }
            }
        }
    }

    public void clearChests() {
        World w = Bukkit.getWorld("world_the_end");

        for (Chunk chunk : w.getLoadedChunks()) {
            for (BlockState e : chunk.getTileEntities()) {
                if (e instanceof Chest || e instanceof DoubleChest) {
                    Chest chest = (Chest) e;
                    getChests().clear();

                    chest.getInventory().clear();
                }
            }
        }
    }

    public void saveData() {
        Document document = toDocument();

        if (spawnLocation != null) {
            document.append("spawnLocation", LocationSerialization.serializeLocation(spawnLocation));
        }

        if (tier1Inventory.getContents().length > 0) {
            document.append("tier1Inventory", InventorySerialization.serializeInventoryAsString(tier1Inventory));
        }

        if (tier2Inventory.getContents().length > 0) {
            document.append("tier2Inventory", InventorySerialization.serializeInventoryAsString(tier2Inventory));
        }

        if (tier3Inventory.getContents().length > 0) {
            document.append("tier3Inventory", InventorySerialization.serializeInventoryAsString(tier3Inventory));
        }

        getMain().getMongoDatabase().getCollection("events").replaceOne(Filters.eq("type", EventType.END_EVENT.toString()), document, new UpdateOptions().upsert(true));

        MessageManager.debug("&7Successfully chests for event: &5" + EventType.END_EVENT.toString() + "&7.");
    }

    public void loadData() {
        for (Document document : getMain().getMongoDatabase().getCollection("events").find()) {
            if (document.getString("type").equalsIgnoreCase(EventType.END_EVENT.toString())) {
                if (document.getString("spawnLocation") != null) {
                    spawnLocation = LocationSerialization.deserializeLocation(document.getString("spawnLocation"));
                }

                if (document.getString("tier1Inventory") != null) {
                    InventorySerialization.setInventory(tier1Inventory, document.getString("tier1Inventory"));
                }

                if (document.getString("tier2Inventory") != null) {
                    InventorySerialization.setInventory(tier2Inventory, document.getString("tier2Inventory"));
                }

                if (document.getString("tier3Inventory") != null) {
                    InventorySerialization.setInventory(tier3Inventory, document.getString("tier3Inventory"));
                }

                MessageManager.debug("&7Successfully loaded chests for event: &5" + EventType.END_EVENT.toString() + "&7.");
            }
        }
    }

    public int getDeathBannedSize() {
        return Cooldowns.cooldowns.size();
    }
}
