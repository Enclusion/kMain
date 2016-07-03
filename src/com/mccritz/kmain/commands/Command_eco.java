package com.mccritz.kmain.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.economy.EconomyManager;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_eco extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private ProfileManager pm = main.getProfileManager();
    private EconomyManager eco = main.getEconomyManager();

    public Command_eco() {
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
            MessageManager.sendMessage(p, "&c/balance &7- View your current balance.");
            MessageManager.sendMessage(p, "&c/baltop &7- View the top 10 richest players.");
            MessageManager.sendMessage(p, "&c/shop &7- Find out what is in the economy.");

            if (p.hasPermission("kmain.eco.admin")) {
                MessageManager.sendMessage(p, "&7&m---------------&r &c&lAdmin &7&m----------------");
                MessageManager.sendMessage(p, "&c/eco give &b(player) (amount) &7- Add to a players balance.");
                MessageManager.sendMessage(p, "&c/eco take &b(player) (amount) &7- Take from a players balance.");
                MessageManager.sendMessage(p, "&c/eco set &b(player) (amount) &7- Set a players balance.");
                MessageManager.sendMessage(p, "&c/eco clear &b(player) &7- Clear a players balance.");
                MessageManager.sendMessage(p, "&c/eco check &b(player) &7- Checks a players sold gems.");
            }

            MessageManager.sendMessage(p, "&7&m-------------------------------------");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("kmain.eco.admin")) {
                    MessageManager.sendMessage(p, "&7Shop Config reloaded.");
                    main.reloadConfig();
                    main.getEconomyManager().loadConfig();
                    main.getEconomyManager().loadShopGUI();
                } else {
                    MessageManager.sendMessage(p, "&cYou are not allowed to do this.");
                }
            }
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

                prof.setBalance(0.0);
                MessageManager.sendMessage(p, "&7You have cleared the balance of &c" + prof.getName() + "&7.");
            }
            if (args[0].equalsIgnoreCase("check")) {
                if (!p.hasPermission("kmain.eco.admin")) {
                    MessageManager.sendMessage(p, "&cYou are not allowed to do this.");
                    return;
                }

                Profile prof = pm.getProfile(args[1]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&c" + args[1] + " &7could not be found.");
                    return;
                }

                MessageManager.sendMessage(p, "&b" + prof.getName() + "&7: &aE&7: &c" + prof.getEmeraldsSold() + " &bD&7: &c" + prof.getDiamondsSold() + " &6G&7: &c" + prof.getGoldSold() + " &7I: &c" + prof.getIronSold());
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

                    prof.setBalance(prof.getBalance() + amount);
                    MessageManager.sendMessage(p, "&7You have given &c$" + eco.formatDouble(amount) + " &7to &c" + prof.getName() + "&7.");
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

                    prof.setBalance(prof.getBalance() - amount);
                    MessageManager.sendMessage(p, "&7You have taken &c$" + eco.formatDouble(amount) + " &7from &c" + prof.getName() + "&7.");
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

                    prof.setBalance(amount + 0.0);
                    MessageManager.sendMessage(p, "&7cYou have set the balance of &c" + prof.getName() + " &7to &c" + amount + "&7.");
                } catch (NumberFormatException e) {
                    MessageManager.sendMessage(p, "&cYou must input a number.");
                }
            }
        }
    }
}
