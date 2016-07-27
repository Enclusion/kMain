package com.mccritz.kmain.economy.commands;

import com.mccritz.kmain.economy.EconomyManager;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PriceCommand extends BaseCommand {

    private EconomyManager em = kMain.getInstance().getEconomyManager();

    public PriceCommand() {
        super("price", null, CommandUsageBy.PlAYER, "value");
        setMinArgs(2);
        setMaxArgs(2);
        setUsage("&c/<command> <amount> <item>");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

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

        em.showPrice(p, itemStack);
    }
}