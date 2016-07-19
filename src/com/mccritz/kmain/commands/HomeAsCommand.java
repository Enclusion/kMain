package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class HomeAsCommand extends BaseCommand {

    private ProfileManager pm = kMain.getInstance().getProfileManager();

    public HomeAsCommand() {
        super("homeas", "kmain.homeas", CommandUsageBy.PlAYER, "has");
        setUsage("&cImproper usage! /homeas help");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.sendMessage(p, "&7***HomeAs Help***");
            MessageManager.sendMessage(p, "&7/homeas <player> - &cWarps you to their home.");
        }

        if (args.length == 1) {
            Profile tprof = pm.getProfile(args[0]);

            if (tprof == null) {
                MessageManager.sendMessage(p, "&c" + args[0] + " &7could not be found.");
                return;
            }

            if (tprof.getHome() != null) {
                p.teleport(tprof.getHome());
                kMain.getInstance().getLogger().log(Level.INFO, "[Admin Teleport]: " + p.getName() + " to " + tprof.getName() + "'s home");
            } else {
                MessageManager.sendMessage(p, "&c" + tprof.getName() + " &7does not have a home set.");
            }
        }
    }

    public List<String> tabComplete(String[] args, CommandSender sender) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                List<String> list2return = toList(PlayerUtility.getOnlinePlayers()).stream().filter(opt -> opt.toLowerCase().startsWith(args[1])).collect(Collectors.toList());

                Collections.sort(list2return);

                return list2return;
            }
        }

        return new ArrayList<>();
    }

    public List<String> toList(Player[] array) {
        List<String> list = new ArrayList<>();
        for (Player t : array) {
            list.add(t.getName());
        }
        return list;
    }
}