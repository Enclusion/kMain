package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.kits.Kit;
import code.breakmc.legacy.kits.KitManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Command_setkit extends BaseCommand {

    private KitManager km = Legacy.getInstance().getKitManager();

    public Command_setkit() {
        super("setkit", "legacy.setkit", CommandUsageBy.PlAYER, "addkit");
        setUsage("&cImproper usage! /setkit (name) (delay)");
        setMinArgs(2);
        setMaxArgs(2);
    }

    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        String kitName = args[0];
        int delay = args.length == 2 ? Integer.parseInt(args[1]) : 0;

        if (args.length == 2 && !isInteger(args[1])) {
            MessageManager.sendMessage(p, "&cPlease enter a valid number");
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

        MessageManager.sendMessage(p, "&aKit \"" + k.getName() + "\" has been setup with a delay of \"" + k.getDelay() + "\"");
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
