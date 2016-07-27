package com.mccritz.kmain.commands;

import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.UUID;

public class ToggleDMCommand extends BaseCommand implements Listener {

    private static HashSet<UUID> toggled = new HashSet<>();

    public ToggleDMCommand() {
        super("toggledm", null, CommandUsageBy.PlAYER, "tdm", "dm");
        setUsage("&cImproper usage! /toggledm");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (!toggled.contains(p.getUniqueId())) {
            toggled.add(p.getUniqueId());
            MessageManager.message(p, "&7Death messages have been &cdisabled&7.");
        } else {
            toggled.remove(p.getUniqueId());
            MessageManager.message(p, "&7Death messages have been &aenabled&7.");
        }
    }

    public static HashSet<UUID> getToggled() {
        return toggled;
    }
}
