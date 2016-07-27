package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class LogoutCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private ProfileManager pm = main.getProfileManager();
    private static HashMap<UUID, BukkitRunnable> dontMove = new HashMap<>();
    private static HashMap<UUID, BukkitRunnable> counter = new HashMap<>();
    private static HashMap<UUID, Integer> count = new HashMap<>();

    public LogoutCommand() {
        super("logout", null, CommandUsageBy.PlAYER, "disconnect");
        setUsage("&cImproper usage! /logout");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player p = (Player) sender;
        final Profile prof = pm.getProfile(p.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (dontMove.containsKey(p.getUniqueId())) {
                    dontMove.get(p.getUniqueId()).cancel();
                    dontMove.remove(p.getUniqueId());
                }

                dontMove.put(p.getUniqueId(), new BukkitRunnable() {
                    public void run() {
                        p.kickPlayer(ChatColor.RED + "You have safely logged out of the server.");
                        prof.setSafeLogged(false);
                        counter.get(p.getUniqueId()).cancel();
                        counter.remove(p.getUniqueId());
                        dontMove.get(p.getUniqueId()).cancel();
                        dontMove.remove(p.getUniqueId());
                        count.remove(p.getUniqueId());
                        this.cancel();
                    }
                });

                dontMove.get(p.getUniqueId()).runTaskLaterAsynchronously(kMain.getInstance(), 10 * 20L);

                MessageManager.message(p, "&cLogging out of the server. Do not move.");

                count.put(p.getUniqueId(), 11);

                if (counter.containsKey(p.getUniqueId())) {
                    counter.get(p.getUniqueId()).cancel();
                    counter.remove(p.getUniqueId());
                }

                counter.put(p.getUniqueId(), new BukkitRunnable() {
                    public void run() {
                        if (count.get(p.getUniqueId()) <= 11 && count.get(p.getUniqueId()) >= 1) {
                            count.put(p.getUniqueId(), count.get(p.getUniqueId()) - 1);
                            MessageManager.message(p, "&cLogging out in " + count.get(p.getUniqueId()) + "..");
                        } else {
                            MessageManager.message(p, "&cLogging out..");
                            this.cancel();
                        }
                    }
                });

                prof.setSafeLogged(true);

                counter.get(p.getUniqueId()).runTaskTimerAsynchronously(main, 0L, 20);
            }
        }.runTaskAsynchronously(kMain.getInstance());
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
