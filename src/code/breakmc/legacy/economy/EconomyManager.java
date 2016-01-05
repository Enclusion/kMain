package code.breakmc.legacy.economy;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.utils.ItemBuilder;
import code.breakmc.legacy.utils.MessageManager;
import com.sun.media.sound.InvalidDataException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Calvin on 5/20/2015.
 */
public class EconomyManager implements CommandExecutor {

    private Legacy main = Legacy.getInstance();
    private ProfileManager pm = main.getProfileManager();
    private final ArrayList<ShopItem> items = new ArrayList<>();
    private double buysell = 0.8;
    private Inventory itemInventory1 = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Item Shop");
    private Inventory itemInventory2 = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Item Shop");

    public EconomyManager() {
        loadConfig();
        loadShopGUI();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;
        String cmd = command.getName();
        Integer amount = 1;
        int searchid = -1;

        if (cmd.equalsIgnoreCase("buy")) {
            if (args.length > 0 && args.length < 3) {
                try {
                    searchid = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {}

                if (args.length >= 2) {
                    amount = getAmount(args[1], p);

                    if (amount == null) {
                        return true;
                    }
                }

                ShopItem item = getShopItem(args, p, searchid);

                if (item == null)
                    return true;

                ItemStack stack = new ItemStack(item.id, item.amount * amount, (short) (item.damage != -1 ? item.damage : 0));

                if (!hasEnough(p, item.price * amount)) {
                    MessageManager.sendMessage(p, "&7You do not have enough money to purchase &c" + amount + " &7" + item.getName() + ".");
                    return true;
                }

                if (p.getInventory().firstEmpty() == -1) {
                    MessageManager.sendMessage(p, "&cYour inventory is full.");
                    return true;
                } else {
                    int spacesNeeded;

                    if (stack.getMaxStackSize() < 2) {
                        spacesNeeded = stack.getAmount();
                    } else {
                        spacesNeeded = stack.getAmount() / 64;
                    }

                    int freeSpace = getFreeInventorySpace(p);

                    if (freeSpace < spacesNeeded) {
                        MessageManager.sendMessage(p, "&cYou do not have enough inventory space!");
                        return true;
                    }
                }

                subtractFunds(p, item.price * amount);
                MessageManager.sendMessage(p, "&7You have purchased &a" + amount + " " + item.getName() + " &7for &a$" + formatDouble((item.getPrice() * amount)) + "&7. New balance: &a$" + formatDouble(pm.getProfile(p.getUniqueId()).getBalance()));
                addItem(p, stack);
            } else {
                MessageManager.sendMessage(p, "&cImproper usage! /buy (item) (amount)");
            }
        }
        if (cmd.equalsIgnoreCase("sell")) {
            if (args.length == 1) {
                if (p.getGameMode() == GameMode.CREATIVE) {
                    MessageManager.sendMessage(p, "&cYou cannot sell items while in creative.");
                    return true;
                }

                if (args[0].equalsIgnoreCase("all")) {
                    sellAllItemInHand(p);
                    return true;
                }

                amount = getAmount(args[0], p);

                if (amount == null)
                    return true;

                sellItemInHand(p, amount);
                return true;
            } else {
                MessageManager.sendMessage(p, "&cImproper usage! /sell (amount;all)");
            }
        }
        if (cmd.equalsIgnoreCase("price")) {
            if (args.length == 1) {
                ShopItem item = getShopItem(args, p, searchid);

                if (item == null)
                    return true;

                MessageManager.sendMessage(p, "&a" + item.getName() + " &7costs &a$" + formatDouble(item.getPrice()));
                return true;
            } else if (args.length == 2) {
                amount = getAmount(args[1], p);

                if (amount == null)
                    return true;

                ShopItem item = getShopItem(args, p, searchid);

                if (item == null)
                    return true;

                MessageManager.sendMessage(p, "&a" + amount + " &7of &a" + item.getName() + " &7costs &a$" + formatDouble(item.getPrice() * amount));
                return true;
            } else {
                MessageManager.sendMessage(p, "&cImproper usage! /price (item) (amount)");
            }
        }
        if (cmd.equalsIgnoreCase("value")) {
            if (args.length == 1) {
                ShopItem item = getShopItem(args, p, searchid);

                if (item == null)
                    return true;

                MessageManager.sendMessage(p, "&a" + item.getName() + " &7is worth &a$" + formatDouble(getPrice(new ItemStack(item.id, item.getAmount(), (short) (item.damage != -1 ? item.damage : 0)), null)));
                return true;
            } else if (args.length == 2) {
                amount = getAmount(args[1], p);

                if (amount == null)
                    return true;

                ShopItem item = getShopItem(args, p, searchid);

                if (item == null)
                    return true;

                MessageManager.sendMessage(p, "&a" + amount + " &7of &a" + item.getName() + " &7is worth &a$" + formatDouble(getPrice(new ItemStack(item.id, amount, (short) (item.damage != -1 ? item.damage : 0)), null)));
                return true;
            } else {
                MessageManager.sendMessage(p, "&cImproper usage! /value (item) (amount)");
            }
        }
        if (cmd.equalsIgnoreCase("pay")) {
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player t = Bukkit.getPlayer(args[0]);
                    try {
                        BigDecimal money = new BigDecimal(args[1]).setScale(2, BigDecimal.ROUND_DOWN);

                        if (!(money.doubleValue() > 0)) {
                            MessageManager.sendMessage(p, "&cYou must enter an amount above 0.");
                            return true;
                        }

                        if (!hasEnough(p, money.doubleValue())) {
                            MessageManager.sendMessage(p, "&cYou do not have that much.");
                            return true;
                        }

                        pm.getProfile(p.getUniqueId()).setBalance(pm.getProfile(p.getUniqueId()).getBalance() - money.doubleValue());
                        pm.getProfile(t.getUniqueId()).setBalance(pm.getProfile(t.getUniqueId()).getBalance() + money.doubleValue());

                        MessageManager.sendMessage(p, "&7You have payed &b" + t.getName() + " &a$" + formatDouble(money.doubleValue()));
                        MessageManager.sendMessage(t, "&b" + p.getName() + " &7has payed you &a$" + formatDouble(money.doubleValue()));
                    } catch (NumberFormatException e) {
                        MessageManager.sendMessage(p, "&cYou must enter an valid amount.");
                        return false;
                    }
                } else {
                    MessageManager.sendMessage(p, "&cThat player is not valid.");
                }
            } else {
                MessageManager.sendMessage(p, "&cImproper usage! /pay (player) (amount)");
            }
        }

