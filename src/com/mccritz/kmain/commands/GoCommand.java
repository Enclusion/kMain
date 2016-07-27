package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import com.mccritz.kmain.warps.WarpManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoCommand extends BaseCommand {

    private WarpManager wm = kMain.getInstance().getWarpManager();

    public GoCommand() {
        super("go", null, CommandUsageBy.PlAYER, "warp");
        setUsage("&cImproper usage! /go");
        setMinArgs(0);
        setMaxArgs(2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.message(p, "&7***/go help***");
            MessageManager.message(p, "&7/go - &cLists your warps.");
            MessageManager.message(p, "&7/go list - &cLists your warps");
            MessageManager.message(p, "&7/go set <name> - &cSets a warp at your location.");
            MessageManager.message(p, "&7/go del <name> - &cDeletes the warp.");
            MessageManager.message(p, "&7/go - &cDisplays this page.");
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                wm.listWarps(p.getUniqueId());
                return;
            }

            if (args[0].equalsIgnoreCase("set")) {
                MessageManager.message(p, "&cImproper Usage! /go set <name>");
                return;
            }

            if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
                MessageManager.message(p, "Improper Usage! /go del <name>");
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
