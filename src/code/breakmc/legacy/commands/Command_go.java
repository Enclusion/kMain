package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import code.breakmc.legacy.warps.WarpManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_go extends BaseCommand {

    private WarpManager wm = Legacy.getInstance().getWarpManager();

    public Command_go() {
        super("go", null, CommandUsageBy.PlAYER, "warp");
        setUsage("&cImproper usage! /go help");
        setMinArgs(0);
        setMaxArgs(2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            if (wm.getWarps().get(p.getUniqueId()).size() > 0) {
                wm.listWarps(p.getUniqueId());
            } else {
                MessageManager.sendMessage(p, "&7&m--------------&r &bWarps &7&m--------------");
                MessageManager.sendMessage(p, "&a/go &7- Lists your warps.");
                MessageManager.sendMessage(p, "&a/go list &7- Lists your warps");
                MessageManager.sendMessage(p, "&a/go set &b(name) &7- Sets a warp at your location.");
                MessageManager.sendMessage(p, "&a/go del &b(name) &7- Deletes the warp.");
                MessageManager.sendMessage(p, "&7&m----------------------------------");
                return;
            }
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                MessageManager.sendMessage(p, "&7&m--------------&r &bWarps &7&m--------------");
                MessageManager.sendMessage(p, "&a/go &7- Lists your warps.");
                MessageManager.sendMessage(p, "&a/go list &7- Lists your warps");
                MessageManager.sendMessage(p, "&a/go set &b(name) &7- Sets a warp at your location.");
                MessageManager.sendMessage(p, "&a/go del &b(name) &7- Deletes the warp.");
                MessageManager.sendMessage(p, "&7&m----------------------------------");
                return;
            }

            if (args[0].equalsIgnoreCase("list")) {
                wm.listWarps(p.getUniqueId());
                return;
            }

            if (args[0].equalsIgnoreCase("set")) {
                MessageManager.sendMessage(p, "&cImproper Usage! /go set (name)");
                return;
            }

            if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
                MessageManager.sendMessage(p, "Improper Usage! /go del (name)");
                return;
            }

            wm.warpTeleport(p, args[0]);
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                wm.setWarp(p.getUniqueId(), args[1], p.getLocation());
                return;
            }

            if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
                wm.removeWarp(p.getUniqueId(), args[1]);
            }
        }
    }
}
