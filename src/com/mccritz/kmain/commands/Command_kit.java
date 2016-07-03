package com.mccritz.kmain.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.kits.Kit;
import com.mccritz.kmain.kits.KitManager;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command_kit extends BaseCommand {

    private KitManager km = Legacy.getInstance().getKitManager();
    private ProfileManager pm = Legacy.getInstance().getProfileManager();

    public Command_kit() {
        super("kit", null, CommandUsageBy.PlAYER, "kits");
        setUsage("&cImproper usage! /kit");
        setMinArgs(0);
        setMaxArgs(1);
    }

    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Profile prof = pm.getProfile(p.getUniqueId());

        if (args.length == 0) {
            if (km.getKits().isEmpty()) {
                MessageManager.sendMessage(p, "&7There are no kits available.");
                return;
            }

            List<String> kitList = new ArrayList<>();

            for (Kit k : km.getKits()) {
                if (!p.hasPermission("kits." + k.getName())) {
                    continue;
                }

                kitList.add(k.getName());
            }

            if (kitList.isEmpty()) {
                MessageManager.sendMessage(p, "&7There are no kits available.");
                return;
            }

            StringBuilder sb = new StringBuilder();

            for (Kit k : km.getKits()) {
                if (p.hasPermission("kits." + k.getName())) {
                    long nextUse = k.getNextUse(prof);

                    if (nextUse == 0L) {
                        if (sb.length() == 0) {
                            sb.append("&a").append(k.getName());
                        } else {
                            sb.append("&7, &a").append(k.getName());
                        }
                    }
                }
            }

            for (Kit k : km.getKits()) {
                if (p.hasPermission("kits." + k.getName())) {
                    long nextUse = k.getNextUse(prof);

                    if (nextUse != 0L) {
                        if (sb.length() == 0) {
                            sb.append("&c").append(k.getName());
                        } else {
                            sb.append("&7, &c").append(k.getName());
                        }
                    }
                }
            }

            MessageManager.sendMessage(p, "&7Kits Available: \n" + sb.toString());
        } else if (args.length == 1) {
            if (km.getKit(args[0]) == null) {
                MessageManager.sendMessage(p, "&7There is no kit &c" + args[0] + "&7.");
                return;
            }

            Kit k = km.getKit(args[0]);

            if (!p.hasPermission("kits." + k.getName()) && !p.hasPermission("kits.*")) {
                MessageManager.sendMessage(p, "&cYou are not allowed to do this.");
                return;
            }

            k.apply(p);
        }
    }
}
