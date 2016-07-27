package com.mccritz.kmain.commands;

import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class DebugCommand extends BaseCommand {

    private static ArrayList<UUID> displayList = new ArrayList<>();

    public DebugCommand() {
        super("hud", "kmain.debug", CommandUsageBy.PlAYER);
        setUsage("&cImproper usage! /debug");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (displayList.contains(p.getUniqueId())) {
            MessageManager.message(p, "&7Debug messages have been &cdisabled&7.");

            displayList.remove(p.getUniqueId());
        } else {
            MessageManager.message(p, "&7Debug messages have been &aenabled&7.");

            displayList.add(p.getUniqueId());
        }
    }

    public static ArrayList<UUID> getDisplayList() {
        return displayList;
    }
}
