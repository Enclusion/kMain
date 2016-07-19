package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import com.mccritz.kmain.warps.WarpManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoAsCommand extends BaseCommand {

    private WarpManager wm = kMain.getInstance().getWarpManager();
    private ProfileManager pm = kMain.getInstance().getProfileManager();

    public GoAsCommand() {
        super("goas", "kmain.goas", CommandUsageBy.PlAYER, "warpas");
        setUsage("&cImproper usage! /goas <player>");
        setMinArgs(0);
        setMaxArgs(3);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.sendMessage(p, "&7***Goas Help***");
            MessageManager.sendMessage(p, "&7/goas <player> - &cLists a players warps.");
            MessageManager.sendMessage(p, "&7/goas <player> <warp> - &cWarps you to their warp.");
            MessageManager.sendMessage(p, "&7/goas del <player> <warp> - &cRemoves a players warp.");
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("del")) {
                MessageManager.sendMessage(p, "&cImproper usage! /goas del <player> <warp>");
            } else {
                Profile prof = pm.getProfile(args[0]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&c" + args[0] + " &7could not be found.");
                    return;
                }

                wm.adminListWarps(p.getUniqueId(), prof.getUniqueId());
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("del")) {
                MessageManager.sendMessage(p, "&cImproper usage! /goas del <player> <warp>");
                return;
            } else {
                Profile prof = pm.getProfile(args[0]);

                if (prof == null) {
                    MessageManager.sendMessage(p, "&c" + args[0] + " &7could not be found.");
                    return;
                }

                wm.warpAdminTeleport(p, prof.getUniqueId(), args[1]);
            }
        }

        if (args.length == 3) {
            Profile prof = pm.getProfile(args[1]);

            if (prof == null) {
                MessageManager.sendMessage(p, "&c" + args[1] + " &7could not be found.");
                return;
            }

            wm.adminRemoveWarp(p.getUniqueId(), prof.getUniqueId(), args[2]);
        }
    }
}
