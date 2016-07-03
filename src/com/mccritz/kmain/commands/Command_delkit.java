package com.mccritz.kmain.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.kits.Kit;
import com.mccritz.kmain.kits.KitManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class Command_delkit extends BaseCommand {

    private KitManager km = Legacy.getInstance().getKitManager();

    public Command_delkit() {
        super("setkit", "kmain.delkit", CommandUsageBy.PlAYER, "removekit");
        setUsage("&cImproper usage! /delkit <name>");
        setMinArgs(1);
        setMaxArgs(1);
    }

    public void execute(CommandSender sender, String[] args) {
        Kit k = km.getKit(args[0]);

        if (k == null) {
            MessageManager.sendMessage(sender, "&7Could not find a kit named &c" + args[0] + "&7.");
            return;
        }

        km.getKits().remove(k);
        km.getConfig().set("kits." + k.getName(), null);

        try {
            km.getConfig().save(km.getFile());
            MessageManager.sendMessage(sender, "&cFailed to remove from kits.yml!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageManager.sendMessage(sender, "&7Kit &c" + k.getName() + " &7has been removed.");
    }
}
