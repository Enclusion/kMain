package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private ProfileManager pm = main.getProfileManager();

    public EconomyCommand() {
        super("economy", null, CommandUsageBy.ANYONE, "eco");
        setUsage("&cImproper usage! /economy");
        setMinArgs(0);
        setMaxArgs(3);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.sendMessage(p, "&7&m--------------&r &c&lEconomy &7&m--------------");
            MessageManager.sendMessage(p, "&c/buy &7- Purchase an item off the market.");
            MessageManager.sendMessage(p, "&c/sell &7- Sell an item on the market.");
            MessageManager.sendMessage(p, "&c/price &7- Find out the price of an item.");
            MessageManager.sendMessage(p, "&c/value &7- Find out the worth of an item.");
            MessageManager.sendMessage(p, "&c/balance &7- View your current gold.");
            MessageManager.sendMessage(p, "&c/baltop &7- View the top 10 richest players.");

            if (p.hasPermission("kmain.eco.admin")) {
                MessageManager.sendMessage(p, "&7&m---------------&r &c&lAdmin &7&m----------------");
                MessageManager.sendMessage(p, "&c/eco give &b(player) (amount) &7- Add to a players gold.");
                MessageManager.sendMessage(p, "&c/eco take &b(player) (amount) &7- Take from a players gold.");
                MessageManager.sendMessage(p, "&c/eco set &b(player) (amount) &7- Set a players gold.");
                MessageManager.sendMessage(p, "&c/eco clear &b(player) &7- Clear a players gold.");
            }

            MessageManager.sendMessage(p, "&7&m-------------------------------------");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("clear")) {
                if (!p.hasPermission("kmain.eco.admin")) {
                    MessageManager.sendMessage(p, "&cYou are not allowed to do this.");
                    return;
                }

                Profile prof = pm.getProfile(args[1]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&c" + args[1] + " &7could not be found.");
                    return;
                }

                prof.setGold(0.0);
                MessageManager.sendMessage(p, "&7You have cleared the gold of &c" + prof.getName() + "&7.");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) {
                if (!p.hasPermission("kmain.eco.admin")) {
                    MessageManager.sendMessage(p, "&cYou are not allowed to do this.");
                    return;
                }

                Profile prof = pm.getProfile(args[1]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&c" + args[1] + " &7could not be found.");
                    return;
                }

                try {
                    int amount = Integer.parseInt(args[2]);

                    if (amount <= 0) {
                        MessageManager.sendMessage(p, "&cThe amount must be greater than 0.");
                        return;
                    }

                    prof.setGold(prof.getGold() + amount);
                    MessageManager.sendMessage(p, "&7You have given &c" + MessageManager.formatDouble(amount) + " &7gold to &c" + prof.getName() + "&7.");
                } catch (NumberFormatException e) {
                    MessageManager.sendMessage(p, "&cThe amount must be a number.");
                }
            } else if (args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")) {
                if (!p.hasPermission("kmain.eco.admin")) {
                    MessageManager.sendMessage(p, "&cYou are not allowed to do this.");
                    return;
                }

                Profile prof = pm.getProfile(args[1]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&c" + args[1] + " &7could not be found.");
                    return;
                }

                try {
                    int amount = Integer.parseInt(args[2]);

                    if (amount <= 0) {
                        MessageManager.sendMessage(p, "&cThe amount must be greater than 0.");
                        return;
                    }

                    prof.setGold(prof.getGold() - amount);
                    MessageManager.sendMessage(p, "&7You have taken &c" + MessageManager.formatDouble(amount) + " &7gold from &c" + prof.getName() + "&7.");
                } catch (NumberFormatException e) {
                    MessageManager.sendMessage(p, "&cThe amount must be a number.");
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                if (!p.hasPermission("kmain.eco.admin")) {
                    MessageManager.sendMessage(p, "&cYou are not allowed to do this.");
                    return;
                }

                Profile prof = pm.getProfile(args[1]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&c" + args[1] + " &7could not be found.");
                    return;
                }

                try {
                    int amount = Integer.parseInt(args[2]);

                    prof.setGold(amount + 0.0);
                    MessageManager.sendMessage(p, "&7cYou have set the gold of &c" + prof.getName() + " &7to &c" + amount + "&7.");
                } catch (NumberFormatException e) {
                    MessageManager.sendMessage(p, "&cYou must input a number.");
                }
            }
        }
    }
}
