package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.kits.Kit;
import code.breakmc.legacy.kits.KitManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class Command_delkit extends BaseCommand {

    private KitManager km = Legacy.getInstance().getKitManager();

    public Command_delkit() {
        super("setkit", "legacy.delkit", CommandUsageBy.PlAYER, "removekit");
        setUsage("&cImproper usage! /delkit (name)");
        setMinArgs(1);
        setMaxArgs(1);
    }

    public void execute(CommandSender sender, String[] args) {
        Kit k = km.getKit(args[0]);

        if (k == null) {
            MessageManager.sendMessage(sender, "&cCould not find a kit named \"" + args[0] + "\"");
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
        MessageManager.sendMessage(sender, "&aKit &b" + k.getName() + " &ahas been removed.");
    }
}
