package code.breakmc.legacy.commands;

import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashSet;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_toggledm extends BaseCommand implements Listener {

    private static HashSet<String> toggled = new HashSet<>();

    public Command_toggledm() {
        super("toggledm", null, CommandUsageBy.PlAYER, "tdm", "dm");
        setUsage("&cImproper usage! /toggledm");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (!toggled.contains(p.getUniqueId().toString())) {
            toggled.add(p.getUniqueId().toString());
            MessageManager.sendMessage(p, "&aDeath messages have been disabled");
        } else {
            toggled.remove(p.getUniqueId().toString());
            MessageManager.sendMessage(p, "&aDeath messages have been enabled");
        }
    }

    public static HashSet<String> getToggled() {
        return toggled;
    }
}
