package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class EditSpawnCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private static ArrayList<UUID> editors = new ArrayList<>();

    public EditSpawnCommand() {
        super("editspawn", "kmain.editspawn", CommandUsageBy.PlAYER);
        setUsage("&cInvalid usage! /editspawn");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (!editors.contains(p.getUniqueId())) {
            MessageManager.message(p, "&7You are now able to edit the spawn.");

            editors.add(p.getUniqueId());
        } else {
            MessageManager.message(p, "&7You have left spawn edit mode.");

            editors.remove(p.getUniqueId());
        }
    }

    public static ArrayList<UUID> getEditors() {
        return editors;
    }
}
