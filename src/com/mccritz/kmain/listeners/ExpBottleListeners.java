package com.mccritz.kmain.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ExpBottleListeners implements Listener {

    public ExpBottleListeners() {
        createRecipes();
    }

    public static ItemStack xpBottles() {
        ItemStack xpBottles = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta meta = xpBottles.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "XP Bottle");

        xpBottles.setItemMeta(meta);
        return xpBottles;
    }

    public static void createRecipes() {
        ShapelessRecipe xpBottle = new ShapelessRecipe(xpBottles()).addIngredient(1, Material.GLASS_BOTTLE);

        Bukkit.getServer().addRecipe(xpBottle);
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        Player p = (Player) event.getWhoClicked();

        if (event.getCurrentItem().equals(xpBottles())) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
                return;
            }

            ItemStack experiencePotion = event.getCurrentItem();
            ItemMeta experienceMeta = experiencePotion.getItemMeta();
            List<String> stringList = new ArrayList<>();
            stringList.add("");
            int xpLevel = levelToExp(p.getLevel());
            stringList.add(ChatColor.GOLD + "Exp" + ChatColor.WHITE + ": " + xpLevel);

            experienceMeta.setLore(stringList);
            experiencePotion.setItemMeta(experienceMeta);

            p.setExp(0.0F);
            p.setLevel(0);
        }
    }

    @EventHandler
    public void onExpSplash(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) && (p.getItemInHand().getType() == Material.EXP_BOTTLE) && (p.getItemInHand().hasItemMeta())) {
            ItemMeta meta = p.getItemInHand().getItemMeta();

            if (p.getItemInHand().getItemMeta().hasLore()) {
                if (meta.getDisplayName().equals(ChatColor.GOLD + "XP Bottle")) {
                    event.setCancelled(true);

                    int exp = Integer.valueOf(ChatColor.stripColor(meta.getLore().get(1)).split(": ")[1]);

                    if (exp < 0.0D) {
                        ItemStack temp = p.getItemInHand().clone();
                        temp.setAmount(p.getItemInHand().getAmount() - 1);
                        p.getInventory().remove(p.getItemInHand());

                        p.getInventory().addItem(temp);
                        p.updateInventory();
                        return;
                    }

                    ItemStack temp = p.getItemInHand().clone();
                    temp.setAmount(p.getItemInHand().getAmount() - 1);
                    p.setItemInHand(temp.getAmount() <= 0 ? new ItemStack(Material.AIR) : temp);
                    p.updateInventory();
                    p.giveExp(exp);
                }
            }
        }
    }

    public static int levelToExp(int level) {
        if (level <= 15) {
            return 17 * level;
        } else if (level <= 30) {
            return (3 * level * level / 2) - (59 * level / 2) + 360;
        } else {
            return (7 * level * level / 2) - (303 * level / 2) + 2220;
        }
    }
}
