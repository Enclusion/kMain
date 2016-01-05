package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_logout extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private ProfileManager pm = main.getProfileManager();
    private static HashMap<UUID, BukkitRunnable> dontMove = new HashMap<>();
    private static HashMap<UUID, BukkitRunnable> counter = new HashMap<>();
    private static HashMap<UUID, Integer> count = new HashMap<>();

    public Command_logout() {
        super("logout", null, CommandUsageBy.PlAYER, "disconnect");
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player p = (Player) sender;
        final Profile prof = pm.getProfile(p.getUniqueId());

        if (dontMove.containsKey(p.getUniqueId())) {
            dontMove.get(p.getUniqueId()).cancel();
        }

        dontMove.put(p.getUniqueId(), new BukkitRunnable() {
            public void run() {
                p.kickPlayer(ChatColor.YELLOW + "You have safely logged out of AdvancedPvP.");
                prof.setSafeLogged(false);
                counter.get(p.getUniqueId()).cancel();
                counter.remove(p.getUniqueId());
                count.remove(p.getUniqueId());
                dontMove.remove(p.getUniqueId());
            }
        });

        dontMove.get(p.getUniqueId()).runTaskLater(Legacy.getInstance(), 10 * 20L);
        MessageManager.sendMessage(p, "&cLogging out of the server! Do not move!");

        count.put(p.getUniqueId(), 11);

        counter.put(p.getUniqueId(), new BukkitRunnable() {
            public void run() {
                if (count.get(p.getUniqueId()) <= 11 && count.get(p.getUniqueId()) >= 1) {
                    count.put(p.getUniqueId(), count.get(p.getUniqueId()) - 1);
                    MessageManager.sendMessage(p, "&cLogging out in " + count.get(p.getUniqueId()) + "..");
                } else {
                    MessageManager.sendMessage(p, "&cLogging out..");
                }
            }
        });

        prof.setSafeLogged(true);

        counter.get(p.getUniqueId()).runTaskTimer(main, 0L, 20);
    }

    public static HashMap<UUID, BukkitRunnable> getCounter() {
        return counter;
    }

    public static HashMap<UUID, BukkitRunnable> getDontMove() {
        return dontMove;
    }

    public static HashMap<UUID, Integer> getCount() {
        return count;
    }
}
