package com.mccritz.kmain.economy;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class EconomyManager {

    private kMain main = kMain.getInstance();
    private ProfileManager pm = main.getProfileManager();

    private ArrayList<Sale> sales = new ArrayList<>();
    private MongoCollection<Document> salesCollection = main.getMongoDatabase().getCollection("sales");

    private boolean economyHalted = false;

    public EconomyManager() {
        salesCollection.createIndex(new Document("transactionId", 1));

        loadSales();

        new BukkitRunnable() {
            @Override
            public void run() {
                saveSales(true);
            }
        }.runTaskTimer(main, 0L, 300 * 20L);
    }

    public void loadSales() {
        if (salesCollection.count() > 0) {
            MessageManager.debug("&7Preparing to load &c" + salesCollection.count() + " &7sales.");

            long startTime = System.currentTimeMillis();

            for (Document document : salesCollection.find()) {
                UUID transactionID = UUID.fromString(document.getString("transactionID"));
                UUID seller = UUID.fromString(document.getString("seller"));

                Document itemData = (Document) document.get("itemStack");
                Material material = Material.matchMaterial(itemData.getString("material"));
                int durability = itemData.getInteger("durability");

                ItemStack newItem = new ItemStack(material, 1, (short) durability);
                double price = document.getDouble("price");

                sales.add(new Sale(transactionID, seller, newItem, price));
            }

            MessageManager.debug("&7Successfully loaded &c" + salesCollection.count() + " &7sales. Took (&c" + (System.currentTimeMillis() - startTime) + "ms&7).");
        }
    }

    public void saveSales(boolean async) {
        if (sales.size() > 0) {
            MessageManager.debug("&7Preparing to save &c" + sales.size() + " &7sales.");

            long startTime = System.currentTimeMillis();

            if (async) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Sale sale : cloneSales()) {
                            salesCollection.replaceOne(Filters.eq("transactionID", sale.getTransactionID().toString()), sale.toDocument(), new UpdateOptions().upsert(true));
                        }

                        MessageManager.debug("&7Successfully saved &c" + cloneSales().size() + " &7sales. Took (&c" + (System.currentTimeMillis() - startTime) + "ms&7).");
                    }
                }.runTaskAsynchronously(main);
            } else {
                for (Sale sale : cloneSales()) {
                    salesCollection.replaceOne(Filters.eq("transactionID", sale.getTransactionID().toString()), sale.toDocument(), new UpdateOptions().upsert(true));
                }

                MessageManager.debug("&7Successfully saved &c" + cloneSales().size() + " &7sales. Took (&c" + (System.currentTimeMillis() - startTime) + "ms&7).");
            }
        }
    }

    public void sell(Player seller, ItemStack itemStack, double price) {
        if (itemStack.getAmount() > 64) {
            MessageManager.message(seller, "&7You can only sell 64 items at a time.");
            return;
        }

        if (price < 0.01) {
            MessageManager.message(seller, "&7You can only sell at a minimum of 0.01 gold.");
            return;
        }

        if (!isFullyRepaired(itemStack)) {
            MessageManager.message(seller, "&7You cannot sell armor/weapons that have not been fully repaired.");
            return;
        }

        if (!seller.getInventory().containsAtLeast(itemStack, itemStack.getAmount())) {
            MessageManager.message(seller, "&7You do not have &c" + itemStack.getAmount() + " &7of &c" + getItemName(itemStack) + " &7in your inventory.");
            MessageManager.message(seller, "&cNote&7: You cannot sell items that are not fully repaired, have enchantments, or with custom names.");
            return;
        }

        double perUnit = price / itemStack.getAmount();

        for (int i = 0; i < itemStack.getAmount(); i++) {
            Sale sale = new Sale(seller.getUniqueId(), new ItemStack(itemStack.getType(), 1, itemStack.getDurability()), perUnit);
            sales.add(sale);
        }

        seller.getInventory().removeItem(itemStack);

        MessageManager.message(seller, "&7You have put &c" + itemStack.getAmount() + " &7of &c" + getItemName(itemStack) + " &7on the market for &c" + price + " &7gold.");
    }

    public void buy(Player buyer, ItemStack itemStack, double priceLimit) {
        if (itemStack.getAmount() > 64) {
            MessageManager.message(buyer, "&7You can only buy 64 items at a time.");
            return;
        }

        if (priceLimit < 0.01) {
            MessageManager.message(buyer, "&7The minimum price limit is 0.01.");
        }

        ArrayList<Sale> sales = getSpecificSales(itemStack, priceLimit);

        if (sales.size() < itemStack.getAmount()) {
            MessageManager.message(buyer, "&7There is not enough " + getItemName(itemStack) + " on the market.");
            return;
        }

        double totalPrice = sales.stream().mapToDouble(Sale::getPrice).sum();

        if (totalPrice > priceLimit) {
            MessageManager.message(buyer, "&c" + itemStack.getAmount() + " " + getItemName(itemStack) + " costs " + totalPrice + " gold which is more than your limit.");
            return;
        }

        int actualAmountBought = 0;
        double actualPrice = 0;
        Map<UUID, CompletedSale> completedSales = new HashMap<>();

        for (Sale sale : sales) {
            HashMap<Integer, ItemStack> leftover = buyer.getInventory().addItem(sale.getItemStack());

            if (leftover.isEmpty()) {
                actualAmountBought++;
                actualPrice += sale.getPrice();

                CompletedSale completedSale = completedSales.getOrDefault(sale.getSeller(), new CompletedSale());
                completedSale.add(sale.getPrice());
                completedSales.put(sale.getSeller(), completedSale);

                getSales().remove(sale);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        salesCollection.deleteOne(Filters.eq("transactionID", sale.getTransactionID().toString()));
                    }
                }.runTaskAsynchronously(main);
            } else {
                break;
            }
        }

        for (Entry<UUID, CompletedSale> data : completedSales.entrySet()) {
            UUID uuid = data.getKey();
            CompletedSale completedSale = data.getValue();

            deposit(uuid, completedSale.getPrice());

            Player seller = Bukkit.getPlayer(uuid);

            if (seller != null) {
                MessageManager.message(seller, "&6A player has bought " + completedSale.getAmount() + " of " + getItemName(itemStack) + " for " + completedSale.getPrice() + " gold. This has been deposited into your account.");
            }
        }

        withdraw(buyer.getUniqueId(), actualPrice);
        MessageManager.message(buyer, "&7You bought " + actualAmountBought + " " + getItemName(itemStack) + " for " + actualPrice + " gold.");
    }

    public void showPrice(Player player, ItemStack itemStack) {
        if (itemStack.getAmount() > 64) {
            MessageManager.message(player, "&7You can only check the price for 64 items at a time.");
            return;
        }

        ArrayList<Sale> sales = getSpecificSales(itemStack);

        if (sales.size() < itemStack.getAmount()) {
            MessageManager.message(player, "&7There is not enough " + getItemName(itemStack) + " on the market.");
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                double totalPrice = 0.0;

                for (Sale sale : sales) {
                    totalPrice += sale.getPrice();
                }

                MessageManager.message(player, "&c" + itemStack.getAmount() + " &7of &c" + getItemName(itemStack) + " &7costs &c" + totalPrice + " &7gold.");
            }
        }.runTaskAsynchronously(main);
    }

    public void deposit(UUID uuid, double amount) {
        Profile profile = pm.getProfile(uuid);
        profile.setGold(profile.getGold() + amount);
    }

    /*
     * This should only work for online players.
     * 
     * Offline players shouldn't even be able to withdraw.
     */
    public boolean withdraw(UUID uuid, double amount) {
        Profile profile = pm.getProfile(uuid);

        if (amount > profile.getGold())
            return false;
        else {
            profile.setGold(profile.getGold() - amount);
            return true;
        }
    }

    public String getItemName(ItemStack itemStack) {
        return main.getItemDb().name(itemStack);
    }

    public ItemStack getItemStack(String partial, int amount) throws Exception {
        return main.getItemDb().get(partial, amount);
    }

    public ArrayList<Sale> getSpecificSales(ItemStack itemStack) {
        Collections.sort(sales, (o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()));

        ArrayList<Sale> results = new ArrayList<>();

        for (Sale sale : getSales()) {
            if (sale.getItemStack().getType() == itemStack.getType() && sale.getItemStack().getDurability() == itemStack.getDurability()) {
                if (results.size() < itemStack.getAmount())
                    results.add(sale);
            }
        }

        Collections.sort(results, (o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()));

        return results;
    }

    public ArrayList<Sale> getSpecificSales(ItemStack itemStack, double price) {
        Collections.sort(sales, (o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()));

        ArrayList<Sale> results = new ArrayList<>();

        for (Sale sale : getSales()) {
            if (sale.getItemStack().getType() == itemStack.getType() && sale.getItemStack().getDurability() == itemStack.getDurability()) {
                if (results.size() < itemStack.getAmount()) {
                    if (sale.getPrice() <= price) {
                        results.add(sale);
                    }
                }
            }
        }

        Collections.sort(results, (o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()));

        return results;
    }

    private boolean isFullyRepaired(ItemStack itemStack) {
        String checkMaterial = itemStack.getType().name();

        return !(checkMaterial.contains("HELMET") || checkMaterial.contains("CHESTPLATE") || checkMaterial.contains("LEGGINGS") || checkMaterial.contains("BOOTS") || checkMaterial.contains("SWORD") || itemStack.getType() == Material.BOW) || itemStack.getDurability() == 0;
    }

    public double getBalance(UUID uuid) {
        return pm.getProfile(uuid).getGold();
    }

    public ArrayList<Sale> getSales() {
        return sales;
    }

    public ArrayList<Sale> cloneSales() {
        return sales.stream().collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean isEconomyHalted() {
        return economyHalted;
    }

    public void setEconomyHalted(boolean economyHalted) {
        this.economyHalted = economyHalted;
    }
}
