package com.mccritz.kmain.economy.commands;

import com.mccritz.kmain.economy.EconomyManager;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuyCommand extends BaseCommand {

    private EconomyManager em = kMain.getInstance().getEconomyManager();

    public BuyCommand() {
        super("buy", null, CommandUsageBy.PlAYER);
        setMinArgs(3);
        setMaxArgs(3);
        setUsage("&c/<command> <amount> <item> <priceLimit>");
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

        double priceLimit;

        try {
            priceLimit = Double.parseDouble(args[2]);

            if (priceLimit < 0) {
                MessageManager.message(p, "&7You must enter a positive price limit.");
                return;
            } else if (priceLimit == 0) {
                MessageManager.message(p, "&7You must enter a price limit greater than zero.");
                return;
            } else if (priceLimit > em.getBalance(p.getUniqueId())) {
                MessageManager.message(p, "&7You do not have that much gold in your account.");
                return;
            }
        } catch (Exception e) {
            MessageManager.message(p, "&7You must enter a valid price limit.");
            return;
        }

        em.buy(p, itemStack, priceLimit);
    }
}