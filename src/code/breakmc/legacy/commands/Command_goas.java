package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import code.breakmc.legacy.warps.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_goas extends BaseCommand {

    private WarpManager wm = Legacy.getInstance().getWarpManager();

    public Command_goas() {
        super("goas", "legacy.goas", CommandUsageBy.PlAYER, "warpas");
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(3);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.sendMessage(p, "&7&m--------------&r &bAdmin &7&m--------------");
            MessageManager.sendMessage(p, "&a/goas &b(player) &7- Lists a players warps.");
            MessageManager.sendMessage(p, "&a/goas &b(player) (warp) &7- Warps you to their warp.");
            MessageManager.sendMessage(p, "&a/goas del &b(player) (warp) &7- Removes a players warp.");
            MessageManager.sendMessage(p, "&7&m----------------------------------");
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("del")) {
                MessageManager.sendMessage(p, "&cImproper usage! /goas del (player) (warp)");
            } else {
                if (Bukkit.getOfflinePlayer(args[0]) == null) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found.");
                    return;
                }

                wm.adminListWarps(p.getUniqueId(), Bukkit.getOfflinePlayer(args[0]).getUniqueId());
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("del")) {
                MessageManager.sendMessage(p, "&cImproper usage! /goas del (player) (warp)");
            } else {
                if (Bukkit.getOfflinePlayer(args[0]) == null) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found.");
                    return;
                }

                wm.warpAdminTeleport(p, Bukkit.getOfflinePlayer(args[0]).getUniqueId(), args[1]);
            }
        }

        if (args.length == 3) {
            if (Bukkit.getOfflinePlayer(args[1]) == null) {
                MessageManager.sendMessage(p, "&cPlayer \"" + args[1] + "\" could not be found.");
                return;
            }

            wm.adminRemoveWarp(p.getUniqueId(), Bukkit.getOfflinePlayer(args[1]).getUniqueId(), args[2]);
        }
    }
}
