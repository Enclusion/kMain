package com.mccritz.kmain.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.utils.Cooldowns;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Command_tpa extends BaseCommand {

    public Command_tpa() {
        super("tpa", null, CommandUsageBy.PlAYER);
        setUsage("&cImproper usage! /tpa <player>");
        setMinArgs(1);
        setMaxArgs(1);
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (Bukkit.getPlayer(args[0]) != null) {
            Player target = Bukkit.getPlayer(args[0]);
            Profile profile = Legacy.getInstance().getProfileManager().getProfile(target.getUniqueId());

            if (Cooldowns.tryCooldown(player.getUniqueId(), "TPACooldown", 15000)) {
                if (target.hasPermission("kmain.tpa.bypass")) {
                    MessageManager.sendMessage(player, "&c" + target.getName() + " &7is not accepting teleport requests right now.");
                    return;
                }

                if (player.getName().equalsIgnoreCase(target.getName())) {
                    MessageManager.sendMessage(player, "&7You cannot request to teleport to yourself.");
                    return;
                }

                profile.requestTeleport(player, true);
                MessageManager.sendMessage(player, "&7You have sent a request to teleport to &c" + target.getDisplayName() + "&7.");
                MessageManager.sendMessage(target, "&c" + player.getDisplayName() + " &7has requested to teleport to you.");
            } else {
                MessageManager.sendMessage(player, "&7You are currently on cooldown for this command for &c" + (Cooldowns.getCooldown(player.getUniqueId(), "TPACooldown") / 1000) + "&7.");
            }
        } else {
            MessageManager.sendMessage(player, "&c" + args[0] + " &7could not be found.");
        }
    }

    public List<String> tabComplete(String[] args, CommandSender sender) {
        List<String> names = new ArrayList<>();

        for (Player all : PlayerUtility.getOnlinePlayers()) {
            names.add(all.getName());
        }

        Collections.sort(names);

        if ((sender instanceof Player)) {
            Player p = (Player) sender;

            if (args.length == 0) {
                return names;
            }

            if (args.length == 1) {
                return names.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
            }
        }

        return names;
    }

    public String[] fixArgs(String[] args) {
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, args.length - 1);
        return subArgs;
    }
}
