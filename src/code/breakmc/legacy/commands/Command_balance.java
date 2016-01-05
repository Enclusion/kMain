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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_balance extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private ProfileManager pm = main.getProfileManager();
    private EconomyManager eco = main.getEconomyManager();

    public Command_balance() {
        super("balance", null, CommandUsageBy.PlAYER, "bal", "money", "dinero");
        setUsage("&cImproper usage! /bal");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.sendMessage(p, "&7Balance: &a$" + eco.formatDouble(pm.getProfile(p.getUniqueId()).getBalance()));
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("top")) {
                new BukkitRunnable() {
                    public void run() {
                        HashMap<String, Double> map = new HashMap<>();
                        for (Profile profs : pm.getLoadedProfiles()) {
                            map.put(profs.getName(), profs.getBalance());
                        }

                        MessageManager.sendMessage(p, "&7&m***&r &3Top 10 Players &7&m***");

                        Object[] a = map.entrySet().toArray();
                        Arrays.sort(a, (o1, o2) -> ((Map.Entry<String, Double>) o2).getValue().compareTo(((Map.Entry<String, Double>) o1).getValue()));

                        int topten = 0;
                        for (Object e : a) {
                            if (topten <= 9) {
                                MessageManager.sendMessage(p, "&7" + (topten + 1) + " - &b" + ((Map.Entry<String, Double>) e).getKey() + "&7: &a$" + eco.formatDouble(((Map.Entry<String, Double>) e).getValue()));
                            }

                            topten++;
                        }
                    }
                }.runTaskAsynchronously(main);
            } else {
                Profile tprof = pm.getProfile(args[0]);

                if (tprof == null) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found.");
                    return;
                }

                MessageManager.sendMessage(p, "&b" + tprof.getName() + "&7's Balance: &a$" + eco.formatDouble(tprof.getBalance()));
            }
        }
    }
}
