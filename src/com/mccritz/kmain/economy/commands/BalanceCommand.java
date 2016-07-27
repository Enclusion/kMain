package com.mccritz.kmain.economy.commands;

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
        setUsage("&c<command>");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.message(p, "&7You currently have " + MessageManager.formatDouble(pm.getProfile(p.getUniqueId()).getGold()) + " gold in your account.");
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("top")) {
                if (!p.hasPermission("kmain.baltop")) {
                    MessageManager.message(p, getUsage());
                    return;
                }

                new BukkitRunnable() {
                    public void run() {
                        HashMap<String, Double> map = new HashMap<>();
                        for (Profile profs : pm.getLoadedProfiles()) {
                            map.put(profs.getName(), profs.getGold());
                        }

                        MessageManager.message(p, "&7&m***&r &3Top 10 Players &7&m***");

                        Object[] a = map.entrySet().toArray();
                        Arrays.sort(a, (o1, o2) -> ((Map.Entry<String, Double>) o2).getValue().compareTo(((Map.Entry<String, Double>) o1).getValue()));

                        int topten = 0;
                        for (Object e : a) {
                            if (topten <= 9) {
                                MessageManager.message(p, "&7" + (topten + 1) + " - &b" + ((Map.Entry<String, Double>) e).getKey() + "&7: &c" + MessageManager.formatDouble(((Map.Entry<String, Double>) e).getValue()));
                            }

                            topten++;
                        }
                    }
                }.runTaskAsynchronously(main);
            } else {
                MessageManager.message(p, getUsage());
            }
        }
    }
}
