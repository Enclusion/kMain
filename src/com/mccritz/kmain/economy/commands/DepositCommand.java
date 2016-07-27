package com.mccritz.kmain.economy.commands;

import com.mccritz.kmain.economy.EconomyManager;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DepositCommand extends BaseCommand {

    private EconomyManager em = kMain.getInstance().getEconomyManager();

    public DepositCommand() {
        super("deposit", null, CommandUsageBy.PlAYER);
        setMinArgs(1);
        setMaxArgs(1);
        setUsage("&c/<command> <all:amount>");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        Player p = (Player) s;
        int amount, total = 0;

        for (ItemStack item : p.getInventory().all(Material.GOLD_INGOT).values()) {
            total += item.getAmount();
        }

        if (args[0].equalsIgnoreCase("all")) {
            amount = total;
        } else {
            try {
                amount = Integer.parseInt(args[0]);
            } catch (Exception e) {
                MessageManager.message(p, "&7You must enter a valid number.");
                return;
            }

            if (amount < 0) {
                MessageManager.message(p, "&7You cannot deposit negative numbers.");
                return;
            }
        }

        if (amount == 0) {
            MessageManager.message(p, "&7You cannot deposit zero gold.");
            return;
        }

        if (amount > total) {
            MessageManager.message(p, "&7You do not have that much gold in your inventory.");
            return;
        }

        em.deposit(p.getUniqueId(), amount);
        p.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, amount));
        MessageManager.message(p, "&7You have deposited &c" + amount + " &7into your account.");
    }

}