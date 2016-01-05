package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.kits.Kit;
import code.breakmc.legacy.kits.KitManager;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
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
                MessageManager.sendMessage(p, "&cThere are no kits available!");
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
                MessageManager.sendMessage(p, "&cThere are no kits available!");
                return;
            }

            StringBuilder sb = new StringBuilder();

            for (Kit k : km.getKits()) {
                if (p.hasPermission("kits." + k.getName())) {
                    long nextUse = k.getNextUse(prof);

                    if (nextUse == 0L) {
                        if (sb.length() == 0) {
                            sb.append("&b").append(k.getName());
                        } else {
                            sb.append("&7, &b").append(k.getName());
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

            MessageManager.sendMessage(p, "&aKits Available: \n" + sb.toString());
        } else if (args.length == 1) {
            if (km.getKit(args[0]) == null) {
                MessageManager.sendMessage(p, "&cThere is no kit \"" + args[0] + "\"");
                return;
            }

            Kit k = km.getKit(args[0]);

            if (!p.hasPermission("kits." + k.getName()) && !p.hasPermission("kits.*")) {
                MessageManager.sendMessage(p, "&cYou do not have permission to use this kit!");
                return;
            }

            k.apply(p);
        }
    }
}
