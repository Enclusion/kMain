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

public class EconomyManager {

    private kMain main = kMain.getInstance();
    private ProfileManager pm = main.getProfileManager();

    private ArrayList<Sale> sales = new ArrayList<>();
    private MongoCollection<Document> sCollection = main.getMongoDatabase().getCollection("sales");

    public EconomyManager() {
        loadSales();

        new BukkitRunnable() {
            @Override
            public void run() {
                saveSales(true);
            }
        }.runTaskTimer(main, 0L, 300*20L);
    }

    public void loadSales() {
        if (sCollection.count() > 0) {
            MessageManager.debug("&7Preparing to load &c" + sCollection.count() + " &7sales.");

            for (Document document : sCollection.find()) {
                UUID transactionID = UUID.fromString(document.getString("transactionID"));
                UUID seller = UUID.fromString(document.getString("seller"));

                Document itemData = (Document) document.get("itemStack");
                Material material = Material.matchMaterial(itemData.getString("material"));
                int durability = itemData.getInteger("durability");

                ItemStack newItem = new ItemStack(material, 1, (short) durability);
                double price = document.getDouble("price");

                sales.add(new Sale(transactionID, seller, newItem, price));
            }

            Collections.sort(sales);

            MessageManager.debug("&7Successfully loaded &c" + sCollection.count() + " &7sales.");
        }
    }

    public void saveSales(boolean async) {
        if (sales.size() > 0) {
            MessageManager.debug("&7Preparing to save &c" + sales.size() + " &7sales.");

            if (async) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Sale sale : sales) {
                            sCollection.replaceOne(Filters.eq("transactionID", sale.getTransactionID().toString()), sale.toDocument(), new UpdateOptions().upsert(true));
                        }

                        MessageManager.debug("&7Successfully saved &c" + sales.size() + " &7sales.");
                    }
                }.runTaskAsynchronously(main);
            } else {
                for (Sale sale : sales) {
                    sCollection.replaceOne(Filters.eq("transactionID", sale.getTransactionID().toString()), sale.toDocument(), new UpdateOptions().upsert(true));
                }

                MessageManager.debug("&7Successfully saved &c" + sales.size() + " &7sales.");
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

        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<Sale> sales = getSpecificSales(itemStack, priceLimit);

                if (sales.size() < itemStack.getAmount()) {
                    MessageManager.message(buyer, "&7There is not enough " + getItemName(itemStack) + " on the market.");
                    return;
                }

                double totalPrice = sales.stream().mapToDouble(Sale::getPrice).sum();

//        Bukkit.broadcastMessage("TotalPrice: " + totalPrice);
//        Bukkit.broadcastMessage("PriceLimit: " + priceLimit);
//        Bukkit.broadcastMessage("TotalPricev2: " + (totalPrice / sales.size()));
//        Bukkit.broadcastMessage("Sales: " + sales.size());
//        Bukkit.broadcastMessage("ItemStack: " + itemStack.toString());
//        Bukkit.broadcastMessage("TotalPrice > PriceLimit: " + (totalPrice > priceLimit));
//        Bukkit.broadcastMessage("TotalPrice / Stack Amount > Price Limit: " + ((totalPrice / itemStack.getAmount()) > priceLimit));

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

                        sCollection.deleteOne(Filters.eq("transactionID", sale.getTransactionID().toString()));
                        getSales().remove(sale);
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
        }.runTaskAsynchronously(main);
    }

    public void showPrice(Player player, ItemStack itemStack) {
        if (itemStack.getAmount() > 64) {
            MessageManager.message(player, "&7You can only check the price for 64 items at a time.");
            return;
        }

        if (getSpecificSales(itemStack).size() < itemStack.getAmount()) {
            MessageManager.message(player, "&7There is not enough " + getItemName(itemStack) + " on the market.");
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                double totalPrice = 0.0;

                for (Sale sale : getSpecificSales(itemStack)) {
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
        ArrayList<Sale> sales = new ArrayList<>();

        for (Sale sale : getSales()) {
            if (sale.getItemStack().getType() == itemStack.getType() && sale.getItemStack().getDurability() == itemStack.getDurability()) {
                if (sales.size() < itemStack.getAmount())
                    sales.add(sale);
            }
        }

        Collections.sort(sales);

        return sales;
    }

    public ArrayList<Sale> getSpecificSales(ItemStack itemStack, double price) {
        ArrayList<Sale> sales = new ArrayList<>();

        for (Sale sale : getSales()) {
            if (sale.getItemStack().getType() == itemStack.getType() && sale.getItemStack().getDurability() == itemStack.getDurability()) {
                if (sales.size() < itemStack.getAmount() && sale.getPrice() <= price) {
                    sales.add(sale);
                }
            }
        }

        Collections.sort(sales);

        return sales;
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
}
