package code.breakmc.legacy.kitviewer;

import code.breakmc.legacy.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Calvin on 5/8/2015.
 */
public class KitViewerManager {

    public static void openPremiumView(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&dHover over the items for info!"));

        inv.setItem(10, new ItemBuilder(Material.PAPER).name("&b&lKit Premium").addLore("&aClick here to view what's inside!").build());
        inv.setItem(13, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(11).name("&9&lPremium").addLore("&aClick here to visit our webstore!").build());
        inv.setItem(21, new ItemBuilder(Material.FEATHER).name("&7&lWarps: 10").build());
        inv.setItem(22, new ItemBuilder(Material.EMERALD).name("&a&lPrice: $4.99").build());
        inv.setItem(16, new ItemBuilder(Material.FIREWORK).name("&b&lGlobal Shoutout!").addLore("&5Everyone will know").addLore("&5you've supported BreakMC!").build());
        inv.setItem(23, new ItemBuilder(Material.GOLD_INGOT).name("&6&lReserved Slot").addLore("&eThis means you can log on").addLore("&eeven when the server is full!").build());
        inv.setItem(25, new ItemBuilder(Material.ENDER_CHEST).name("&5&lExtra Slots").addLore("&dYou will get 6 extra enderchest slots for storage.").build());

        inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(5, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(6, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(7, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(8, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(11, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(12, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(14, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(15, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(17, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(18, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(19, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(26, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(27, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(28, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(29, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(30, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(31, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(32, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(33, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(34, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(35, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        p.openInventory(inv);
    }

    public static void openVIPView(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&dHover over the items for info!"));

        inv.setItem(10, new ItemBuilder(Material.PAPER).name("&b&lKit Vip").addLore("&aClick here to view what's inside!").build());
        inv.setItem(13, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(13).name("&2&lVip").addLore("&aClick here to visit our webstore!").build());
        inv.setItem(21, new ItemBuilder(Material.FEATHER).name("&7&lWarps: 15").build());
        inv.setItem(22, new ItemBuilder(Material.EMERALD).name("&a&lPrice: $9.99").build());
        inv.setItem(16, new ItemBuilder(Material.FIREWORK).name("&b&lGlobal Shoutout!").addLore("&5Everyone will know").addLore("&5you've supported BreakMC!").build());
        inv.setItem(23, new ItemBuilder(Material.GOLD_INGOT).name("&6&lReserved Slot").addLore("&eThis means you can log on").addLore("&eeven when the server is full!").build());
        inv.setItem(25, new ItemBuilder(Material.ENDER_CHEST).name("&5&lExtra Slots").addLore("&dYou will get 6 extra enderchest slots for storage.").build());

        inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(5, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(6, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(7, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(8, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(11, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(12, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(14, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(15, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(17, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(18, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(19, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(26, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(27, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(28, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(29, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(30, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(31, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(32, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(33, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(34, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(35, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        p.openInventory(inv);
    }

    public static void openProView(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&dHover over the items for info!"));

        inv.setItem(10, new ItemBuilder(Material.PAPER).name("&b&lKit Pro").addLore("&aClick here to view what's inside!").build());
        inv.setItem(13, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(1).name("&6&lPro").addLore("&aClick here to visit our webstore!").build());
        inv.setItem(21, new ItemBuilder(Material.FEATHER).name("&7&lWarps: 20").build());
        inv.setItem(22, new ItemBuilder(Material.EMERALD).name("&a&lPrice: $19.99").build());
        inv.setItem(16, new ItemBuilder(Material.FIREWORK).name("&b&lGlobal Shoutout!").addLore("&5Everyone will know").addLore("&5you've supported BreakMC!").build());
        inv.setItem(23, new ItemBuilder(Material.GOLD_INGOT).name("&6&lReserved Slot").addLore("&eThis means you can log on").addLore("&eeven when the server is full!").build());
        inv.setItem(25, new ItemBuilder(Material.ENDER_CHEST).name("&5&lExtra Slots").addLore("&dYou will get 6 extra enderchest slots for storage.").build());

        inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(5, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(6, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(7, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(8, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(11, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(12, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(14, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(15, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(17, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(18, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(19, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(26, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(27, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(28, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(29, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(30, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(31, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(32, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(33, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(34, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(35, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        p.openInventory(inv);
    }

    public static void openLegendView(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&dHover over the items for info!"));

        inv.setItem(10, new ItemBuilder(Material.PAPER).name("&b&lKit Legend").addLore("&aClick here to view what's inside!").build());
        inv.setItem(13, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("&a&lLegend").addLore("&aClick here to visit our webstore!").build());
        inv.setItem(21, new ItemBuilder(Material.FEATHER).name("&7&lWarps: 25").build());
        inv.setItem(22, new ItemBuilder(Material.EMERALD).name("&a&lPrice: $29.99").build());
        inv.setItem(16, new ItemBuilder(Material.FIREWORK).name("&b&lGlobal Shoutout!").addLore("&5Everyone will know").addLore("&5you've supported BreakMC!").build());
        inv.setItem(23, new ItemBuilder(Material.GOLD_INGOT).name("&6&lReserved Slot").addLore("&eThis means you can log on").addLore("&eeven when the server is full!").build());
        inv.setItem(25, new ItemBuilder(Material.ENDER_CHEST).name("&5&lExtra Slots").addLore("&dYou will get 6 extra enderchest slots for storage.").build());

        inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(5, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(6, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(7, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(8, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(11, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(12, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(14, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(15, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(17, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(18, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(19, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(26, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(27, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(28, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(29, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(30, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(31, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(32, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(33, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(34, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(35, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        p.openInventory(inv);
    }

    public static void openMasterView(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&dHover over the items for info!"));

        inv.setItem(10, new ItemBuilder(Material.PAPER).name("&b&lKit Master").addLore("&aClick here to view what's inside!").build());
        inv.setItem(13, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(4).name("&e&lMaster").addLore("&aClick here to visit our webstore!").build());
        inv.setItem(21, new ItemBuilder(Material.FEATHER).name("&7&lWarps: 30").build());
        inv.setItem(22, new ItemBuilder(Material.EMERALD).name("&a&lPrice: $49.99").build());
        inv.setItem(16, new ItemBuilder(Material.FIREWORK).name("&b&lGlobal Shoutout!").addLore("&5Everyone will know").addLore("&5you've supported BreakMC!").build());
        inv.setItem(23, new ItemBuilder(Material.GOLD_INGOT).name("&6&lReserved Slot").addLore("&eThis means you can log on").addLore("&eeven when the server is full!").build());
        inv.setItem(25, new ItemBuilder(Material.ENDER_CHEST).name("&5&lExtra Slots").addLore("&dYou will get 6 extra enderchest slots for storage.").build());

        inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(5, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(6, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(7, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(8, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(11, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(12, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(14, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(15, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(17, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(18, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(26, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(27, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(28, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(29, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(30, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(31, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(32, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(33, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(34, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(35, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        p.openInventory(inv);
    }

    public static void openInfamousView(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&dHover over the items for info!"));

        inv.setItem(10, new ItemBuilder(Material.PAPER).name("&b&lKit Infamous").addLore("&aClick here to view what's inside!").build());
        inv.setItem(19, new ItemBuilder(Material.PAPER).name("&b&lKit High").addLore("&aClick here to view what's inside!").build());
        inv.setItem(13, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(13).name("&6&lInfamous").addLore("&aClick here to visit our webstore!").build());
        inv.setItem(21, new ItemBuilder(Material.FEATHER).name("&7&lWarps: 40").build());
        inv.setItem(22, new ItemBuilder(Material.EMERALD).name("&a&lPrice: $79.99").build());
        inv.setItem(16, new ItemBuilder(Material.FIREWORK).name("&b&lGlobal Shoutout!").addLore("&5Everyone will know").addLore("&5you've supported BreakMC!").build());
        inv.setItem(23, new ItemBuilder(Material.GOLD_INGOT).name("&6&lReserved Slot").addLore("&eThis means you can log on").addLore("&eeven when the server is full!").build());
        inv.setItem(25, new ItemBuilder(Material.ENDER_CHEST).name("&5&lExtra Slots").addLore("&dYou will get 6 extra enderchest slots for storage.").build());

        inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(5, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(6, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(7, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(8, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(11, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(12, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(14, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(15, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(17, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(18, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(26, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(27, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(28, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(29, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(30, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(31, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(32, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(33, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(34, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(35, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        p.openInventory(inv);
    }

    public static void openAdvancedView(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&dHover over the items for info!"));

        inv.setItem(10, new ItemBuilder(Material.PAPER).name("&b&lKit Advanced").addLore("&aClick here to view what's inside!").build());
        inv.setItem(19, new ItemBuilder(Material.PAPER).name("&b&lKit Higher").addLore("&aClick here to view what's inside!").build());
        inv.setItem(13, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(3).name("&b&lAdvanced").addLore("&aClick here to visit our webstore!").build());
        inv.setItem(21, new ItemBuilder(Material.FEATHER).name("&7&lWarps: 50").build());
        inv.setItem(22, new ItemBuilder(Material.EMERALD).name("&a&lPrice: $99.99").build());
        inv.setItem(16, new ItemBuilder(Material.FIREWORK).name("&b&lGlobal Shoutout!").addLore("&5Everyone will know").addLore("&5you've supported BreakMC!").build());
        inv.setItem(23, new ItemBuilder(Material.GOLD_INGOT).name("&6&lReserved Slot").addLore("&eThis means you can log on").addLore("&eeven when the server is full!").build());
        inv.setItem(25, new ItemBuilder(Material.ENDER_CHEST).name("&5&lExtra Slots").addLore("&dYou will get 6 extra enderchest slots for storage.").build());

        inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(5, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(6, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(7, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(8, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(11, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(12, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(14, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(15, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(17, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(18, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(26, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(27, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(28, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(29, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(30, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(31, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(32, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(33, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(34, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(35, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        p.openInventory(inv);
    }

    public static void openExtremeView(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&dHover over the items for info!"));

        inv.setItem(10, new ItemBuilder(Material.PAPER).name("&b&lKit Extreme").addLore("&aClick here to view what's inside!").build());
        inv.setItem(19, new ItemBuilder(Material.PAPER).name("&b&lKit Highest").addLore("&aClick here to view what's inside!").build());
        inv.setItem(13, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("&4&lExtreme").addLore("&aClick here to visit our webstore!").build());
        inv.setItem(21, new ItemBuilder(Material.FEATHER).name("&7&lWarps: 60").build());
        inv.setItem(22, new ItemBuilder(Material.EMERALD).name("&a&lPrice: $249.99").build());
        inv.setItem(16, new ItemBuilder(Material.FIREWORK).name("&b&lGlobal Shoutout!").addLore("&5Everyone will know").addLore("&5you've supported BreakMC!").build());
        inv.setItem(23, new ItemBuilder(Material.GOLD_INGOT).name("&6&lReserved Slot").addLore("&eThis means you can log on").addLore("&eeven when the server is full!").build());
        inv.setItem(25, new ItemBuilder(Material.ENDER_CHEST).name("&5&lExtra Slots").addLore("&dYou will get 6 extra enderchest slots for storage.").build());

        inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(5, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(6, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(7, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(8, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(11, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(12, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(14, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(15, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(17, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(18, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(26, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(27, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(28, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(29, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(30, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(31, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(32, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(33, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(34, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        inv.setItem(35, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        p.openInventory(inv);
    }

    public static void openPremiumKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&9Premium"));

        ItemStack helm = new ItemStack(Material.IRON_HELMET, 1);

        ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemStack legs = new ItemStack(Material.IRON_LEGGINGS, 1);
        legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);

        ItemStack p1 = new ItemStack(Material.POTION, 1);
        p1.setDurability((short) 8233);

        ItemStack p2 = new ItemStack(Material.POTION, 1);
        p2.setDurability((short) 8226);

        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 29);

        ItemStack exp = new ItemStack(Material.EXP_BOTTLE, 8);

        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addEnchantment(Enchantment.DIG_SPEED, 1);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);

        ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);

        inv.setItem(0, helm);
        inv.setItem(9, chest);
        inv.setItem(18, legs);
        inv.setItem(27, boots);
        inv.setItem(28, sword);
        inv.setItem(29, p1);
        inv.setItem(30, p2);
        inv.setItem(31, soup);

        inv.setItem(7, exp);
        inv.setItem(8, pick);
        inv.setItem(26, axe);
        inv.setItem(35, shovel);

        p.openInventory(inv);
    }

    public static void openVipKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&2Vip"));

        ItemStack helm = new ItemStack(Material.IRON_HELMET, 1);

        ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        ItemStack legs = new ItemStack(Material.IRON_LEGGINGS, 1);
        legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);

        ItemStack p1 = new ItemStack(Material.POTION, 1);
        p1.setDurability((short) 8233);

        ItemStack p2 = new ItemStack(Material.POTION, 1);
        p2.setDurability((short) 8226);

        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 29);

        ItemStack exp = new ItemStack(Material.EXP_BOTTLE, 16);

        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addEnchantment(Enchantment.DIG_SPEED, 2);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);

        ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
        shovel.addEnchantment(Enchantment.DIG_SPEED, 1);

        inv.setItem(0, helm);
        inv.setItem(9, chest);
        inv.setItem(18, legs);
        inv.setItem(27, boots);
        inv.setItem(28, sword);
        inv.setItem(29, p1);
        inv.setItem(30, p2);
        inv.setItem(31, soup);

        inv.setItem(7, exp);
        inv.setItem(8, pick);
        inv.setItem(26, axe);
        inv.setItem(35, shovel);

        p.openInventory(inv);
    }

    public static void openProKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&6Pro"));

        ItemStack helm = new ItemStack(Material.IRON_HELMET, 1);
        helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        ItemStack legs = new ItemStack(Material.IRON_LEGGINGS, 1);
        legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);

        ItemStack p1 = new ItemStack(Material.POTION, 1);
        p1.setDurability((short) 8233);

        ItemStack p2 = new ItemStack(Material.POTION, 1);
        p2.setDurability((short) 8226);

        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 29);

        ItemStack exp = new ItemStack(Material.EXP_BOTTLE, 16);

        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addEnchantment(Enchantment.DIG_SPEED, 2);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
        axe.addEnchantment(Enchantment.DIG_SPEED, 1);

        ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
        shovel.addEnchantment(Enchantment.DIG_SPEED, 1);

        inv.setItem(0, helm);
        inv.setItem(9, chest);
        inv.setItem(18, legs);
        inv.setItem(27, boots);
        inv.setItem(28, sword);
        inv.setItem(29, p1);
        inv.setItem(30, p2);
        inv.setItem(31, soup);

        inv.setItem(7, exp);
        inv.setItem(8, pick);
        inv.setItem(26, axe);
        inv.setItem(35, shovel);

        p.openInventory(inv);
    }

    public static void openLegendKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&aLegend"));

        ItemStack helm = new ItemStack(Material.IRON_HELMET, 1);
        helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);

        ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);

        ItemStack p1 = new ItemStack(Material.POTION, 1);
        p1.setDurability((short) 8233);

        ItemStack p2 = new ItemStack(Material.POTION, 1);
        p2.setDurability((short) 8226);

        ItemStack p3 = new ItemStack(Material.POTION, 1);
        p3.setDurability((short) 8259);

        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 28);

        ItemStack exp = new ItemStack(Material.EXP_BOTTLE, 24);

        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addEnchantment(Enchantment.DIG_SPEED, 2);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
        axe.addEnchantment(Enchantment.DIG_SPEED, 1);

        ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
        shovel.addEnchantment(Enchantment.DIG_SPEED, 1);

        inv.setItem(0, helm);
        inv.setItem(9, chest);
        inv.setItem(18, legs);
        inv.setItem(27, boots);
        inv.setItem(28, sword);
        inv.setItem(29, p1);
        inv.setItem(30, p2);
        inv.setItem(31, p3);
        inv.setItem(32, soup);

        inv.setItem(7, exp);
        inv.setItem(8, pick);
        inv.setItem(26, axe);
        inv.setItem(35, shovel);

        p.openInventory(inv);
    }

    public static void openMasterKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&eMaster"));

        ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);

        ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

        ItemStack p1 = new ItemStack(Material.POTION, 2);
        p1.setDurability((short) 8233);

        ItemStack p2 = new ItemStack(Material.POTION, 2);
        p2.setDurability((short) 8226);

        ItemStack p3 = new ItemStack(Material.POTION, 1);
        p3.setDurability((short) 8259);

        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 26);

        ItemStack exp = new ItemStack(Material.EXP_BOTTLE, 48);

        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addEnchantment(Enchantment.DIG_SPEED, 4);
        pick.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
        axe.addEnchantment(Enchantment.DIG_SPEED, 2);

        ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
        shovel.addEnchantment(Enchantment.DIG_SPEED, 2);

        inv.setItem(0, helm);
        inv.setItem(9, chest);
        inv.setItem(18, legs);
        inv.setItem(27, boots);
        inv.setItem(28, sword);
        inv.setItem(29, p1);
        inv.setItem(30, p2);
        inv.setItem(31, p3);
        inv.setItem(32, soup);

        inv.setItem(7, exp);
        inv.setItem(8, pick);
        inv.setItem(26, axe);
        inv.setItem(35, shovel);

        p.openInventory(inv);
    }

    public static void openInfamousKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&dInfamous"));

        ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
        helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);

        ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

        ItemStack p1 = new ItemStack(Material.POTION, 2);
        p1.setDurability((short) 8233);

        ItemStack p2 = new ItemStack(Material.POTION, 2);
        p2.setDurability((short) 8226);

        ItemStack p3 = new ItemStack(Material.POTION, 1);
        p3.setDurability((short) 8259);

        ItemStack p4 = new ItemStack(Material.POTION, 1);
        p4.setDurability((short) 8257);

        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 25);

        ItemStack exp = new ItemStack(Material.EXP_BOTTLE, 56);

        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addEnchantment(Enchantment.DIG_SPEED, 4);
        pick.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
        axe.addEnchantment(Enchantment.DIG_SPEED, 3);

        ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
        shovel.addEnchantment(Enchantment.DIG_SPEED, 3);

        inv.setItem(0, helm);
        inv.setItem(9, chest);
        inv.setItem(18, legs);
        inv.setItem(27, boots);
        inv.setItem(28, sword);
        inv.setItem(29, p1);
        inv.setItem(30, p2);
        inv.setItem(31, p3);
        inv.setItem(32, p4);
        inv.setItem(33, soup);

        inv.setItem(7, exp);
        inv.setItem(8, pick);
        inv.setItem(26, axe);
        inv.setItem(35, shovel);

        p.openInventory(inv);
    }

    public static void openAdvancedKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&bAdvanced"));

        ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
        helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        helm.addEnchantment(Enchantment.DURABILITY, 1);

        ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

        ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        boots.addEnchantment(Enchantment.DURABILITY, 1);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

        ItemStack p1 = new ItemStack(Material.POTION, 2);
        p1.setDurability((short) 8233);

        ItemStack p2 = new ItemStack(Material.POTION, 2);
        p2.setDurability((short) 8226);

        ItemStack p3 = new ItemStack(Material.POTION, 1);
        p3.setDurability((short) 8259);

        ItemStack p4 = new ItemStack(Material.POTION, 1);
        p4.setDurability((short) 8257);

        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 25);

        ItemStack exp = new ItemStack(Material.EXP_BOTTLE, 64);

        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addEnchantment(Enchantment.DIG_SPEED, 5);
        pick.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
        axe.addEnchantment(Enchantment.DIG_SPEED, 4);

        ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
        shovel.addEnchantment(Enchantment.DIG_SPEED, 4);

        inv.setItem(0, helm);
        inv.setItem(9, chest);
        inv.setItem(18, legs);
        inv.setItem(27, boots);
        inv.setItem(28, sword);
        inv.setItem(29, p1);
        inv.setItem(30, p2);
        inv.setItem(31, p3);
        inv.setItem(32, p4);
        inv.setItem(33, soup);

        inv.setItem(7, exp);
        inv.setItem(8, pick);
        inv.setItem(26, axe);
        inv.setItem(35, shovel);

        p.openInventory(inv);
    }

    public static void openExtremeKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.translateAlternateColorCodes('&', "&6Extreme"));

        ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
        helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        helm.addEnchantment(Enchantment.DURABILITY, 1);

        ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        chest.addEnchantment(Enchantment.DURABILITY, 2);

        ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        legs.addEnchantment(Enchantment.DURABILITY, 2);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        boots.addEnchantment(Enchantment.DURABILITY, 1);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

        ItemStack p1 = new ItemStack(Material.POTION, 2);
        p1.setDurability((short) 8233);

        ItemStack p2 = new ItemStack(Material.POTION, 2);
        p2.setDurability((short) 8226);

        ItemStack p3 = new ItemStack(Material.POTION, 1);
        p3.setDurability((short) 8259);

        ItemStack p4 = new ItemStack(Material.POTION, 1);
        p4.setDurability((short) 8257);

        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 25);

        ItemStack exp = new ItemStack(Material.EXP_BOTTLE, 64);

        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addEnchantment(Enchantment.DIG_SPEED, 5);
        pick.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
        pick.addEnchantment(Enchantment.DURABILITY, 2);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
        axe.addEnchantment(Enchantment.DIG_SPEED, 4);

        ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
        shovel.addEnchantment(Enchantment.DIG_SPEED, 4);

        inv.setItem(0, helm);
        inv.setItem(9, chest);
        inv.setItem(18, legs);
        inv.setItem(27, boots);
        inv.setItem(28, sword);
        inv.setItem(29, p1);
        inv.setItem(30, p2);
        inv.setItem(31, p3);
        inv.setItem(32, p4);
        inv.setItem(33, soup);

        inv.setItem(7, exp);
        inv.setItem(8, pick);
        inv.setItem(26, axe);
        inv.setItem(35, shovel);

        p.openInventory(inv);
    }

    public static void openHighKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 27, ChatColor.translateAlternateColorCodes('&', "&6High"));

        ItemStack glowstone = new ItemStack(Material.GLOWSTONE_DUST, 8);
        ItemStack sugar = new ItemStack(Material.GLOWSTONE_DUST, 8);
        ItemStack netherwart = new ItemStack(Material.NETHER_STALK, 16);
        ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 8);
        ItemStack diamond = new ItemStack(Material.DIAMOND, 2);
        ItemStack egg = new ItemStack(Material.MONSTER_EGG, 1);
        egg.setDurability((short) 90);

        ItemStack cream = new ItemStack(Material.MAGMA_CREAM, 8);
        ItemStack powder = new ItemStack(Material.BLAZE_POWDER, 8);
        ItemStack stand = new ItemStack(Material.BREWING_STAND_ITEM, 3);
        ItemStack tnt = new ItemStack(Material.TNT, 5);
        ItemStack iron = new ItemStack(Material.IRON_INGOT, 8);
        ItemStack firework = new ItemStack(Material.FIREWORK, 5);

        ItemStack sulphur = new ItemStack(Material.SULPHUR, 16);
        ItemStack tear = new ItemStack(Material.GHAST_TEAR, 4);
        ItemStack redstone = new ItemStack(Material.REDSTONE, 16);
        ItemStack glass = new ItemStack(Material.GLASS, 64);
        ItemStack gold = new ItemStack(Material.GOLD_INGOT, 8);
        ItemStack saddle = new ItemStack(Material.SADDLE, 1);

        inv.setItem(0, glowstone);
        inv.setItem(1, sugar);
        inv.setItem(2, netherwart);
        inv.setItem(6, obsidian);
        inv.setItem(7, diamond);
        inv.setItem(8, egg);

        inv.setItem(9, cream);
        inv.setItem(10, powder);
        inv.setItem(11, stand);
        inv.setItem(15, tnt);
        inv.setItem(16, iron);
        inv.setItem(17, firework);

        inv.setItem(18, sulphur);
        inv.setItem(19, tear);
        inv.setItem(20, redstone);
        inv.setItem(24, glass);
        inv.setItem(25, gold);
        inv.setItem(26, saddle);

        p.openInventory(inv);
    }

    public static void openHigherKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 27, ChatColor.translateAlternateColorCodes('&', "&6Higher"));

        ItemStack glowstone = new ItemStack(Material.GLOWSTONE_DUST, 16);
        ItemStack sugar = new ItemStack(Material.GLOWSTONE_DUST, 16);
        ItemStack netherwart = new ItemStack(Material.NETHER_STALK, 32);
        ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 16);
        ItemStack diamond = new ItemStack(Material.DIAMOND, 4);
        ItemStack egg = new ItemStack(Material.MONSTER_EGG, 1);
        egg.setDurability((short) 90);

        ItemStack cream = new ItemStack(Material.MAGMA_CREAM, 16);
        ItemStack powder = new ItemStack(Material.BLAZE_POWDER, 16);
        ItemStack stand = new ItemStack(Material.BREWING_STAND_ITEM, 6);
        ItemStack tnt = new ItemStack(Material.TNT, 10);
        ItemStack iron = new ItemStack(Material.IRON_INGOT, 16);
        ItemStack firework = new ItemStack(Material.FIREWORK, 8);

        ItemStack sulphur = new ItemStack(Material.SULPHUR, 32);
        ItemStack tear = new ItemStack(Material.GHAST_TEAR, 8);
        ItemStack redstone = new ItemStack(Material.REDSTONE, 32);
        ItemStack glass = new ItemStack(Material.GLASS, 64);
        ItemStack gold = new ItemStack(Material.GOLD_INGOT, 16);
        ItemStack saddle = new ItemStack(Material.SADDLE, 2);

        inv.setItem(0, glowstone);
        inv.setItem(1, sugar);
        inv.setItem(2, netherwart);
        inv.setItem(6, obsidian);
        inv.setItem(7, diamond);
        inv.setItem(8, egg);

        inv.setItem(9, cream);
        inv.setItem(10, powder);
        inv.setItem(11, stand);
        inv.setItem(15, tnt);
        inv.setItem(16, iron);
        inv.setItem(17, firework);

        inv.setItem(18, sulphur);
        inv.setItem(19, tear);
        inv.setItem(20, redstone);
        inv.setItem(24, glass);
        inv.setItem(25, gold);
        inv.setItem(26, saddle);

        p.openInventory(inv);
    }

    public static void openHighestKit(Player p) {
        Inventory inv = Bukkit.createInventory(p, 27, ChatColor.translateAlternateColorCodes('&', "&6Highest"));

        ItemStack glowstone = new ItemStack(Material.GLOWSTONE_DUST, 32);
        ItemStack sugar = new ItemStack(Material.GLOWSTONE_DUST, 32);
        ItemStack netherwart = new ItemStack(Material.NETHER_STALK, 64);
        ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 16);
        ItemStack diamond = new ItemStack(Material.DIAMOND, 8);
        ItemStack egg = new ItemStack(Material.MONSTER_EGG, 1);
        egg.setDurability((short) 100);

        ItemStack cream = new ItemStack(Material.MAGMA_CREAM, 32);
        ItemStack powder = new ItemStack(Material.BLAZE_POWDER, 32);
        ItemStack stand = new ItemStack(Material.BREWING_STAND_ITEM, 9);
        ItemStack tnt = new ItemStack(Material.TNT, 24);
        ItemStack iron = new ItemStack(Material.IRON_INGOT, 16);
        ItemStack firework = new ItemStack(Material.FIREWORK, 12);

        ItemStack sulphur = new ItemStack(Material.SULPHUR, 32);
        ItemStack tear = new ItemStack(Material.GHAST_TEAR, 8);
        ItemStack redstone = new ItemStack(Material.REDSTONE, 32);
        ItemStack glass = new ItemStack(Material.GLASS, 64);
        ItemStack gold = new ItemStack(Material.GOLD_INGOT, 16);
        ItemStack saddle = new ItemStack(Material.SADDLE, 1);

        inv.setItem(0, glowstone);
        inv.setItem(1, sugar);
        inv.setItem(2, netherwart);
        inv.setItem(6, obsidian);
        inv.setItem(7, diamond);
        inv.setItem(8, egg);

        inv.setItem(9, cream);
        inv.setItem(10, powder);
        inv.setItem(11, stand);
        inv.setItem(15, tnt);
        inv.setItem(16, iron);
        inv.setItem(17, firework);

        inv.setItem(18, sulphur);
        inv.setItem(19, tear);
        inv.setItem(20, redstone);
        inv.setItem(24, glass);
        inv.setItem(25, gold);
        inv.setItem(26, saddle);

        p.openInventory(inv);
    }
}
