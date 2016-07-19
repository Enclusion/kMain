package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BalanceCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private ProfileManager pm = main.getProfileManager();

    public BalanceCommand() {
        super("balance", null, CommandUsageBy.PlAYER, "bal", "money", "dinero");
        setUsage("&cImproper usage! /bal");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.sendMessage(p, "&7Balance: &c" + MessageManager.formatDouble(pm.getProfile(p.getUniqueId()).getGold()));
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("top")) {
                new BukkitRunnable() {
                    public void run() {
                        HashMap<String, Double> map = new HashMap<>();
                        for (Profile profs : pm.getLoadedProfiles()) {
                            map.put(profs.getName(), profs.getGold());
                        }

                        MessageManager.sendMessage(p, "&7&m***&r &3Top 10 Players &7&m***");

                        Object[] a = map.entrySet().toArray();
                        Arrays.sort(a, (o1, o2) -> ((Map.Entry<String, Double>) o2).getValue().compareTo(((Map.Entry<String, Double>) o1).getValue()));

                        int topten = 0;
                        for (Object e : a) {
                            if (topten <= 9) {
                                MessageManager.sendMessage(p, "&7" + (topten + 1) + " - &b" + ((Map.Entry<String, Double>) e).getKey() + "&7: &c" + MessageManager.formatDouble(((Map.Entry<String, Double>) e).getValue()));
                            }

                            topten++;
                        }
                    }
                }.runTaskAsynchronously(main);
            } else {
                Profile tprof = pm.getProfile(args[0]);

                if (tprof == null) {
                    MessageManager.sendMessage(p, "&c" + args[0] + " &7could not be found.");
                    return;
                }

                MessageManager.sendMessage(p, "&c" + tprof.getName() + " has a balance of &c" + MessageManager.formatDouble(tprof.getGold()) + "&7.");
            }
        }
    }
}
