package com.mccritz.kmain.economy.commands;

import com.mccritz.kmain.economy.EconomyManager;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SellCommand extends BaseCommand {

    private EconomyManager em = kMain.getInstance().getEconomyManager();

    public SellCommand() {
        super("sell", null, CommandUsageBy.PlAYER);
        super.setMinArgs(3);
        super.setMaxArgs(3);
        super.setUsage("&c/<command> <amount> <item> <price>");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (kMain.getInstance().getEconomyManager().isEconomyHalted()) {
            MessageManager.message(sender, "&cThe economy is temporarily disabled. The administrators will let you know when it is re-enabled.");
            return;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[0]);

            if (amount < 0) {
                MessageManager.message(p, "&7You must enter a positive amount.");
                return;
            } else if (amount == 0) {
                MessageManager.message(p, "&7You must enter an amount greater than zero.");
                return;
            }
        } catch (Exception e) {
            MessageManager.message(p, "&7You must enter a valid amount.");
            return;
        }

        ItemStack itemStack;

        try {
            itemStack = em.getItemStack(args[1], amount);
        } catch (Exception e) {
            MessageManager.message(p, "&7That is not a valid item.");
            return;
        }

        double price;

        try {
            price = Double.parseDouble(args[2]);

            if (price < 0) {
                MessageManager.message(p, "&7You must enter a positive price.");
                return;
            } else if (price == 0) {
                MessageManager.message(p, "&7You must enter a price greater than zero.");
                return;
            }
        } catch (Exception e) {
            MessageManager.message(p, "&7You must enter a valid price.");
            return;
        }

        em.sell(p, itemStack, price);
    }
}