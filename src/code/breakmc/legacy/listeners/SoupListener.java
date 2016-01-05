package code.breakmc.legacy.listeners;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.commands.Command_toggledm;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin on 5/4/2015.
 */
public class SoupListener implements Listener {

    public SoupListener() {
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

    @EventHandler
    public void onSoup(PlayerInteractEvent e) {
        Action action = e.getAction();
        Player p = e.getPlayer();
        if (action.name().contains("RIGHT")) {
            if (PlayerUtility.getHealth(p) < PlayerUtility.getMaxHealth(p)) {
                if (e.getItem() != null && e.getItem().getType() == Material.MUSHROOM_SOUP) {
                    e.setCancelled(true);
                    p.setHealth(PlayerUtility.getHealth(p) + 7 > PlayerUtility.getMaxHealth(p) ? PlayerUtility.getMaxHealth(p) : PlayerUtility.getHealth(p) + 7);
                    e.getItem().setType(Material.BOWL);
                }
            } else {
                if (p.getFoodLevel() < 20) {
                    if (e.getItem() != null && e.getItem().getType() == Material.MUSHROOM_SOUP) {
                        e.setCancelled(true);
                        p.setFoodLevel(p.getFoodLevel() + 7 > 20 ? 20 : p.getFoodLevel() + 7);
                        e.getItem().setType(Material.BOWL);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        for (Player all : PlayerUtility.getOnlinePlayers()) {
            if (!Command_toggledm.getToggled().contains(all.getUniqueId().toString())) {
                MessageManager.sendMessage(all, ChatColor.stripColor(e.getDeathMessage()));
            }
        }

        e.setDeathMessage(null);
    }

    @EventHandler
    public void onItemSpawn(final ItemSpawnEvent e) {
        new BukkitRunnable() {
            public void run() {
                if (e.getEntity().getItemStack().getType() == Material.BOWL || e.getEntity().getItemStack().getType() == Material.MUSHROOM_SOUP || e.getEntity().getItemStack().getType() == Material.GLASS_BOTTLE || e.getEntity().getItemStack().getType() == Material.SAND || e.getEntity().getItemStack().getType() == Material.GRAVEL || e.getEntity().getItemStack().getType() == Material.DIRT || e.getEntity().getItemStack().getType() == Material.COBBLESTONE || e.getEntity().getItemStack().getType() == Material.ROTTEN_FLESH || e.getEntity().getItemStack().getType() == Material.STRING || e.getEntity().getItemStack().getType() == Material.BONE || e.getEntity().getItemStack().getType() == Material.ARROW) {
                    e.getEntity().remove();
                }
            }
        }.runTaskLater(Legacy.getInstance(), 20*20L);
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
