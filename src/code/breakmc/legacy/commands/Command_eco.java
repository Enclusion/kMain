package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.economy.EconomyManager;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
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
        super("economy", null, CommandUsageBy.ANYONE, "balancetop", "moneytop", "dinerotop", "eco");
        setUsage("&cImproper usage! /economy");
        setMinArgs(0);
        setMaxArgs(3);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.sendMessage(p, "&e&m--------------&r &a&lEconomy &e&m--------------");
            MessageManager.sendMessage(p, "&a/buy &7- Purchase an item off the market.");
            MessageManager.sendMessage(p, "&a/sell &7- Sell an item on the market.");
            MessageManager.sendMessage(p, "&a/price &7- Find out the price of an item.");
            MessageManager.sendMessage(p, "&a/value &7- Find out the worth of an item.");
            MessageManager.sendMessage(p, "&a/balance &7- View your current balance.");
            MessageManager.sendMessage(p, "&a/baltop &7- View the top 10 richest players.");
            MessageManager.sendMessage(p, "&a/shop &7- Find out what is in the economy.");

            if (p.hasPermission("legacy.eco.admin")) {
                MessageManager.sendMessage(p, "&e&m---------------&r &a&lAdmin &e&m----------------");
                MessageManager.sendMessage(p, "&a/eco give &b(player) (amount) &7- Add to a players balance.");
                MessageManager.sendMessage(p, "&a/eco take &b(player) (amount) &7- Take from a players balance.");
                MessageManager.sendMessage(p, "&a/eco set &b(player) (amount) &7- Set a players balance.");
                MessageManager.sendMessage(p, "&a/eco clear &b(player) &7- Clear a players balance.");
                MessageManager.sendMessage(p, "&a/eco check &b(player) &7- Checks a players sold gems.");
            }

            MessageManager.sendMessage(p, "&e&m-------------------------------------");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("legacy.eco.admin")) {
                    MessageManager.sendMessage(p, "&7Shop Config reloaded.");
                    main.getEconomyManager().loadConfig();
                    main.getEconomyManager().loadShopGUI();
                } else {
                    MessageManager.sendMessage(p, "&cYou do not have permission to use this command.");
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("clear")) {
                if (!p.hasPermission("legacy.eco.admin")) {
                    MessageManager.sendMessage(p, "&cYou do not have permission to use this command.");
                    return;
                }

                Profile prof = pm.getProfile(args[1]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + args[1] + "\" could not be found.");
                    return;
                }

                prof.setBalance(0.0);
                MessageManager.sendMessage(p, "&7You have cleared &b" + prof.getName() + "'s &7balance.");
            }
            if (args[0].equalsIgnoreCase("check")) {
                if (!p.hasPermission("legacy.eco.admin")) {
                    MessageManager.sendMessage(p, "&cYou do not have permission to use this command.");
                    return;
                }

                Profile prof = pm.getProfile(args[1]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + args[1] + "\" could not be found.");
                    return;
                }

                MessageManager.sendMessage(p, "&b" + prof.getName() + "&7: &aE&7: &c" + prof.getEmeraldsSold() + " &bD&7: &c" + prof.getDiamondsSold() + " &6G&7: &c" + prof.getGoldSold() + " &7I: &c" + prof.getIronSold());
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) {
                if (!p.hasPermission("legacy.eco.admin")) {
                    MessageManager.sendMessage(p, "&cYou do not have permission to use this command.");
                    return;
                }

                Profile prof = pm.getProfile(args[1]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + args[1] + "\" could not be found.");
                    return;
                }

                try {
                    int amount = Integer.parseInt(args[2]);

                    if (amount <= 0) {
                        MessageManager.sendMessage(p, "&cThe amount must be greater than 0.");
                        return;
                    }

                    prof.setBalance(prof.getBalance() + amount);
                    MessageManager.sendMessage(p, "&7You have given &a$" + eco.formatDouble(amount) + " &7to &b" + prof.getName());
                } catch (NumberFormatException e) {
                    MessageManager.sendMessage(p, "&cThe amount must be a number.");
                }
            } else if (args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")) {
                if (!p.hasPermission("legacy.eco.admin")) {
                    MessageManager.sendMessage(p, "&cYou do not have permission to use this command.");
                    return;
                }

                Profile prof = pm.getProfile(args[1]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + args[1] + "\" could not be found.");
                    return;
                }

                try {
                    int amount = Integer.parseInt(args[2]);

                    if (amount <= 0) {
                        MessageManager.sendMessage(p, "&cThe amount must be greater than 0.");
                        return;
                    }

                    prof.setBalance(prof.getBalance() - amount);
                    MessageManager.sendMessage(p, "&7You have removed &a$" + eco.formatDouble(amount) + " &7from &b" + prof.getName());
                } catch (NumberFormatException e) {
                    MessageManager.sendMessage(p, "&cThe amount must be a number.");
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                if (!p.hasPermission("legacy.eco.admin")) {
                    MessageManager.sendMessage(p, "&cYou do not have permission to use this command.");
                    return;
                }

                Profile prof = pm.getProfile(args[1]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + args[1] + "\" could not be found.");
                    return;
                }

                try {
                    int amount = Integer.parseInt(args[2]);

                    prof.setBalance(amount + 0.0);
                    MessageManager.sendMessage(p, "&7You have set &b" + prof.getName() + "'s &7balance to &a$" + amount);
                } catch (NumberFormatException e) {
                    MessageManager.sendMessage(p, "&cYou must input a number.");
                }
            }
        }
    }
}
