package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.economy.EconomyManager;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_baltop extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private ProfileManager pm = main.getProfileManager();
    private EconomyManager eco = main.getEconomyManager();

    public Command_baltop() {
        super("balancetop", null, CommandUsageBy.ANYONE, "baltop", "moneytop", "dinerotop");
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(final CommandSender sender, String[] args) {
        new BukkitRunnable() {
            public void run() {
                HashMap<String, Double> map = new HashMap<>();
                for (Profile profs : pm.getLoadedProfiles()) {
                    map.put(profs.getName(), profs.getBalance());
                }

                MessageManager.sendMessage(sender, "&7&m***&r &3Top 10 Players &7&m***");

                Object[] a = map.entrySet().toArray();
                Arrays.sort(a, (o1, o2) -> ((Map.Entry<String, Double>) o2).getValue().compareTo(((Map.Entry<String, Double>) o1).getValue()));

                int topten = 0;
                for (Object e : a) {
                    if (topten <= 9) {
                        MessageManager.sendMessage(sender, "&7" + (topten + 1) + " - &b" + ((Map.Entry<String, Double>) e).getKey() + "&7: &a$" + eco.formatDouble(((Map.Entry<String, Double>) e).getValue()));
                    }
                    topten++;
                }
            }
        }.runTaskAsynchronously(main);
    }
}