        if (cmd.equalsIgnoreCase("shop")) {
            if (args.length == 0) {
                p.openInventory(itemInventory1);
            }
        }

        return false;
    }

    public boolean loadConfig() {
        List<String> slist;

        items.clear();

        File cfile = new File(main.getDataFolder(), "config.yml");

        FileConfiguration config = YamlConfiguration.loadConfiguration(cfile);

        buysell = config.getDouble("economy.buy-sell-ratio", buysell);
        slist = config.getStringList("shop-items");

        main.getLogger().log(Level.INFO, "Preparing to load " + slist.size() + " shop items.");
        items.clear();
        try {
            for (String s : slist) {
                String[] parts = s.split(":");

                if (parts.length == 4) {
                    items.add(new ShopItem(Integer.parseInt(parts[0]), parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3])));
                } else if (parts.length == 5) {
                    items.add(new ShopItem(Integer.parseInt(parts[0]), parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]), Integer.parseInt(parts[4])));
                } else {
                    throw new InvalidDataException("Wrong size of arguments in ShopItems: " + parts[1]);
                }
            }

            main.getLogger().log(Level.INFO, "Successfully loaded " + items.size() + " shop items.");
            return true;
        } catch (InvalidDataException e) {
            main.getLogger().log(Level.WARNING, "Failed to load shop items!");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            main.getLogger().log(Level.WARNING, "Failed to load shop items!");
            e.printStackTrace();
            return false;
        }
    }

    public void loadShopGUI() {
        List<ShopItem> sitems = new ArrayList<>();
        sitems.addAll(items);

        itemInventory1.clear();
        itemInventory2.clear();

        itemInventory1.setItem(53, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("&aNext Page").build());
        itemInventory2.setItem(45, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("&aPrevious Page").build());

        for (int i = 0; i < items.size(); i++) {
            ShopItem item = sitems.get(i);

            if (i <= 44) {
                itemInventory1.addItem(new ItemBuilder(Material.getMaterial(item.getId())).durability((item.damage != -1 ? item.damage : 0)).amount(item.getAmount())
                        .name("&b" + item.getName())
                        .addLore("&7Price: &a$" + formatDouble(item.getPrice()))
                        .addLore("&7Worth: &a$" + formatDouble(getPrice(new ItemStack(item.id, item.getAmount(), (short) (item.damage != -1 ? item.damage : 0)), item.getAmount())))
                        .build());
            }
            if (i >= 44 && i <= 88) {
                itemInventory2.addItem(new ItemBuilder(Material.getMaterial(item.getId())).durability((item.damage != -1 ? item.damage : 0)).amount(item.getAmount())
                        .name("&b" + item.getName())
                        .addLore("&7Price: &a$" + formatDouble(item.getPrice()))
                        .addLore("&7Worth: &a$" + formatDouble(getPrice(new ItemStack(item.id, item.getAmount(), (short) (item.damage != -1 ? item.damage : 0)), item.getAmount())))
                        .build());
            }
        }
    }

    public boolean isDurable(ItemStack item) {
        return (item.getType().getMaxDurability() > 0);
    }

    public Double getDurabilityPercentage(ItemStack item) {
        Double maxDura = (double) item.getType().getMaxDurability();
        Double dura = (double) item.getDurability();

        if (maxDura < 1) return 1D;
        return (1 - (dura / maxDura));
    }

    public Integer getItemNum(ItemStack item) {
        int itemnum = items.indexOf(new ShopItem(item.getTypeId(), "", 0, 0, item.getDurability()));
        if (itemnum == -1)
            itemnum = items.indexOf(new ShopItem(item.getTypeId(), "", 0, 0, -1));
        if (itemnum == -1)
            return null;
        return itemnum;
    }

    public Double getPrice(ItemStack item, Integer amount) {
        if (item == null) return null;
        Integer itemnum = getItemNum(item);
        if (itemnum == null) return null;

        if (amount == null) amount = item.getAmount();
        Double condition = getDurabilityPercentage(item);

        return (items.get(itemnum).price) * this.buysell * (amount) / (items.get(itemnum).amount) * condition;
    }

    public boolean sellAllItemInHand(Player player) {
        ItemStack item = player.getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            MessageManager.sendMessage(player, "&cYou must be holding an item in your hand to sell.");
            return false;
        }

        Double price = getPrice(item, 1);

        if (price == null) {
            MessageManager.sendMessage(player, "&cThis item cannot be sold.");
            return false;
        }

        String itemText;
        Integer itemnum = getItemNum(item);

        if (itemnum == null) {
            MessageManager.sendMessage(player, "&cThis item cannot be sold.");
            return false;
        }

        itemText = items.get(itemnum).name;

        if (item.getType() == Material.POTION) {
            if (item.getDurability() != items.get(itemnum).getDamage()) {
                MessageManager.sendMessage(player, "&cThis item cannot be sold.");
            }
        }

        if (isDurable(item)) itemText = itemText + "@" + (Math.round(getDurabilityPercentage(item) * 100)) + "%";

        Integer amount = 0;
        price = 0.0;
        List<ItemStack> removeList = new ArrayList<>();
        Inventory inv = player.getInventory();

        for (ItemStack is : inv.all(item.getType()).values()) {
            amount += is.getAmount();
            price += getPrice(is, is.getAmount());
            removeList.add(is);
        }

        addFunds(player, price);
        MessageManager.sendMessage(player, "&7You got &a$" + formatDouble(price) + " &7for selling &a" + amount + " " + itemText + "&7.");

        removeList.forEach(inv::remove);

        return true;
    }

    public boolean sellItemInHand(Player player, Integer amount) {
        ItemStack item = player.getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            MessageManager.sendMessage(player, "&cYou must be holding an item in your hand to sell.");
            return false;
        }

        Double price = getPrice(item, amount);

        if (price == null) {
            MessageManager.sendMessage(player, "&cThis item cannot be sold.");
            return false;
        }

        ItemStack holdingitem = player.getItemInHand();
        String itemText;
        Integer itemnum = getItemNum(item);

        if (itemnum == null) {
            MessageManager.sendMessage(player, "&cThis item cannot be sold.");
            return false;
        }

        itemText = items.get(itemnum).name;

        if (item.getType() == Material.POTION) {
            if (item.getDurability() != items.get(itemnum).getDamage()) {
                MessageManager.sendMessage(player, "&cThis item cannot be sold.");
            }
        }

        if (isDurable(item)) itemText = itemText + "@" + (Math.round(getDurabilityPercentage(item) * 100)) + "%";

        if (amount == null) {
            amount = holdingitem.getAmount();
        } else {
            if (amount > holdingitem.getAmount()) {
                MessageManager.sendMessage(player, "&cYou do not have enough of that item!");
                return false;
            }
        }

        addFunds(player, price);
        MessageManager.sendMessage(player, "&7You got &a$" + formatDouble(price) + " &7for selling &a" + amount + " " + itemText + "&7.");

        if (amount == null) {
            player.setItemInHand(null);
        } else {
            holdingitem.setAmount(holdingitem.getAmount() - amount);
            if (holdingitem.getAmount() == 0) player.setItemInHand(null);
        }

        return true;
    }

    public int getFreeInventorySpace(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        int count = 0;

        for (ItemStack content : contents) {
            if (content == null)
                count++;
        }

        return count;
    }

    public void addItem(Player player, ItemStack stack) {
        if (stack.getMaxStackSize() > 1) {
            player.getInventory().addItem(stack);
        } else {
            int quantity = stack.getAmount();
            stack.setAmount(1);

            for (int i = 0; i < quantity; i++) {
                player.getInventory().addItem(stack);
            }
        }
    }

    public Integer getAmount(String amountString, Player p) throws NumberFormatException {
        int amount = 0;

        try {
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException e) {
            MessageManager.sendMessage(p, "&cThe amount must be a number.");
        }

        if (amount < 1) {
            MessageManager.sendMessage(p, "&cThe amount must be greater than 0.");
            return null;
        }

        return amount;
    }

    public ShopItem getShopItem(String[] args, Player player, int searchid) {
        int itemnum = items.indexOf(new ShopItem(searchid, args[0], 0, 0, -1));

        if (itemnum == -1) {
            MessageManager.sendMessage(player, "&c" + args[0] + " is not for sale.");
            return null;
        }

        return items.get(itemnum);
    }

    public void subtractFunds(Player p, double amount) {
        pm.getProfile(p.getUniqueId()).setBalance(pm.getProfile(p.getUniqueId()).getBalance() - amount);
    }

    public void addFunds(Player p, Double amount) {
        pm.getProfile(p.getUniqueId()).setBalance(pm.getProfile(p.getUniqueId()).getBalance() + amount);
    }

    public boolean hasEnough(Player p, Double amount) {
        return pm.getProfile(p.getUniqueId()).getBalance() >= amount;
    }

    public Inventory getItemInventory1() {
        return itemInventory1;
    }

    public Inventory getItemInventory2() {
        return itemInventory2;
    }

    public ArrayList<ShopItem> getItems() {
        return items;
    }

    public class ShopItem {

        public int id;
        public double price;
        public String name;
        public int amount;
        public int damage = -1;

        public ShopItem(int id, String name, int amount, double price) {
            this.id = id;
            this.price = price;
            this.name = name;
            this.amount = amount;
            this.damage = -1;
        }

        public ShopItem(int id, String name, int amount, double price, int damage) {
            this.id = id;
            this.price = price;
            this.name = name;
            this.amount = amount;
            this.damage = damage;
        }

        public String getName() {
            return name;
        }

        public int getAmount() {
            return amount;
        }

        public double getPrice() {
            return price;
        }

        public int getDamage() {
            return damage;
        }

        public int getId() {
            return id;
        }

        public boolean equals(Object o) {
            ShopItem obj = (ShopItem) o;
            if (obj.id == id) {
                if (damage != -1) {
                    if (damage == obj.damage) {
                        return true;
                    }
                } else {
                    return true;
                }
            }

            if (name.equalsIgnoreCase(obj.name)) {
                if (damage == -1) {
                    return true;
                } else {
                    if (damage == obj.damage) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public String formatDouble(double d) {
        DecimalFormat df2 = new DecimalFormat("#,##0.00");
        return df2.format(d);
    }
}
