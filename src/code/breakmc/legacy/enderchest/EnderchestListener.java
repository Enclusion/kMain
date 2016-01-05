package code.breakmc.legacy.enderchest;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.ItemBuilder;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Calvin on 4/26/2015.
 * Project: Legacy
 */
public class EnderchestListener implements Listener {

    private Legacy main = Legacy.getInstance();
    private EnderchestManager ecm = main.getEnderchestManager();

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();

        if (inv.getType().equals(InventoryType.ENDER_CHEST)) {
            e.setCancelled(true);

            if (!main.getSpawnManager().getSpawnProtected().contains(p.getUniqueId())) {
                MessageManager.sendMessage(p, "&cYou cannot open your enderchest without spawn protection!");
                return;
            }

            if (ecm.hasEnderchest(p)) {
                Inventory ec = Bukkit.createInventory(p, 27, ChatColor.DARK_PURPLE + "Enderchest");
                ec.setContents(ecm.getEnderchest(p).getContents());

                ItemStack redBlock = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("&8&l\u2716").build();
                ItemStack donateBlock = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("&c&l\u2716").addLore("&dDonate to unlock this slot!").build();

                if (!p.hasPermission("legacy.enderchest.donator")) {
                    ec.setItem(0, redBlock);
                    ec.setItem(1, redBlock);
                    ec.setItem(2, donateBlock);
                    ec.setItem(6, donateBlock);
                    ec.setItem(7, redBlock);
                    ec.setItem(8, redBlock);
                    ec.setItem(9, redBlock);
                    ec.setItem(10, redBlock);
                    ec.setItem(11, donateBlock);
                    ec.setItem(15, donateBlock);
                    ec.setItem(16, redBlock);
                    ec.setItem(17, redBlock);
                    ec.setItem(18, redBlock);
                    ec.setItem(19, redBlock);
                    ec.setItem(18, redBlock);
                    ec.setItem(19, redBlock);
                    ec.setItem(20, donateBlock);
                    ec.setItem(24, donateBlock);
                    ec.setItem(25, redBlock);
                    ec.setItem(26, redBlock);
                } else {
                    ec.setItem(0, redBlock);
                    ec.setItem(1, redBlock);
                    ec.setItem(7, redBlock);
                    ec.setItem(8, redBlock);
                    ec.setItem(9, redBlock);
                    ec.setItem(10, redBlock);
                    ec.setItem(16, redBlock);
                    ec.setItem(17, redBlock);
                    ec.setItem(18, redBlock);
                    ec.setItem(19, redBlock);
                    ec.setItem(18, redBlock);
                    ec.setItem(19, redBlock);
                    ec.setItem(25, redBlock);
                    ec.setItem(26, redBlock);

                    for (ItemStack item : ec.getContents()) {
                        if (item != null) {
                            if (item.getType() == Material.STAINED_GLASS_PANE) {
                                if (item.getDurability() == 14) {
                                    if (item.hasItemMeta()) {
                                        if (item.getItemMeta().hasDisplayName()) {
                                            if (item.getItemMeta().getDisplayName().contains("\u2716")) {
                                                ec.remove(item);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                p.openInventory(ec);
            } else {
                Inventory ec = Bukkit.createInventory(p, 27, ChatColor.DARK_PURPLE + "Enderchest");

                ItemStack redBlock = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("&8&l\u2716").build();
                ItemStack donateBlock = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("&c&l\u2716").addLore("&dDonate to unlock this slot!").build();

                if (!p.hasPermission("legacy.enderchest.donator")) {
                    ec.setItem(0, redBlock);
                    ec.setItem(1, redBlock);
                    ec.setItem(2, donateBlock);
                    ec.setItem(6, donateBlock);
                    ec.setItem(7, redBlock);
                    ec.setItem(8, redBlock);
                    ec.setItem(9, redBlock);
                    ec.setItem(10, redBlock);
                    ec.setItem(11, donateBlock);
                    ec.setItem(15, donateBlock);
                    ec.setItem(16, redBlock);
                    ec.setItem(17, redBlock);
                    ec.setItem(18, redBlock);
                    ec.setItem(19, redBlock);
                    ec.setItem(18, redBlock);
                    ec.setItem(19, redBlock);
                    ec.setItem(20, donateBlock);
                    ec.setItem(24, donateBlock);
                    ec.setItem(25, redBlock);
                    ec.setItem(26, redBlock);
                } else {
                    ec.setItem(0, redBlock);
                    ec.setItem(1, redBlock);
                    ec.setItem(7, redBlock);
                    ec.setItem(8, redBlock);
                    ec.setItem(9, redBlock);
                    ec.setItem(10, redBlock);
                    ec.setItem(16, redBlock);
                    ec.setItem(17, redBlock);
                    ec.setItem(18, redBlock);
                    ec.setItem(19, redBlock);
                    ec.setItem(18, redBlock);
                    ec.setItem(19, redBlock);
                    ec.setItem(25, redBlock);
                    ec.setItem(26, redBlock);

                    for (ItemStack item : ec.getContents()) {
                        if (item != null) {
                            if (item.getType() == Material.STAINED_GLASS_PANE) {
                                if (item.getDurability() == 14) {
                                    if (item.hasItemMeta()) {
                                        if (item.getItemMeta().hasDisplayName()) {
                                            if (item.getItemMeta().getDisplayName().contains("\u2716")) {
                                                ec.remove(item);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                p.openInventory(ec);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        if (inv.getName().contains(ChatColor.DARK_PURPLE + "Enderchest")) {
            ecm.setEnderchest(p, inv);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item != null) {
            if (item.getType().equals(Material.STAINED_GLASS_PANE)) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasDisplayName()) {
                        if (item.getItemMeta().getDisplayName().contains("\u2716")) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void itemCraft(CraftItemEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getRecipe().getResult().getType() == Material.ENDER_CHEST) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "You cannot craft enderchests.");
        }
    }
}
