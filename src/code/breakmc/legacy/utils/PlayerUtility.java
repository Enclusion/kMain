package code.breakmc.legacy.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerUtility {

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
}
