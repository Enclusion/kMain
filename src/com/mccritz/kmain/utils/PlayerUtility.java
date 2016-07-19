package com.mccritz.kmain.utils;

import com.mccritz.kmain.commands.HudCommand;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.glaedr.scoreboards.Entry;
import com.mccritz.kmain.utils.glaedr.scoreboards.PlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtility {

    private static TeamManager tm = kMain.getInstance().getTeamManager();
    private static SpawnManager sm = kMain.getInstance().getSpawnManager();

    public static double getHealth(Player p) {
        return p.getHealth();
    }

    public static double getMaxHealth(Player p) { return p.getMaxHealth(); }

    public static void setHealth(Player p, double health) { p.setHealth(health); }

    public static double getDamage(EntityDamageByEntityEvent e) { return e.getDamage(); }

    public static Player[] getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public static boolean hasInventorySpace(Inventory inventory, ItemStack is) {
        Inventory inv = Bukkit.createInventory(null, inventory.getSize());

        for (int i = 0; i < inv.getSize(); i++) {
            if (inventory.getItem(i) != null) {
                ItemStack item = inventory.getItem(i).clone();
                inv.setItem(i, item);
            }
        }

        return inv.addItem(new ItemStack[]{is.clone()}).size() <= 0;

    }

    public static int checkSlotsAvailable(Inventory inv) {
        ItemStack[] items = inv.getContents();
        int emptySlots = 0;

        for (ItemStack is : items) {
            if (is == null) {
                emptySlots = emptySlots + 1;
            }
        }

        return emptySlots;
    }

    public static List<String> toList(Player[] array) {
        List<String> list = new ArrayList<>();

        for (Player t : array) {
            list.add(t.getName());
        }

        return list;
    }

    public static void updateScoreboard(Player p) {
        PlayerScoreboard scoreboard = PlayerScoreboard.getScoreboard(p);

        if (HudCommand.getDisplayList().contains(p.getUniqueId())) {
            if (scoreboard != null) {
                if (scoreboard.getEntry("balance") != null) {
                    scoreboard.getEntry("balance")
                            .setText("&7Gold: &a" + MessageManager.formatDouble(kMain.getInstance().getProfileManager().getProfile(p.getUniqueId()).getGold()))
                            .send();
                } else {
                    new Entry("balance", scoreboard)
                            .setText("&7Gold: &a" + MessageManager.formatDouble(kMain.getInstance().getProfileManager().getProfile(p.getUniqueId()).getGold()))
                            .send();
                }

                if (scoreboard.getEntry("team") != null) {
                    scoreboard.getEntry("team")
                            .setText("&7Team: &3" + (tm.hasTeam(p.getUniqueId()) ? tm.getTeam(p.getUniqueId()).getName() : "&cNone."))
                            .send();
                } else {
                    new Entry("team", scoreboard)
                            .setText("&7Team: &3" + (tm.hasTeam(p.getUniqueId()) ? tm.getTeam(p.getUniqueId()).getName() : "&cNone."))
                            .send();
                }

                if (scoreboard.getEntry("spawn") != null) {
                    scoreboard.getEntry("spawn")
                            .setText("&7Spawn Protection: " + (sm.hasSpawnProt(p.getUniqueId()) ? "&aYes" : "&cNo"))
                            .send();
                } else {
                    new Entry("spawn", scoreboard)
                            .setText("&7Spawn Protection: " + (sm.hasSpawnProt(p.getUniqueId()) ? "&aYes" : "&cNo"))
                            .send();
                }
            }
        } else {
            if (scoreboard != null) {
                scoreboard.getEntries().clear();
            }
        }
    }
}
