package com.mccritz.kmain.economy.commands;

import com.mccritz.kmain.economy.EconomyManager;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
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

        if (kMain.getInstance().getEconomyManager().isEconomyHalted()) {
            MessageManager.message(s, "&cThe economy is temporarily disabled. The administrators will let you know when it is re-enabled.");
            return;
        }

        int amount, total = 0;

        boolean attemptDupe = false;
        ItemStack itemStack = null;

        for (ItemStack item : p.getInventory().all(Material.GOLD_INGOT).values()) {
            if (!item.hasItemMeta() && !(item.getItemMeta().hasDisplayName() || item.getItemMeta().hasLore())) {
                total += item.getAmount();
            } else {
                attemptDupe = true;
                itemStack = item;
            }
        }

        if (attemptDupe) {
            MessageManager.message(p, "&7You cannot sell renamed gold ingots.");

            for (Player all : PlayerUtility.getOnlinePlayers()) {
                if (all.hasPermission("kmain.dupe")) {
                    MessageManager.message(all, "&c[&4Dupe Attempt&c]: " + p.getName() + " attempted to dupe gold.");
                    MessageManager.message(all, "&cDisplayName: " + itemStack.getItemMeta().getDisplayName());
                }
            }
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