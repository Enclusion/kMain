package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.kits.Kit;
import com.mccritz.kmain.kits.KitManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SetKitCommand extends BaseCommand {

    private KitManager km = kMain.getInstance().getKitManager();

    public SetKitCommand() {
        super("setkit", "kmain.setkit", CommandUsageBy.PlAYER, "addkit");
        setUsage("&cImproper usage! /setkit <name> <delay>");
        setMinArgs(2);
        setMaxArgs(2);
    }

    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        String kitName = args[0];
        int delay = args.length == 2 ? Integer.parseInt(args[1]) : 0;

        if (args.length == 2 && !isInteger(args[1])) {
            MessageManager.message(p, "&cPlease enter a valid number.");
            return;
        }

        Kit k = km.getKit(kitName);

        if (k == null) {
            k = new Kit(kitName);
            km.getKits().add(k);
        }

        List<ItemStack> contents = new ArrayList<>();

        for(ItemStack is : p.getInventory()) {
            if (is == null) {
                continue;
            }

            contents.add(is.clone());
        }

        k.setContents(contents);
        k.setDelay(delay);

        MessageManager.message(p, "&7Kit &c" + k.getName() + " &7has been created with a delay of &c" + k.getDelay());
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }

        return true;
    }
}
