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

import java.util.Map;

public class WithdrawCommand extends BaseCommand {

    private EconomyManager em = kMain.getInstance().getEconomyManager();

    public WithdrawCommand() {
        super("withdraw", null, CommandUsageBy.PlAYER);
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

        int amount, balance = (int) em.getBalance(p.getUniqueId());

        if (args[0].equalsIgnoreCase("all")) {
            amount = balance;
        } else {
            try {
                amount = Integer.parseInt(args[0]);
            } catch (Exception e) {
                MessageManager.message(p, "&7You must enter a valid number.");
                return;
            }

            if (amount < 0) {
                MessageManager.message(p, "&7You cannot withdraw negative numbers.");
                return;
            }
        }

        if (amount == 0) {
            MessageManager.message(p, "&7You cannot withdraw zero gold.");
            return;
        }

        if (amount > balance) {
            MessageManager.message(p, "&7You do not have that much gold in your account.");
            return;
        }

        Map<Integer, ItemStack> leftover = p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, amount));

        if (!leftover.isEmpty()) {
            for (ItemStack item : leftover.values()) {
                amount -= item.getAmount();
            }
        }

        // 2nd check due to lack of inventory space.
        if (amount == 0) {
            MessageManager.message(p, "&7You do not have enough inventory space.");
            return;
        }

        em.withdraw(p.getUniqueId(), amount);
        MessageManager.message(p, "&7You have withdrawn " + (amount == balance ? "all" : "&c" + amount + "&7") + " of your gold.");
    }
}