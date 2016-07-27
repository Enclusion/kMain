package com.mccritz.kmain.economy;

import lombok.Getter;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public class Sale implements Comparable<Sale> {

    private UUID transactionID, seller;
    private ItemStack itemStack;
    private double price;

    public Sale(UUID transactionID, UUID seller, ItemStack itemStack, double price) {
        this.transactionID = transactionID;
        this.seller = seller;
        this.itemStack = itemStack;
        this.price = price;
    }

    public Sale(UUID seller, ItemStack itemStack, double price) {
        this(UUID.randomUUID(), seller, itemStack, price);
    }

    public Document toDocument() {
        Document document = new Document("transactionID", transactionID.toString());

        document.append("seller", seller.toString());
        document.append("itemStack", new Document("material", itemStack.getType().toString()).append("durability", itemStack.getDurability()));
        document.append("price", price);

        return document;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "seller=" + seller +
                ", stack=" + itemStack.toString() +
                ", price=" + price +
                '}';
    }

    @Override
    public int compareTo(Sale o) {
        return new Double(price).compareTo(o.price);
    }
}